<template>
  <div class="content">
    <h1>Edit Campaign</h1>
    <loading-screen v-if="isLoading"></loading-screen>
    <campaign-form class="form" v-else :campaign="campaign"></campaign-form>
  </div>
</template>

<script>
import CampaignForm from '../components/CampaignForm.vue';
import campaignService from '../services/CampaignService';
import LoadingScreen from '../components/LoadingScreen.vue';
export default {
  components: {
    CampaignForm,
    LoadingScreen
  },
  data() {
    return {
      isLoading: true,
      campaign: {
        name: '',
        description: ''
      },
    }
  },
  methods: {
    async retrieveCampaign() {
      const campaignId = parseInt(this.$route.params.id)
      try {
        const response = await campaignService.getCampaign(campaignId);
        this.campaign = response.data;
        this.campaign.startDate = this.campaign.startDate.slice(0, 10);
        this.campaign.endDate = this.campaign.endDate.slice(0, 10);
        this.campaign.fundingGoal /= 100;
      } catch (error) {
        campaignService.handleErrorResponse(this.$store, error, 'getting', 'campaign');
        this.$router.push({ name: 'CampaignView', params: { id: campaignId } })
      } finally {
        this.isLoading = false;
      }
    }
  },
  async created() {
    this.retrieveCampaign()
  }
}
</script>

<style scoped>
.form {
  max-width: 500px;
}
</style>