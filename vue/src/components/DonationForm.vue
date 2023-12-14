<template>
  <div>
    <loading-screen v-if="isLoading"></loading-screen>
    <form v-else @submit.prevent="submitForm">
      <div class="field">
        <label class="label">Comment</label>
        <div class="control">
          <textarea class="textarea" placeholder="Optional" v-model="editDonation.comment"></textarea>
        </div>
      </div>
      <div class="field">
        <label class="label">Amount ($)</label>
        <div class="control">
          <input type="Number" class="input" placeholder="$1 Minimum" v-model="editDonation.amount">
        </div>
      </div>
      <div v-if="$store.state.user.username" class="field">
        <div class="control">
          <label class="checkbox">
            <input type="checkbox" v-model="editDonation.anonymous">
            Donate Anonymously?
          </label>
        </div>
      </div>
      <div class="field is-grouped">
        <div class="control">
          <button class="button is-link" type="submit">Save</button>
        </div>
        <div class="control">
          <button class="button is-light" @click.prevent="resetAddForm">Reset Form</button>
        </div>
        <div class="control">
          <button class="button is-danger"
            @click.prevent="$router.push({ name: 'CampaignView', params: { id: editDonation.campaignId } })">Cancel</button>
        </div>
      </div>
    </form>
  </div>
</template>

<script>
import CampaignService from '../services/CampaignService';
import LoadingScreen from './LoadingScreen.vue';
export default {
  components: {
    LoadingScreen
  },
  props: ['donation'],
  data() {
    return {
      editDonation: { ...this.donation },
      isLoading: false
    }
  },
  computed: {
    currentUser() {
      return this.$store.state.user;
    },
    newDonationDto() {
      const dto = {}
      dto.campaignId = this.editDonation.campaignId;
      dto.donorId = this.currentUser.id;
      dto.anonymous = this.editDonation.anonymous;
      if (dto.donorId === undefined) {
        dto.anonymous = true;
      }
      //TODO: add anonymous field to create donation form.
      dto.comment = this.editDonation.comment.trim();
      dto.amount = this.editDonation.amount * 100;
      return dto;
    }
  },
  methods: {
    async submitForm() {
      if (!this.validateAddForm()) {
        return;
      }
      this.isLoading = true;
      try {
        const response = await CampaignService.createDonation(this.newDonationDto);
        if (response.status === 201) {
          this.$store.commit('SET_NOTIFICATION', { message: 'Created Donation!', type: 'success' })
          this.resetAddForm();
          this.$router.push({ name: 'CampaignView', params: { id: this.editDonation.campaignId } })
        }
      } catch (error) {
        CampaignService.handleErrorResponse(this.$store, error, 'creating', 'donation');
      } finally {
        this.isLoading = false;
      }
    },
    validateAddForm() {
      let msg = '';
      if (this.editDonation.amount < 1 || this.editDonation.amount == null) {
        msg += 'A donation must have an amount of at least $1. ';
      }
      if (msg.length > 0) {
        this.$store.commit('SET_NOTIFICATION', msg);
        return false;
      }
      return true;
    },
    resetAddForm() {
      this.editDonation = {
        comment: '',
        amount: null,
      }
    },
  },
}
</script>

<style scoped></style>