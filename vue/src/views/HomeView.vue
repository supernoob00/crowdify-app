<template>
  <div class="home">
    <loading-screen v-if="isLoading"></loading-screen>
    <div v-else class="content">
      <all-campaigns-info :campaigns="campaigns"></all-campaigns-info>
      <header class="header">
        <h3 class="is-size-2">Campaigns</h3>
        <router-link :to="{ name: 'CreateCampaignView' }" class="button is-link"><i
            class="fa-solid fa-plus"></i>Campaign</router-link>
      </header>
      <campaign-list :campaigns="campaigns"></campaign-list>
    </div>
  </div>
</template>

<script>
import CampaignList from '../components/CampaignList.vue';
import CampaignService from '../services/CampaignService'
import LoadingScreen from '../components/LoadingScreen.vue';
import AllCampaignsInfo from '../components/AllCampaignsInfo.vue';
export default {
  components: {
    CampaignList,
    LoadingScreen,
    AllCampaignsInfo
  },
  data() {
    return {
      campaigns: [],
      isLoading: true
    }
  },
  methods: {
    async retrieveCampaigns() {
      try {
        const response = await CampaignService.listCampaigns();
        this.campaigns = response.data;
      } catch (error) {
        CampaignService.handleErrorResponse(this.$store, error, 'getting', 'campaigns');
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

.header * {
  margin-right: 30px;
}

.fa-plus {
  margin-right: 10px;
}
</style>
