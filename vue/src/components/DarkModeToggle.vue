<template>
  <a class="toggle" @click="toggleDark()">
    <i v-if="isDark" class="fa-regular fa-sun"></i>
    <i v-else class="fa-regular fa-moon"></i>
  </a>
</template>

<script setup>
import { useDark, useToggle } from '@vueuse/core';
const isDark = useDark({
  selector: "body",
  attribute: "theme",
  valueDark: "custom-dark",
  valueLight: "custom-light",
});
const toggleDark = useToggle(isDark);
</script> 

<style scoped>
.toggle,
.toggle:hover,
.toggle:clicked {
  color: black;
}
</style>