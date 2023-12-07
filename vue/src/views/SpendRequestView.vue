<template>
  <div>
    <loading-screen v-if="isLoading"></loading-screen>
    <div v-else class="content">
      <h1>Spend Request for {{ campaign.name }}</h1>
      <h3>{{ spendRequest.description }}</h3>
    </div>
  </div>
</template>

<script>
import campaignService from '../services/CampaignService';
import LoadingScreen from '../components/LoadingScreen.vue';
export default {
  components: {
    LoadingScreen,
  },
  data() {
    return {
      campaign: {},
      spendRequest: {},
      isLoading: true,
    }
  },
  computed: {
    spendRequestId() {
      return parseInt(this.$route.params.id);
    }
  },
  methods: {
    async getSpendRequest() {
      try {
        const response = await campaignService.getSpendRequestById(this.spendRequestId);
        if (response.status === 200) {
          this.spendRequest = response.data;
        }
      } catch (error) {
        campaignService.handleErrorResponse(this.$store, error, 'getting', 'spend request')
      }
    },
    async getCampaign() {
      try {
        const response = await campaignService.getCampaign(this.spendRequest.campaignId);
        if (response.status === 200) {
          this.campaign = response.data;
        }
      } catch (error) {
        campaignService.handleErrorResponse(this.$store, error, 'getting', 'campaign');
      }
    },
  },
  async created() {
    await this.getSpendRequest();
    await this.getCampaign();
    this.isLoading = false;
  }
}

</script>

<style scoped>
.form-section {
  max-width: 500px;
}
</style>