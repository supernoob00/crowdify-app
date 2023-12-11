<template>
  <div v-bind:class="notificationClass" v-show="notification" v-on:click="clearNotification">
    {{ notification?.message }}
  </div>
</template>

<script>
export default {
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
}
</script>

<style scoped>
.status-message {
  display: block;
  border-radius: 5px;
  font-weight: bold;
  font-size: 1rem;
  text-align: center;
  padding: 10px;
  margin-bottom: 10px;
  cursor: pointer;
  color: black;
}

.status-message.success {
  background-color: #90ee90;
}

.status-message.error {
  background-color: #f08080;
}
</style>