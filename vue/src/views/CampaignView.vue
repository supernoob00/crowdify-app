<template>
  <div>
    <div class="loading" v-if="isLoading">
      <img src="../assets/ping_pong_loader.gif" />
    </div>
    <div v-else>
      <campaign-details :campaign="campaign"></campaign-details>
    </div>
  </div>
</template>

<script>
import CampaignDetails from '@/components/CampaignDetails.vue';
import campaignService from '@/services/CampaignService';
export default {
  components: {
    CampaignDetails
  },
  data() {
    return {
      // campaign has junk default data while backend isn't ready
      campaign: {
        name: 'Poop',
        description: 'Just dropping some poop off as is my usual way of doing thangs. Just need some money to help out with this',
        fundingGoal: 1000,
        donations: [
          {
            donor: {
              name: 'Bob'
            },
            amount: 50,
            comment: 'Gotta support the poop train!'
          }
        ]
      },
      isLoading: true
    }
  },
  methods: {
    async retrieveCampaign() {
      try {
        let campaignId = parseInt(this.$route.params.id)
        const response = await campaignService.getCampaign(campaignId);
        this.campaigns = response.data;
      } catch (error) {
        campaignService.handleErrorResponse(this.$store, error, 'getting', 'campaign');
      } finally {
        this.isLoading = false;
      }
    }
  },
  async created() {
    this.isLoading = false;
    //for testing purposes, commented out api call while backend not ready
    // this.retrieveCampaign()
  }
}
</script>

<style scoped></style>