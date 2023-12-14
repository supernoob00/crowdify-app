<template>
  <div>
    <loading-screen v-if="isLoading"></loading-screen>
    <div v-else-if="campaign.deleted">
      <deleted-campaign-details :campaign="campaign" :spend-requests-obj="spendRequestObj"></deleted-campaign-details>
    </div>
    <div v-else>
      <campaign-details :campaign="campaign" :spend-requests-obj="spendRequestObj"></campaign-details>
    </div>
  </div>
</template>

<script>
import CampaignDetails from '../components/CampaignDetails.vue';
import CampaignService from '../services/CampaignService';
import LoadingScreen from '../components/LoadingScreen.vue';
import DeletedCampaignDetails from '../components/DeletedCampaignDetails.vue';
export default {
  components: {
    CampaignDetails,
    LoadingScreen,
    DeletedCampaignDetails
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
      return this.campaign.donations.filter(d => d.donor != null && d.donor.id === this.currentUser.id).length > 0;
    },
    canViewSpendRequests() {
      return (this.currentUser.id != undefined) && (this.isManager || this.isStakeHolder);
    }
  },
  methods: {
    async retrieveCampaign() {
      try {
        const response = await CampaignService.getCampaign(this.campaignId);
        this.campaign = response.data;
      } catch (error) {
        if (error.response.status === 403) {
          this.$router.push({ name: 'forbidden' })
        } else {
          CampaignService.handleErrorResponse(this.$store, error, 'getting', 'campaign');
        }
      }
    },
    async getSpendRequestsForCampaign() {
      try {
        const response = await CampaignService.getSpendRequestsByCampaignId(this.campaignId);
        this.spendRequestObj.list = response.data;
        this.spendRequestObj.canView = this.canViewSpendRequests;
      } catch (error) {
        CampaignService.handleErrorResponse(this.$store, error, 'getting', 'spend request');
      }
    }
  },
  async created() {
    //TODO: Bug here when logged in as Adi and it fails to pull Burn Pizza SR's
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