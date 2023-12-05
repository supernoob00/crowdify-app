<template>
  <div class="home">
    <div v-if="isLoading">
      <img src="../assets/ping_pong_loader.gif" />
    </div>
    <div v-else class="content">
      <h1>Campaigns</h1>
      <campaign-list :campaigns="campaigns"></campaign-list>
      <router-link :to="{ name: 'CreateCampaignView' }" class="button is-link">Create Campaign</router-link>
    </div>
  </div>
</template>

<script>
import CampaignList from '@/components/CampaignList.vue';
import campaignService from '@/services/CampaignService'
export default {
  components: {
    CampaignList
  },
  data() {
    return {
      campaigns: [{
        name: 'Poop',
        id: 1,
        public: true,
        managers: [
          {
            id: 3, username: "adi", authorities: [{ name: "ROLE_USER" }]
          }]
      }],
      isLoading: true
    }
  },
  methods: {
    async retrieveCampaigns() {
      try {
        const response = await campaignService.listCampaigns();
        this.campaigns = response.data;
      } catch (error) {
        campaignService.handleErrorResponse(this.$store, error, 'getting', 'campaigns');
      } finally {
        this.isLoading = false;
      }
    },
  },
  async created() {
    await this.retrieveCampaigns();
  }
}
</script>
<style scoped>
.header {
  display: flex;
  align-items: center;
}

.header h1 {
  margin-right: 10px;
}
</style>
