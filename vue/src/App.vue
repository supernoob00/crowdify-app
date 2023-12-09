<template>
  <div>
    <nav-bar></nav-bar>
    <notification-text class="header"></notification-text>
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
  --campaign-card-background: lightgray;
  --font-color: #000;
  --link-color: #1C75B9;
  --link-white-color: #fff;
  --bg-color: white;
}

[theme="custom-dark"] {
  --font-color: #e9e9e9;
  --link-color: #0a86da;
  --link-white-color: #bdbab8;
  --bg-color: #333;
  --navbar-background: darkgrey;
  --campaign-card-background: black;
  --user-is-manager-public: teal;
  --user-is-manager-private: rgba(12, 88, 95, 0.374);
}

html,
body {
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

.content h1,
.content h2,
.content h3,
.content h4,
.content h5,
.content h6 {
  margin-top: 0.5em;
  color: var(--font-color)
}
</style>
