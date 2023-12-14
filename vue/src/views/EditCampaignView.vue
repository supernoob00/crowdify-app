<template>
  <div class="content container">
    <h1>Edit Campaign</h1>
    <loading-screen v-if="isLoading"></loading-screen>
    <campaign-form v-else :campaign="campaign"></campaign-form>
  </div>
</template>

<script>
import CampaignForm from '../components/CampaignForm.vue';
import CampaignService from '../services/CampaignService';
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
  computed: {
    campaignId() {
      return parseInt(this.$route.params.id)
    }
  },
  methods: {
    retrieveCampaign() {
      CampaignService
        .getCampaign(this.campaignId)
        .then(response => {
          this.campaign = response.data;
          this.campaign.startDate = this.campaign.startDate.slice(0, 10);
          this.campaign.endDate = this.campaign.endDate.slice(0, 10);
          this.campaign.fundingGoal /= 100;
        })
        .catch(error => {
          CampaignService.handleErrorResponse(this.$store, error, 'getting', 'campaign');
          this.$router.push({ name: 'CampaignView', params: { id: this.campaignId } })
        })
        .finally(() => this.isLoading = false);
    }
  },
  created() {
    this.retrieveCampaign()
  }
}
</script>

<style scoped>
.content {
  max-width: var(--standard-form-width);
}
</style>