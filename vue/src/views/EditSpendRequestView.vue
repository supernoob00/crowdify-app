<template>
  <div class="content">
    <h1>Edit SpendRequest</h1>
    <loading-screen v-if="isLoading"></loading-screen>
    <spend-request-form v-else :spend-request="updatedSpendRequest"></spend-request-form>
  </div>
</template>

<script>
import SpendRequestForm from '../components/SpendRequestForm.vue';
import LoadingScreen from '../components/LoadingScreen.vue';
import campaignService from '../services/CampaignService';
export default {
  components: {
    SpendRequestForm,
    LoadingScreen
  },
  data() {
    return {
      isLoading: true,
      updatedSpendRequest: {}
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
        const response = await campaignService.getSpendRequestById(this.campaignId, this.spendRequestId);
        this.updatedSpendRequest = response.data;
        this.updatedSpendRequest.amount /= 100;
        this.updatedSpendRequest.endDate = this.updatedSpendRequest.endDate.slice(0, 10);
      } catch (error) {
        campaignService.handleErrorResponse(this.$store, error, 'getting', 'spendRequest');
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

<style scoped></style>