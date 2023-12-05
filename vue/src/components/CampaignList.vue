<template>
  <div class="campaigns block">
    <router-link v-for="campaign in campaigns" :key="campaign.id"
      :to="{ name: 'CampaignView', params: { id: campaign.id } }">
      <div class="campaign" :class="campaignClass(campaign)">
        <p>{{ campaign.name }}</p>
        <p class="fund-percent">{{ `${campaignPercentage(campaign)}% funded` }}</p>
      </div>
    </router-link>
  </div>
</template>

<script>
export default {
  props: ['campaigns'],
  computed: {
    currentUser() {
      return this.$store.state.user;
    }
  },
  methods: {
    campaignClass(c) {
      if (c.public && c.managers.filter(m => m.username === this.currentUser.username).length > 0) {
        return { 'managed-public': true }
      }
      if ((!c.public && c.managers.filter(m => m.username === this.currentUser.username).length > 0)) {
        return { 'managed-private': true }
      }
      return {}
    },
    campaignPercentage(c) {
      const totalDonated = c.donations.reduce((sum, currDonation) => sum += currDonation.amount, 0);
      return Math.trunc(totalDonated / c.fundingGoal * 100);
    },
  }
}
</script>

<style scoped>
.campaigns {
  display: flex;
  margin-right: 20px;
  flex-wrap: wrap;
  column-gap: 20px;
  row-gap: 25px;
  justify-content: flex-start;
  align-items: center;
}

.campaign {
  min-width: 150px;
  border-radius: 10px;
  padding: 20px;
  text-align: center;
  background-color: lightgray;
}

.managed-private {
  background-color: var(--user-is-manager-private);
  color: black;
  border: none;
}

.managed-public {
  background-color: var(--user-is-manager-public);
  color: black;
  border: none;
}

a:link,
a:visited {
  color: black;
  text-decoration: none;
}

/* .fund-percent {
  color: green;
} */
</style>
