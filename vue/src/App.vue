<template>
  <nav-bar></nav-bar>
  <notification-text class="header"></notification-text>
  <div id="capstone-app">
    <router-view></router-view>
  </div>
</template>

<script>
import NavBar from './components/NavBar.vue'
import NotificationText from './components/NotificationText.vue';
export default {
  components: {
    NavBar,
    NotificationText
  },
};
</script>

<style>
:root {
  --navbar-background: rgb(236, 236, 236);
  --user-is-manager-public: pink;
  --user-is-manager-private: lightcoral;
  --campaign-card-background: lightgray;
  --font-color: #000;
  --link-color: #1C75B9;
  --link-white-color: #fff;
  --bg-color: linear-gradient(120deg, #00d1b2, #FFFFFF);
  --standard-form-width: 600px;
  --login-form-width: 300px;
  --box-color: #F5F5F5;
  --box-hr-color: rgb(215, 215, 215);
}

[theme="custom-dark"] {
  --font-color: #e9e9e9;
  --link-color: #7acaff;
  --link-white-color: #bdbab8;
  --bg-color: #333;
  --bg-color: linear-gradient(120deg, #333, grey);
  --navbar-background: rgb(4, 71, 71);
  --campaign-card-background: black;
  --user-is-manager-public: teal;
  --user-is-manager-private: rgba(12, 88, 95, 0.374);
  --box-color: grey;
  --box-hr-color: #F5F5F5;
}

body,
html {
  color: var(--font-color);
  background: var(--bg-color);
  margin: 0;
  padding: 0;
  height: 100%;
  overflow-y: auto;
}

hr {
  margin: 1rem 0;
}

#capstone-app {
  width: 100%;
  max-width: 1120px;
  margin: 0 auto;
  padding: 20px;
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

.content p,
.content span {
  color: var(--font-color);
}

.tooltip-button::after {
  content: attr(data-title);
  position: absolute;
  background: #444;
  color: #fff;
  left: 0;
  top: 100%;
  font-size: 12px;
  width: fit-content;
  padding: 5px;
  border-radius: 4px;
  transition: 300ms;
  opacity: 0;
  pointer-events: none;
  transform: translateY(15px);
}

.tooltip-button:hover::after {
  opacity: 1;
  transform: translateY(5px);
}

.box {
  background-color: var(--box-color);
}

.box hr {
  background-color: var(--box-hr-color);
}

.label {
  color: var(--font-color);
}
</style>
