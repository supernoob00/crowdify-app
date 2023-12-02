<template>
  <div id="capstone-app" class="container">
    <div v-bind:class="notificationClass" v-show="notification" v-on:click="clearNotification">
      {{ notification?.message }}
    </div>
    <nav-bar></nav-bar>
    <router-view />
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
export default {
  components: {
    NavBar
  },
  computed: {
    notification() {
      return this.$store.state.notification;
    },
    notificationClass() {
      return {
        'status-message': true,
        error: this.notification?.type?.toLowerCase() === 'error',
        success: this.notification?.type?.toLowerCase() === 'success'
      };
    }
  },
  methods: {
    clearNotification() {
      this.$store.commit('CLEAR_NOTIFICATION');
    },
  }
};
</script>

<style>
.container {
  width: 100%;
  max-width: 1100px;
  margin: 0 auto;
}

.status-message {
  display: block;
  border-radius: 5px;
  font-weight: bold;
  font-size: 1rem;
  text-align: center;
  padding: 10px;
  margin-bottom: 10px;
  cursor: pointer;
}

.status-message.success {
  background-color: #90ee90;
}

.status-message.error {
  background-color: #f08080;
}

nav {
  border-bottom: 1px solid black;
}
</style>
