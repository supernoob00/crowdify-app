<template>
  <nav class="navbar" :class="buttonClass" role="navigation" aria-label="main navigation">
    <div class="navbar-brand">
      <div class="navbar-item">
        <router-link class="navbar-item" v-bind:to="{ name: 'home' }">
          <img v-if="isDark" src="../assets/logo-white.png">
          <img v-else src="../assets/logo-no-background.png">
        </router-link>
      </div>
    </div>
    <div class="navbar-start">
    </div>
    <div class="navbar-end">
      <dark-mode-toggle class="navbar-item"></dark-mode-toggle>

      <router-link class="navbar-item" :to="{ name: 'MyAccountView', params: { id: $store.state.user.id } }"
        v-if="$store.state.token != ''">My Account</router-link>
      <div v-if="$store.state.user.username" class="navbar-item"> Hi,&nbsp;<span class="has-text-weight-bold">{{ $store.state.user.username }}</span>!</div>
      <div class="buttons">
        <div class="navbar-item">
          <router-link class="button is-primary" v-bind:to="{ name: 'register' }" v-if="$store.state.token === ''">Sign
            up</router-link>
          <router-link class="button" :class="buttonClass" v-bind:to="{ name: 'login' }"
            v-if="$store.state.token === ''">Login</router-link>
          <router-link class="button is-danger" v-bind:to="{ name: 'logout' }"
            v-if="$store.state.token != ''">Logout</router-link>
        </div>
      </div>
    </div>
  </nav>
</template>
<script>
import DarkModeToggle from './DarkModeToggle.vue';
import { useDark } from '@vueuse/core';
export default {
  components: {
    DarkModeToggle
  },
  data() {
    return {
      isDark: useDark(this.$store.state.darkModeSettings)
    }
  },
  computed: {
    buttonClass() {
      if (this.isDark) {
        return { 'is-dark': true }
      }
      return { 'is-light': true }
    }
  }
}
</script>
<style scoped>
.navbar {
  /* background-color: var(--navbar-background); */
  position: sticky;
  left: 0;
  top: 0;
  z-index: 200;
}
</style>