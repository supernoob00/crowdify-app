<template>
  <div class="block box donation">
    <p class="header">
      <span class="title is-6" id="donor-name">{{ donorUsername }} </span>
      <span class="has-text-grey" id="amount">&nbsp;|&nbsp;${{ donationAmount }}</span>
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

export default {
  props: ['donation'],
  computed: {
    donorUsername() {
      return this.donation.donor === null ? 'Anonymous Donor' : this.donation.donor.username;
    },
    donationAmount() {
      return Util.formatToMoney(this.donation.amount);
    }
  }
}
</script>

<style scoped>
article {
  margin-right: 80px;
  /* max-width: 300px; */
}


.donation {
  margin-right: 40px;
}

.message-body {
  padding: 1em;
}

/* #donor-name {
  margin-bottom: 0;
  font-weight: 600;
}

#amount {
  color: grey;
} */
</style>