<template>
  <div>
    {{ spendRequest.name }}
  </div>
</template>

<script setup>
import { defineProps } from 'vue';
const props = defineProps(['spendRequest']);

</script>

<style scoped></style>