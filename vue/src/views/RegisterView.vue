<template>
  <div id="register" class="content container">
    <form v-on:submit.prevent="register">
      <h1>Create Account</h1>
      <div class="field">
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
      <div class="field">
        <label class="label">Confirm Password</label>
        <div class="control">
          <input type="password" class="input" v-model="user.confirmPassword" required />
        </div>
      </div>
      <div class="control">
        <button class="sign-in-button button is-link" type="submit">Create Account</button>
      </div>
      <br />
      <p><router-link v-bind:to="{ name: 'login' }">Already have an account? Log in.</router-link></p>
    </form>
  </div>
</template>

<script>
import authService from '../services/AuthService';

export default {
  data() {
    return {
      user: {
        username: '',
        password: '',
        confirmPassword: '',
        role: 'user',
      },
      registrationErrors: false,
      registrationErrorMsg: 'There were problems registering this user.',
    };
  },
  methods: {
    register() {
      if (!this.validateRegisterForm()) {
        return;
      }
      authService
        .register(this.user)
        .then((response) => {
          if (response.status == 201) {
            this.$router.push({
              path: '/login',
              query: { registration: 'success' },
            });
          }
        })
        .catch((error) => {
          const response = error.response;
          if (response.status === 400) {
            this.$store.commit('SET_NOTIFICATION', 'Bad Request: Validation Errors.');
          }
        });
    },
    validateRegisterForm() {
      let msg = '';
      if (this.user.password != this.user.confirmPassword) {
        msg += 'Password and Confirm Password do not match. ';
      }
      if (this.user.password.length < 8) {
        msg += 'Password must be at least 8 characters long. '
      }
      if (msg.length > 0) {
        this.$store.commit('SET_NOTIFICATION', msg);
        return false;
      }
      return true;
    },
  },
};
</script>

<style scoped>
.content {
  max-width: var(--login-form-width)
}

.sign-in-button {
  margin-top: 24px;
}
</style>
