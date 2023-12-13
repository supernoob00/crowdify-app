<template>
  <router-link :to="{ name: 'CampaignView', params: { id: campaign.id } }">
    <div class="box" :class="campaignClass">
      <p class="has-text-weight-medium">{{ campaign.name }}</p>
      <p>${{ campaignFundingGoal }} Goal</p>
      <p class="is-italic fund-percent">{{ `${campaignPercentage}% funded` }}</p>
    </div>
  </router-link>
</template>

<script>
import Util from '../services/Util';
export default {
  props: ['campaign'],
  computed: {
    currentUser() {
      return this.$store.state.user;
    },
    isManager() {
      return this.campaign.managers.filter(m => m.username === this.currentUser.username).length > 0;
    },
    campaignFundingGoal() {
      return Util.formatToMoney(this.campaign.fundingGoal);
    },
    campaignClass() {
      if (this.campaign.public && this.isManager) {
        return { 'managed-public': true }
      }
      if (!this.campaign.public && this.isManager) {
        return { 'managed-private': true }
      }
      return {}
    },
    campaignDonationTotal() {
      return this.campaign.donations.reduce((sum, currDonation) => sum += currDonation.amount, 0);
    },
    campaignPercentage() {
      const totalDonated = this.campaign.donations.reduce((sum, currDonation) => sum += currDonation.amount, 0);
      return Math.trunc(totalDonated / this.campaign.fundingGoal * 100);
    }
  },
}

</script>

<style scoped>
.campaign {
  min-width: 150px;
  padding: 20px;
  text-align: center;
  background-color: var(--campaign-card-background);
  color: var(--font-color);
}

/* .managed-private {
  background-color: var(--user-is-manager-private);
}

.managed-public {
  background-color: var(--user-is-manager-public);
} */

a:link,
a:visited {
  color: black;
  text-decoration: none;
}

/* .fund-percent {
  color: green;
} */
</style>