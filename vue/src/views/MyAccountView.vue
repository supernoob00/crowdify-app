<template>
  <div class="content">
    <h1>My Account</h1>
    <h3>{{ $store.state.user.username }}</h3>
    <loading-screen v-if="isLoading"></loading-screen>
    <div v-else>
      <div class="donations">
        <h3>Donations</h3>
        <user-donation-display v-for="donation in donations" :key="donation.id"
          :donation="donation"></user-donation-display>
      </div>
    </div>
  </div>
</template>

<script>
import UserDonationDisplay from '../components/UserDonationDisplay.vue';
import campaignService from '../services/CampaignService';
import LoadingScreen from '../components/LoadingScreen.vue';
export default {
  components: {
    UserDonationDisplay,
    LoadingScreen
  },
  data() {
    return {
      donations: [],
      isLoading: true
    }
  },
  computed: {
    currentUserId() {
      return parseInt(this.$route.params.id);
    }
  },
  methods: {
    async getDonations() {
      try {
        const response = await campaignService.getDonationsByUser(this.currentUserId);
        this.donations = response.data;
      } catch (error) {
        campaignService.handleErrorResponse(this.$store, error, 'getting', 'user donations');
      } finally {
        this.isLoading = false;
      }
    }
  },
  created() {
    this.getDonations()
  }
}
</script>

<style></style>