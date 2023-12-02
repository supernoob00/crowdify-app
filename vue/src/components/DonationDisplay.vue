<template>
  <main>
    <h2>Donations</h2>
    <div class="donation">
      <h3>{{ donation.donor.name }}</h3>
      <h4>Amount: {{ donation.amount }}</h4>
      <p>{{ donation.comment }}</p>
    </div>
  </main>
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
  padding: 1rem;
  margin: 10px;
}

.donation p {
  margin: 20px;
}

.donation h4 {
  font-size: 1em;
}

.rating {
  height: 2rem;
  display: inline-block;
  vertical-align: top;
  margin: 0 0.5rem;
}
</style>