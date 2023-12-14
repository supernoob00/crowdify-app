<template>
  <div class="block box donation">
    <p class="header">
      <span class="title is-6" id="donor-name">{{ donorUsername }}&nbsp;</span>
      <span>|</span>
      <span :class="amountColor" id="amount">&nbsp;${{ donationAmount }}</span>
    </p>
    <p class="is-italic" id="comment"> {{ donation.comment }}</p>
  </div>
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