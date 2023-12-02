<template>
  <div>
    <div class="donation">
      <h4 class="donor-name">{{ donation.donor.name }}</h4>
      <p class="amount">${{ donation.amount / 100 }}</p>
      <p>{{ donation.comment }}</p>
    </div>
  </div>
</template>

<script>
export default {
  props: ['donation']
}
</script>

<style scoped>
.donation {
  border: 1px black solid;
  border-radius: 6px;
  margin: 10px;
  padding: 0.5em;
  max-width: 400px;
}

.donation .amount {
  margin: 0;
  color: grey;
}

.donor-name {
  margin-bottom: 5px;
}
</style>