<template>
  <article class="message">
    <div class="message-header">
      <p id="donor-name">{{ donorUsername }} </p>
    </div>
    <div class="message-body">
      <span id="amount">${{ donation.amount / 100 }}</span>
      <p id="comment">{{ donation.comment }}</p>
    </div>
  </article>
</template>

<script>
export default {
  props: ['donation'],
  computed: {
    donorUsername() {
      return this.donation.donor === null ? 'Anonymous Donor' : this.donation.donor.username;
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