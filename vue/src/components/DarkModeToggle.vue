<template>
  <a class="toggle" @click="toggleDark()">
    <i v-if="isDark" class="fa-regular fa-sun"></i>
    <i v-else class="fa-regular fa-moon"></i>
  </a>
</template>

<script setup>
import { useDark, useToggle } from '@vueuse/core';
import { useStore } from 'vuex';
const store = useStore();
const isDark = useDark(store.state.darkModeSettings);
const toggleDark = useToggle(isDark);
</script> 

<style scoped>
.toggle,
.toggle:hover,
.toggle:clicked {
  color: black;
}
</style>