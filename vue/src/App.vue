<template>
  <div id="capstone-app" class="container">
    <notification-text></notification-text>
    <nav-bar></nav-bar>
    <router-view></router-view>
  </div>
</template>

<script>
import NavBar from '@/components/NavBar.vue'
import NotificationText from '@/components/NotificationText.vue';
export default {
  components: {
    NavBar,
    NotificationText
  },
};
</script>

<style>
:root {
  --app-background: white;
  --user-is-manager-public: green;
  --user-is-manager-private: teal;
}

#app {
  background-color: var(--app-background)
}

.container {
  width: 100%;
  max-width: 1100px;
  margin: 0 auto;
}

h1 {
  margin-top: 0.5em;
}
</style>
