<template>
  <div class="content">
    <h1>Edit SpendRequest</h1>
    <loading-screen v-if="isLoading"></loading-screen>
    <spend-request-form v-else :spend-request="spendRequest"></spend-request-form>
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
      spendRequest: {}
    }
  },
  methods: {
    async getSpendRequest() {
      const spendRequestId = parseInt(this.$route.params.id)
      try {
        const response = await campaignService.getSpendRequestById(spendRequestId);
        this.newSpendRequest = response.data;
        this.spendRequest.amount /= 100;
        this.spendRequest.endDate = this.spendRequest.endDate.slice(0, 10);
      } catch (error) {
        campaignService.handleErrorResponse(this.$store, error, 'getting', 'spendRequest');
        this.$router.push({ name: 'SpendRequestView', params: { id: spendRequestId } })
      } finally {
        this.isLoading = false;
      }
    }
  },
  async created() {
    this.getSpendRequest()
  }
}
</script>

<style scoped></style>