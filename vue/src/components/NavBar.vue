<template>
  <nav class="navbar" role="navigation" aria-label="main navigation">
    <div class="navbar-brand">
      <div  class="navbar-item">
        <img src="@/assets/icon1.png">
      </div>
    </div>
    <router-link class="navbar-item" v-bind:to="{ name: 'home' }">Home</router-link>
    <router-link class="navbar-item" v-bind:to="{ name: 'logout' }" v-if="$store.state.token != ''">Logout</router-link>
    <div class="navbar-end">
      <div class="buttons">
        <div class="navbar-item">
          <router-link class="button is-primary" v-bind:to="{ name: 'register' }" v-if="$store.state.token === ''">Sign
            up</router-link>
          <router-link class="button is-light" v-bind:to="{ name: 'login' }"
            v-if="$store.state.token === ''">Login</router-link>
        </div>
      </div>
    </div>
  </nav>
</template>
<script>
</script>
<style scoped></style>