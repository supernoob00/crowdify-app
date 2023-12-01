<template>
  <div>
    <div class="campaign" v-for="campaign in campaigns" :key="campaign.id">
      <p>{{ campaign.name }}</p>
    </div>
  </div>
</template>

<script>
export default {
  props: ['campaigns']
}
</script>

<style scoped>
.campaign {
  background-color: grey;
}
</style>
