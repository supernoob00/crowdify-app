<template>
  <div>
    <loading-screen v-if="isLoading"></loading-screen>
    <div v-else>
      <campaign-details :campaign="campaign"></campaign-details>
    </div>
  </div>
</template>

<script>
import CampaignDetails from '../components/CampaignDetails.vue';
import campaignService from '../services/CampaignService';
import LoadingScreen from '../components/LoadingScreen.vue';
export default {
  components: {
    CampaignDetails,
    LoadingScreen
  },
  data() {
    return {
      campaign: {},
      isLoading: true
    }
  },
  methods: {
    async retrieveCampaign() {
      try {
        let campaignId = parseInt(this.$route.params.id)
        const response = await campaignService.getCampaign(campaignId);
        this.campaign = response.data;
      } catch (error) {
        campaignService.handleErrorResponse(this.$store, error, 'getting', 'campaign');
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

<style scoped></style>