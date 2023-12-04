<template>
  <div id="register" class="content">
    <form v-on:submit.prevent="register">
      <h1>Create Account</h1>
      <div role="alert" v-if="registrationErrors">
        {{ registrationErrorMsg }}
      </div>
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
        <button class="button is-link" type="submit">Create Account</button>
      </div>
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
      if (this.user.password != this.user.confirmPassword) {
        this.registrationErrors = true;
        this.registrationErrorMsg = 'Password & Confirm Password do not match.';
      } else {
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
            this.registrationErrors = true;
            if (response.status === 400) {
              this.registrationErrorMsg = 'Bad Request: Validation Errors';
            }
          });
      }
    },
    clearErrors() {
      this.registrationErrors = false;
      this.registrationErrorMsg = 'There were problems registering this user.';
    },
  },
};
</script>

<style scoped>
input {
  max-width: 150px;
}
</style>
