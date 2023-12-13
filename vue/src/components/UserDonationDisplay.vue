<template>
  <div class="block box donation">
    <p class="header">
      <span :class="amountColor" id="amount">${{ donationAmount }}</span>
      <span> to </span>
      <router-link :to="{ name: 'CampaignView', params: { id: donation.campaignId } }">
        <span class="has-text-weight-semibold is-underlined">{{ donation.campaignName }}</span>
      </router-link>
    </p>
    <p class="is-italic" id="comment"> {{ donation.comment }}</p>
  </div>
  <!-- <article class="message" :class="{ 'is-danger': donation.refunded }">
    <div class="message-body">
      <span class="amount">${{ donationAmount }}</span>
      <span> to </span>
      <router-link :to="{ name: 'CampaignView', params: { id: donation.campaignId } }" class="campaign-name">
        {{ donation.campaignName }}</router-link>
      <p class="comment title is-6 has-text-weight-normal mt-4">{{ donation.comment }}</p>
    </div>
  </article> -->
</template>

<script>
import Util from '../services/Util';
import { useDark } from '@vueuse/core';

export default {
  props: ['donation'],
  data() {
    return {
      isDark: useDark(this.$store.state.darkModeSettings)
    }
  },
  computed: {
    donationAmount() {
      return Util.formatToMoney(this.donation.amount);
    },
    amountColor() {
      return this.isDark ? { 'has-text-grey-lighter': true } : { 'has-text-grey': true };
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