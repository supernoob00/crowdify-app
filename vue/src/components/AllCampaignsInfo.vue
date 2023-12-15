<template>
    <div class="box info">
      <div class="info-card">
        <span class="title is-1">{{ totalCampaignCount }}</span>
        <span class="title is-5">Campaigns Funded</span>
      </div>
      <div class="vertical-line"></div>
      <div class="info-card">
        <span class="title is-1">${{ totalDonations }}</span>
        <span class="title is-5">In Donations</span>
      </div>
      <div class="vertical-line"></div>
      <div class="info-card">
        <span class="title is-1">{{ numberOfDonations }}</span>
        <span class="title is-5">Times Donated</span>
      </div>
    </div>
</template>

<script>
export default {
  components: {

  },
  props: ['campaigns'],
  computed: {
    totalCampaignCount() {
        return this.campaigns.length;
    },
    totalDonations() {
        return this.campaigns
                .map(cam => cam.donations)
                .flatMap(donos => donos)
                .map(dono => dono.amount)
                .reduce((total, dono) => total + dono, 0) / 100;
    },
    numberOfDonations() {
        return this.campaigns
                .map(cam => cam.donations)
                .reduce((count, donos) => count + donos.length, 0);
    }
  },
  methods: {
    isManager(c) {
      return c.managers.filter(m => m.username === this.currentUser.username).length > 0;
    }
  }
}
</script>

<style scoped>
  .info {
    display: flex;
    justify-content: space-around;
    margin-top: 32px;
  }

  .vertical-line {
    border-left: thick solid lightgray;
  }

  .info-card {
    display: flex;
    flex-direction: column;
    align-items: center;
  }
</style>