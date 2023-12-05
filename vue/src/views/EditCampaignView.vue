<template>
  <div class="content">
    <h1>Edit Campaign</h1>
    <campaign-form :campaign="campaign"></campaign-form>
  </div>
</template>

<script>
import CampaignForm from '@/components/CampaignForm.vue';
import campaignService from '../services/CampaignService';
export default {
  components: {
    CampaignForm
  },
  data() {
    return {
      isLoading: true,
      campaign: {},
    }
  },
  methods: {
    async retrieveCampaign() {
      try {
        let campaignId = parseInt(this.$route.params.id)
        const response = await campaignService.getCampaign(campaignId);
        console.log(this.campaign)
        this.campaign = response.data;
      } catch (error) {
        campaignService.handleErrorResponse(this.$store, error, 'getting', 'campaign');
        this.$router.push({ name: 'CampaignView', params: { id: this.$route.params.id } })
      } finally {
        this.isLoading = false;
      }
    }
  },
  async created() {
    this.retrieveCampaign()
  }
}
</script>

<style scoped>
.content {
  max-width: 500px;
}
</style>