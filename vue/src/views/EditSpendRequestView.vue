<template>
  <div class="content container">
    <h1>Edit SpendRequest</h1>
    <loading-screen v-if="isLoading"></loading-screen>
    <spend-request-form v-else :spend-request="spendRequestToEdit"></spend-request-form>
  </div>
</template>

<script>
import SpendRequestForm from '../components/SpendRequestForm.vue';
import LoadingScreen from '../components/LoadingScreen.vue';
import CampaignService from '../services/CampaignService';
export default {
  components: {
    SpendRequestForm,
    LoadingScreen
  },
  data() {
    return {
      isLoading: true,
      spendRequestToEdit: {}
    }
  },
  computed: {
    spendRequestId() {
      return parseInt(this.$route.params.spendRequestId);
    },
    campaignId() {
      return parseInt(this.$route.params.campaignId);
    }
  },
  methods: {
    async getSpendRequest() {
      try {
        const response = await CampaignService.getSpendRequestById(this.campaignId, this.spendRequestId);
        this.spendRequestToEdit = response.data;
        this.spendRequestToEdit.amount /= 100;
        this.spendRequestToEdit.endDate = this.spendRequestToEdit.endDate.slice(0, 10);
      } catch (error) {
        CampaignService.handleErrorResponse(this.$store, error, 'getting', 'spendRequest');
        this.$router.push({ name: 'SpendRequestView', params: { campaignId: this.campaignId, spendRequestId: this.spendRequestId } });
      } finally {
        this.isLoading = false;
      }
    }
  },
  async created() {
    await this.getSpendRequest()
  }
}
</script>

<style scoped>
.content {
  max-width: var(--standard-form-width);
}
</style>