<template>
  <div class="block box donation">
    <p class="header">
      <span class="title is-6" id="donor-name">{{ donorUsername }}&nbsp;</span>
      <span>|</span>
      <span :class="amountColor" id="amount">&nbsp;${{ donationAmount }}</span>
    </p>
    <p class="is-italic" id="comment"> {{ donation.comment }}</p>
  </div>
  <!-- <article class="message" :class="{ 'is-danger': donation.refunded }">
    <div class="message-header">
      <p id="donor-name">{{ donorUsername }} </p>
    </div>
    <div class="message-body">
      <span id="amount">${{ donationAmount }}</span>
      <p class="title is-6 mt-3" id="comment" :class="{ 'has-text-danger': donation.refunded }">{{ donation.comment }}</p>
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
    donorUsername() {
      return this.donation.donor === null ? 'Anonymous Donor' : this.donation.donor.username;
    },
    donationAmount() {
      return Util.formatToMoney(this.donation.amount);
    },
    amountColor() {
      return this.isDark ? { 'has-text-grey-lighter': true } : { 'has-text-grey': true };
    }
  }
}
</script>

<style scoped></style>