<template>
  <div class="content">
    <h1>{{ campaign.name }}</h1>
    <p>{{ campaign.description }}</p>
    <h6>Funding Goal: {{ campaign.fundingGoal }}</h6>
    <h6>Total Backing: {{ totalDonated }}</h6>
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

<style scoped></style>
