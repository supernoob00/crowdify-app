<template>
  <div class="content container">
    <h1>Donate to {{ campaign.name }}</h1>
    <loading-screen v-if="isLoading"></loading-screen>
    <donation-form v-else :donation="editDonation"></donation-form>
  </div>
</template>

<script>
import CampaignService from '../services/CampaignService';
import LoadingScreen from '../components/LoadingScreen.vue';
import DonationForm from '../components/DonationForm.vue';
export default {
  components: {
    LoadingScreen,
    DonationForm
  },
  data() {
    return {
      editDonation: {
        id: -1,
        campaignId: parseInt(this.$route.params.id),
        comment: '',
      },
      campaign: {},
      isLoading: true,
    }
  },
  computed: {
    campaignId() {
      return parseInt(this.$route.params.id);
    },
  },
  methods: {
    async getCampaign() {
      try {
        const response = await CampaignService.getCampaign(this.campaignId);
        if (response.status === 200) {
          this.campaign = response.data;
        }
      } catch (error) {
        CampaignService.handleErrorResponse(this.$store, error, 'getting', 'campaign');
      } finally {
        this.isLoading = false;
      }
    },
  },
  async created() {
    await this.getCampaign();
  }
}
</script>

<style scoped>
.content {
  max-width: var(--standard-form-width);
}
</style>