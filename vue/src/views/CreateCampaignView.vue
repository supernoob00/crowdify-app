<template>
  <div class="container content">
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
        name: '',
        description: '',
        public: false,
      },
    }
  },
}
</script>

<style scoped>
.content {
  max-width: var(--standard-form-width);
}
</style>