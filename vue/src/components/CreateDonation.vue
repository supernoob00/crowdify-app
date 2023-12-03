<template>
  <div class="form-section">
    <form @submit.prevent="">
      <div class="field">
        <label class="label">comment</label>
        <div class="control">
          <textarea class="textarea" placeholder="e.g Excited to see more!" v-model="newDonation.comment"></textarea>
        </div>
      </div>
      <div class="field">
        <label class="label">Amount ($)</label>
        <div class="control">
          <input type="Number" class="input" placeholder="$1 Minimum" v-model="newDonation.amount">
        </div>
      </div>
      <div class="field is-grouped">
        <div class="control">
          <button class="button is-link" @click="saveNewDonation">Save</button>
        </div>
        <div class="control">
          <button class="button is-light" @click="resetAddForm">Reset Form</button>
        </div>
        <div class="control">
          <button class="button is-danger" @click="$router.push({ name: 'home' })">Cancel</button>
        </div>
      </div>
    </form>
  </div>
</template>

<script>
import campaignService from '@/services/CampaignService';
export default {
  data() {
    return {
      newDonation: {
        donor: {},
        comment: ''
      }
    }
  },
  computed: {
    campaignId() {
      return parseInt(this.$route.params.id);
    }
  },
  methods: {
    async saveNewDonation() {
      if (!this.validateAddForm()) {
        return;
      }
      try {
        const response = await campaignService.createDonation(this.NewDonation);
        if (response.status === 201) {
          this.$store.commit('SET_NOTIFICATION', { message: 'Created Campaign!', type: 'success' })
          this.isLoading = true;
          this.resetAddForm();
          this.$router.push({ name: 'CampaignView', params: { id: this.campaignId } })
        }
      } catch (error) {
        campaignService.handleErrorResponse(this.$store, error, 'adding', 'campaign');
      }
    },
    validateAddForm() {
      let msg = '';
      if (this.newDonation.amount < 1 || this.newDonation.amount == null) {
        msg += 'A donation must have an amount of at least $1. ';
      }
      if (msg.length > 0) {
        this.$store.commit('SET_NOTIFICATION', msg);
        return false;
      }
      return true;
    },
    resetAddForm() {
      this.newDonation = {}
    },
  }
}
</script>

<style></style>