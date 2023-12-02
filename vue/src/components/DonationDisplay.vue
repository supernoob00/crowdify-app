<template>
  <article class="message">
    <div class="message-header">
      <p id="donor-name">{{ donation.donor.name }} </p>
    </div>
    <div class="message-body">
      <span id="amount">${{ donation.amount / 100 }}</span>
      <p id="comment">{{ donation.comment }}</p>
    </div>
  </article>
</template>

<script>
export default {
  props: ['donation']
}
</script>

<style scoped>
article {
  max-width: 40%;
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