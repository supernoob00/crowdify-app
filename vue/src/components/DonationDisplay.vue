<template>
  <article class="message">
    <div class="message-header">
      <p id="donor-name">{{ donorUsername }} </p>
    </div>
    <div class="message-body">
      <span id="amount">${{ donationAmount }}</span>
      <p class="title is-6" id="comment">{{ donation.comment }}</p>
    </div>
  </article>
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
  max-width: 300px;
}

.message-body {
  padding: 1em;
}

#donor-name {
  margin-bottom: 0;
  font-weight: 600;
}

#amount {
  color: grey;
}
</style>