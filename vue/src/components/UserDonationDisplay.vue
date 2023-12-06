<template>
  <article class="message">
    <div class="message-body">
      <span class="amount">${{ donation.amount / 100 }}</span>
      <span> to </span>
      <span class="campaign-name">"name"</span>
      <p class="comment">{{ donation.comment }}</p>
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
  max-width: 300px;
}

.message-body {
  padding: 1em;
}

.amount,
.comment {
  color: grey;
}

.comment {
  font-style: italic;
}

.campaign-name {
  font-weight: 600;
}
</style>