<template>
  <div class="content">
    <h1>My Account</h1>
    <div>
      <h3>Donations</h3>
      <user-donation-display v-for="donation in donations" :key="donation.id"
        :donation="donation"></user-donation-display>
    </div>
  </div>
</template>

<script>
import UserDonationDisplay from '../components/UserDonationDisplay.vue';
import campaignService from '../services/CampaignService';
export default {
  components: {
    UserDonationDisplay
  },
  data() {
    return {
      donations: []
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