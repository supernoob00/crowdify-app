<template>
  <div>
    <nav-bar></nav-bar>
    <notification-text></notification-text>
    <div id="capstone-app" class="container">
      <router-view></router-view>
    </div>
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
  --navbar-background: #fb8500;
  --user-is-manager-public: green;
  --user-is-manager-private: teal;
}

html,
body {
  margin: 0;
  padding: 0;
  height: 100%;
  background-color: var(--app-background);
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
