<template>
  <router-link :to="{ name: 'CampaignView', params: { id: campaign.id } }">
    <div class="box campaign">
      <p class="title is-6">{{ campaign.name }}</p>
      <p>${{ campaignFundingGoal }} Goal</p>
      <p class="is-italic fund-percent has-text-success">{{ `${campaignPercentage}% funded` }}</p>
    </div>
  </router-link>
</template>

<script>
import Util from '../services/Util';
export default {
  props: ['campaign'],
  computed: {
    campaignFundingGoal() {
      return Util.formatToMoney(this.campaign.fundingGoal);
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
  width: 225px;
}

a:link,
a:visited {
  color: black;
  text-decoration: none;
}
</style>