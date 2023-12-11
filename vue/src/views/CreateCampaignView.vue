<template>
  <div class="content">
    <h1>Create Campaign</h1>
    <campaign-form :campaign="newCampaign"></campaign-form>
  </div>
</template>

<script>
import CampaignForm from '../components/CampaignForm.vue';
export default {
  components: {
    CampaignForm
  },
  data() {
    return {
      isLoading: true,
      newCampaign: {
        id: -1,
        name: 'Creation Test',
        description: 'test',
        public: false,
        startDate: '2023-12-31',
        endDate: '2024-01-31',
        fundingGoal: 2
      },
    }
  },
}
</script>

<style scoped>
.content {
  max-width: 500px;
}
</style>