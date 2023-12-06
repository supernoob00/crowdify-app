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
  --navbar-background: #fb8500;
  --user-is-manager-public: pink;
  --user-is-manager-private: lightcoral;
  --font-color: #000;
  --link-color: #1C75B9;
  --link-white-color: #fff;
  --bg-color: white;
}

[data-theme="dark"] {
  --font-color: #bdbab8;
  --link-color: #0a86da;
  --link-white-color: #bdbab8;
  --bg-color: #333;
}

html,
body {
  color: #000;
  color: var(--font-color);
  background: var(--bg-color);
  margin: 0;
  padding: 0;
  height: 100%;
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
