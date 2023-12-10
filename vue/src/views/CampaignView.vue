<template>
  <div>
    <loading-screen v-if="isLoading"></loading-screen>
    <div v-else>
      <campaign-details :campaign="campaign" :spend-requests-obj="spendRequestObj"></campaign-details>
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
      spendRequestObj: {},
      isLoading: true
    }
  },
  computed: {
    currentUser() {
      return this.$store.state.user;
    },
    campaignId() {
      return parseInt(this.$route.params.id);
    },
    isManager() {
      return this.campaign.managers.filter(m => m.username === this.currentUser.username).length > 0;
    },
    isStakeHolder() {
      return this.campaign.donations.filter(d => d.donorId === this.currentUser.id).length > 0;
    },
    canViewSpendRequests() {
      
      return (this.currentUser.id != undefined) && (this.isManager || this.isStakeHolder);
    }
  },
  methods: {
    async retrieveCampaign() {
      try {
        const response = await campaignService.getCampaign(this.campaignId);
        this.campaign = response.data;
      } catch (error) {
        campaignService.handleErrorResponse(this.$store, error, 'getting', 'campaign');
      } finally {
        this.isLoading = false;
      }
    },
    async getSpendRequestsForCampaign() {
      try {
        const response = await campaignService.getSpendRequestsByCampaignId(this.campaignId);
        this.spendRequestObj.list = response.data;
        this.spendRequestObj.canView = this.canViewSpendRequests;
      } catch (error) {
        campaignService.handleErrorResponse(this.$store, error, 'getting', 'spend request');
      }
    }
  },
  async created() {
    await this.retrieveCampaign();
    if (this.canViewSpendRequests) {
      await this.getSpendRequestsForCampaign();
    }
    this.isLoading = false;
  }
}
</script>

<style scoped>
.body {
  display: flex;
}
</style>