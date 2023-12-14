<template>
    <div class="box campaign">
      <p class="title is-6">{{ totalCampaignCount }} campaigns funded</p>
      <p>{{ totalDonations }}</p>
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
                .reduce((total, dono) => total + dono, 0);
    },
    numberOfDonations() {
        return this.campaigns.map(cam => cam.donations)
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