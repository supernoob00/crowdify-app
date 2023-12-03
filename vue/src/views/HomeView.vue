<template>
  <div class="home">
    <div v-if="isLoading">
      <div class="loading">
        <img src="../assets/ping_pong_loader.gif" />
      </div>
    </div>
    <div v-else class="content">
      <h1>Campaigns</h1>
      <campaign-list :campaigns="campaigns"></campaign-list>
      <br>
      <router-link :to="{ name: 'CreateCampaignView' }" class="button is-link">Create new Campaign</router-link>
    </div>
  </div>
</template>

<script>
import CampaignList from '../components/CampaignList.vue';
import campaignService from '@/services/CampaignService'
export default {
  components: {
    CampaignList
  },
  data() {
    return {
      // campaigns has junk default data while backend isn't ready
      campaigns: [{
        name: 'Poop',
        id: 1
      }],
      isLoading: false
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
    this.isLoading = false;
    // for testing purposes, commented out api call while backend not ready
    // await this.retrieveCampaigns();
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
