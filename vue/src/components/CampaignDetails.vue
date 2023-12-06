<template>
  <div class="content">
    <div class="header">
      <div>
        <h1 id="campaign-name">{{ campaign.name }}</h1>
        <span>Created by </span>
        <span class="campaign-creator">{{ campaign.creator.username }}</span>
      </div>
      <router-link v-if="isManager" class="button is-link"
        :to="{ name: 'EditCampaignView', params: { id: campaign.id } }">
        Edit Campaign</router-link>
    </div>
    <hr>
    <div id="campaign-description" class="block">{{ campaign.description }}</div>
    <div class="progress-container">
      <div class="block" id="progress-meter-heading">
        <h3 class="amount-raised">${{ totalDonated }}</h3>
        <span class="goal-text"> raised of ${{ fundingGoal }} Goal</span>
      </div>
      <progress class="progress is-success is-small" :value="totalDonated" :max="fundingGoal"></progress>
      <div class="num-donations">{{ numberOfDonations }} donations</div>
    </div>
    <hr>
    <h2 class="block">Donations</h2>
    <router-link class="button is-link block"
      :to="{ name: 'CreateDonationView', params: { id: campaign.id } }">Donate</router-link>
    <donation-display v-for="donation in donationsSortedByAmount" :key="donation.id"
      :donation="donation"></donation-display>
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
      return this.campaign.donations.reduce((sum, currDonation) => sum += currDonation.amount, 0) / 100;
    },
    fundingGoal() {
      return this.campaign.fundingGoal / 100;
    },
    numberOfDonations() {
      return this.campaign.donations.length;
    },
    donationsSortedByAmount() {
      const donationsCopy = [...this.campaign.donations]
      return donationsCopy.sort((d1, d2) => d2.amount - d1.amount);
    },
    isManager() {
      return this.campaign.managers.filter(m => m.username === this.$store.state.user.username).length > 0;
    }
  }

}
</script>

<style scoped>
.content {
  max-width: 75%;
  margin: 10px;
}

.header {
  display: flex;
  justify-content: space-between;
}

.header a {
  margin-top: 1em;
}

hr {
  margin: 1rem 0;
}

.progress-container {
  max-width: 500px;
}

.progress-container .goal-text,
.progress-container .num-donations {
  color: grey;
}

.progress-container .amount-raised {
  display: inline;
}

.progress-container progress {
  margin-bottom: 0.5rem;
}

.campaign-creator {
  font-weight: 600;
}
</style>
