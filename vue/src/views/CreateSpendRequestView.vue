<template>
  <div class="content container">
    <h1>Create SpendRequest</h1>
    <spend-request-form :spend-request="newSpendRequest"></spend-request-form>
  </div>
</template>

<script>
import SpendRequestForm from '../components/SpendRequestForm.vue';
export default {
  components: {
    SpendRequestForm,
  },
  data() {
    return {
      newSpendRequest: {
        id: -1,
        name: '',
        description: '',
        campaignId: parseInt(this.$route.params.id)
      }
    }
  },
}
</script>

<style scoped>
.content {
  max-width: var(--standard-form-width);
}
</style>