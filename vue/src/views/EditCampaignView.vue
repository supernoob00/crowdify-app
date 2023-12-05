<template>
  <div class="content">
    <h1>Edit Campaign</h1>
    <div class="loading" v-if="isLoading">
      <img src="../assets/ping_pong_loader.gif">
    </div>
    <campaign-form v-else :campaign="campaign"></campaign-form>
  </div>
</template>

<script>
import CampaignForm from '../components/CampaignForm.vue';
import campaignService from '../services/CampaignService';
export default {
  components: {
    CampaignForm
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
      try {
        const campaignId = parseInt(this.$route.params.id)
        const response = await campaignService.getCampaign(campaignId);
        this.campaign = response.data;
        this.campaign.startDate = this.campaign.startDate.slice(0, 10);
        this.campaign.endDate = this.campaign.endDate.slice(0, 10);
        this.campaign.fundingGoal /= 100;
      } catch (error) {
        campaignService.handleErrorResponse(this.$store, error, 'getting', 'campaign');
        this.$router.push({ name: 'CampaignView', params: { id: this.$route.params.id } })
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
.content {
  max-width: 500px;
}
</style>