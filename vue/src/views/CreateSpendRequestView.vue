<template>
  <div class="content">
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
        name: 'Creation Test name',
        description: 'Creation Test desc',
        endDate: "2024-01-19",
        amount: 10,
        campaignId: parseInt(this.$route.params.id)
      }
    }
  },
}
</script>

<style scoped></style>