<template>
  <div class="content">
    <h1>My Account</h1>
    <loading-screen v-if="isLoading"></loading-screen>
    <div class="body" v-else>
      <section class="mr-5">
        <h3>My Donations</h3>
        <user-donation-display v-for="donation in donations" :key="donation.id"
          :donation="donation"></user-donation-display>
      </section>
      <section class="campaigns-section">
        <div class="campaign-collection">
          <h3>My Public Campaigns</h3>
          <div class="campaigns block">
            <campaign-card v-for="campaign in publicOwnedCampaigns" :key="campaign.id"
              :campaign="campaign"></campaign-card>
            <span v-if="publicOwnedCampaigns.length === 0">You have no public campaigns</span>
          </div>
        </div>
        <div class="campaign-collection">
          <h3>My Private Campaigns</h3>
          <div class="campaigns block">
            <campaign-card v-for="campaign in privateOwnedCampaigns" :key="campaign.id"
              :campaign="campaign"></campaign-card>
            <span v-if="privateOwnedCampaigns.length === 0">You have no private campaigns</span>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script>
import UserDonationDisplay from '../components/UserDonationDisplay.vue';
import CampaignService from '../services/CampaignService';
import LoadingScreen from '../components/LoadingScreen.vue';
import CampaignCard from '../components/CampaignCard.vue';
export default {
  components: {
    UserDonationDisplay,
    LoadingScreen,
    CampaignCard
  },
  data() {
    return {
      donations: [],
      myCampaigns: [],
      isLoading: true
    }
  },
  computed: {
    currentUser() {
      return this.$store.state.user;
    },
    privateOwnedCampaigns() {
      return this.myCampaigns.filter(c => !c.public);
    },
    publicOwnedCampaigns() {
      return this.myCampaigns.filter(c => c.public);
    }
  },
  methods: {
    async getMyDonations() {
      try {
        const response = await CampaignService.getDonationsByUser(this.currentUser.id);
        this.donations = response.data;
      } catch (error) {
        CampaignService.handleErrorResponse(this.$store, error, 'getting', 'user donations');
      }
    },
    async getMyCampaigns() {
      try {
        const response = await CampaignService.getCampaignsUserManages(this.currentUser.id);
        this.myCampaigns = response.data;
      } catch (error) {
        CampaignService.handleErrorResponse(this.$store, error, 'getting', 'user campaigns');
      }
    }
  },
  created() {
    this.getMyDonations();
    this.getMyCampaigns();
    this.isLoading = false;
  }
}
</script>

<style scoped>
.campaigns-section {
  display: flex;
}

.campaigns {
  display: flex;
  flex-direction: column;
  margin-right: 20px;
  flex-wrap: wrap;
  column-gap: 20px;
  row-gap: 25px;
  justify-content: flex-start;
  align-items: center;
}

.campaign-collection {
  margin-bottom: 36px;
  margin-right: 36px;
}

.body {
  display: flex;
  max-width: 1000px;
}
</style>