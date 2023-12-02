<template>
  <div class="campaign">
    <h1>{{ campaign.name }}</h1>
    <p>{{ campaign.description }}</p>
    <p>{{ campaign.fundingGoal }}</p>
    <div class="donation" v-for="donation in campaign.donations" :key="donation.id">
      <p>{{ donation.user.name }}</p>
      <p>{{ donation.comment }}</p>
    </div>
  </div>
</template>

<script>
export default {
  props: ['campaign']
}
</script>

<style scoped>
.campaign{
  
}
</style>
