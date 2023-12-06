<template>
  <div class="toggle">
    <input @change="switchTheme" :v-model="isLight" type="checkbox" id="toggle-checkbox">
    <label for="toggle-checkbox" class="toggle-label">
      <i v-if="isLight" class="fa-regular fa-moon"></i>
      <i v-else class="fa-regular fa-sun"></i>
    </label>
  </div>
</template>

<script lang="ts">
export default {
  data() {
    return {
      isLight: localStorage.getItem('theme') === 'light'
    };
  },
  mounted() {
    const currentTheme = localStorage.getItem('theme') ? localStorage.getItem('theme') : null;
    currentTheme && document.documentElement.setAttribute('data-theme', currentTheme);

    if (document.documentElement.getAttribute("data-theme") == "dark") {
      this.isLight = false;
    }
  },

  methods: {
    switchTheme(e: Event) {
      if ((<HTMLInputElement>e.target).checked) {
        localStorage.setItem('theme', 'dark');
        // document.documentElement.setAttribute('data-theme', 'dark');
        this.isLight = false;
      } else {
        localStorage.setItem('theme', 'light');
        // document.documentElement.setAttribute('data-theme', 'light');
        this.isLight = true;
      }
    },
  },
};
</script> 

<style scoped>
.toggle input[type="checkbox"] {
  display: none;
}
</style>