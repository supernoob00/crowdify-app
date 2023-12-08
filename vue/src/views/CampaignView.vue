<template>
  <div>
    <loading-screen v-if="isLoading"></loading-screen>
    <div v-else>
      <campaign-details :campaign="campaign" :spend-requests="spendRequests"></campaign-details>
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
    LoadingScreen,
  },
  data() {
    return {
      campaign: {},
      spendRequests: [],
      isLoading: true
    }
  },
  computed: {
    campaignId() {
      return parseInt(this.$route.params.id);
    }
  },
  methods: {
    async retrieveCampaign() {
      try {
        const response = await campaignService.getCampaign(this.campaignId);
        this.campaign = response.data;
      } catch (error) {
        campaignService.handleErrorResponse(this.$store, error, 'getting', 'campaign');
      }
    },
    async getSpendRequestsForCampaign() {
      try {
        const response = await campaignService.getSpendRequestsByCampaignId(this.campaignId);
        this.spendRequests = response.data;
        console.log(this.spendRequests)
      } catch (error) {
        campaignService.handleErrorResponse(this.$store, error, 'getting', 'campaign');
      }
    }
  },
  async created() {
    await this.retrieveCampaign()
    this.isLoading = false;
  }
}
</script>

<style scoped>
.body {
  display: flex;
}
</style>