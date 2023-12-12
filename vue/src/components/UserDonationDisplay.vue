<template>
  <article class="message" :class="{ 'is-danger': donation.refunded }">
    <div class="message-body">
      <span class="amount">${{ donationAmount }}</span>
      <span> to </span>
      <router-link :to="{ name: 'CampaignView', params: { id: donation.campaignId } }" class="campaign-name">
        {{ donation.campaignName }}</router-link>
      <p class="comment title is-6 has-text-weight-normal mt-4">{{ donation.comment }}</p>
    </div>
  </article>
</template>

<script>
import Util from '../services/Util';

export default {
  props: ['donation'],
  computed: {
    donationAmount() {
      return Util.formatToMoney(this.donation.amount);
    }
  }
}
</script>

<style scoped>
article {
  max-width: 300px;
}

.message-body {
  padding: 1em;
}

.amount {
  color: grey;
}

.comment {
  font-style: italic;
}

.campaign-name {
  font-weight: 600;
}
</style>