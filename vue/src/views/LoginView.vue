<template>
  <div id="login" class="content container">
    <div>
      <form v-on:submit.prevent="login">
        <!--TODO: This is hard to see in dark mode-->
        <h1>Please Sign In</h1>
        <div role="alert" v-if="invalidCredentials">
          Invalid username and password!
        </div>
        <div role="alert" v-if="this.$route.query.registration">
          Thank you for registering, please sign in.
        </div>
        <div class="field mt-6">
          <label class="label">Username</label>
          <div class="control">
            <input type="text" class="input" v-model="user.username" required autofocus />
          </div>
        </div>
        <div class="field">
          <label class="label">Password</label>
          <div class="control">
            <input type="password" class="input" v-model="user.password" required />
          </div>
        </div>
        <div class="sign-in-button control">
          <button class="button is-link" type="submit">Sign in</button>
        </div>
        <br />
        <p>
          <router-link v-bind:to="{ name: 'register' }">Need an account? Sign up.</router-link>
        </p>
      </form>
    </div>
  </div>
</template>

<script>
import authService from "../services/AuthService";

export default {
  components: {},
  data() {
    return {
      user: {
        username: "",
        password: ""
      },
      invalidCredentials: false
    };
  },
  methods: {
    login() {
      authService
        .login(this.user)
        .then(response => {
          if (response.status == 200) {
            this.$store.commit("SET_AUTH_TOKEN", response.data.token);
            this.$store.commit("SET_USER", response.data.user);
            this.$router.push("/");
          }
        })
        .catch(error => {
          const response = error.response;
          if (response.status === 401) {
            this.invalidCredentials = true;
          }
        });
    }
  }
};
</script>

<style scoped>  
.content {
  max-width: var(--login-form-width);
}

.sign-in-button {
  margin-top: 24px;
}
</style>