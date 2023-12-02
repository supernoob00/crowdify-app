<template>
  <div class="campaigns">
    <router-link v-for="campaign in campaigns" :key="campaign.id"
      :to="{ name: 'CampaignView', params: { id: campaign.id } }">
      <div class="campaign">{{ campaign.name }}</div>
    </router-link>
  </div>
</template>

<script>
export default {
  props: ['campaigns']
}
</script>

<style scoped>
.campaigns {
  display: flex;
  margin: 0 20px;
  flex-wrap: wrap;
  column-gap: 20px;
  row-gap: 25px;
  justify-content: flex-start;
  align-items: center;
}

.campaign {
  min-width: 150px;
  border-radius: 10px;
  padding: 40px;
  text-align: center;
  background-color: grey;
}

a:link,
a:visited {
  color: rgb(243, 243, 243);
  text-decoration: none;
}
</style>
