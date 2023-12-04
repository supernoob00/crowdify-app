<template>
  <nav class="navbar is-light" role="navigation" aria-label="main navigation">
    <div class="navbar-brand">
      <div class="navbar-item">
        <img src="@/assets/icon1.png">
      </div>
    </div>
    <div class="navbar-start">
      <router-link class="navbar-item" v-bind:to="{ name: 'home' }">Home</router-link>
      <!-- Uncomment section below when presenting/releasing to production for better design -->
      <!-- </div> -->
      <!-- <div class="navbar-end"> -->
      <router-link class="navbar-item" to="" v-if="$store.state.token != ''">My Account</router-link>
      <div class="buttons">
        <div class="navbar-item">
          <router-link class="button is-primary" v-bind:to="{ name: 'register' }" v-if="$store.state.token === ''">Sign
            up</router-link>
          <router-link class="button is-light" v-bind:to="{ name: 'login' }"
            v-if="$store.state.token === ''">Login</router-link>
          <router-link class="button is-danger" v-bind:to="{ name: 'logout' }"
            v-if="$store.state.token != ''">Logout</router-link>
        </div>
      </div>
    </div>
  </nav>
</template>
<script>
</script>
<style scoped></style>