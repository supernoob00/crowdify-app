<template>
  <div class="content">
    <h1 class="campaign-name">{{ campaign.name }}</h1>
    <div class="campaign-description">
      <p>{{ campaign.description }}</p>
    </div>
    <h6>Funding Goal: ${{ campaign.fundingGoal / 100 }}</h6>
    <h6 class="total-backing">Total Backing: ${{ totalDonated / 100 }}</h6>
    <h2>Donations</h2>
    <donation-display v-for="donation in campaign.donations" :key="donation.id" :donation="donation"></donation-display>
  </div>
</template>

<script>
import DonationDisplay from './DonationDisplay.vue';
export default {
  components: {
    DonationDisplay
  },
  props: ['campaign'],
  computed: {
    totalDonated() {
      return this.campaign.donations.reduce((sum, currDonation) => sum += currDonation.amount, 0)
    }
  }

}
</script>

<style scoped>
.campaign-description {
  padding: 1em 0 0 0;
  margin: 1em 0 1em 0;
  border-top: 1px solid lightgrey;
}

.total-backing {
  padding-bottom: 1em;
  border-bottom: 1px solid lightgrey;
}
</style>
