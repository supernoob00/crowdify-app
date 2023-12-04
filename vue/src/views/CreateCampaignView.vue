<template>
  <div class="content">
    <h1>Create Campaign</h1>
    <form @submit.prevent="">
      <div class="field">
        <label class="label">Campaign Name</label>
        <div class="control">
          <input type="text" class="input" placeholder="e.g Solving World Hunger!" v-model="newCampaign.name">
        </div>
      </div>
      <div class="field">
        <label class="label">Description</label>
        <div class="control">
          <textarea class="textarea" placeholder="e.g Super easy way to save the world!"
            v-model="newCampaign.description"></textarea>
        </div>
      </div>
      <div class="field">
        <label class="label">Funding Goal ($)</label>
        <div class="control">
          <input type="Number" class="input" placeholder="$1 Minimum" v-model="newCampaign.fundingGoal">
        </div>
      </div>
      <div class="field">
        <label class="label">Start Date</label>
        <div class="control">
          <input type="date" class="input" placeholder="Start Date" v-model="newCampaign.startDate">
        </div>
      </div>
      <div class="field">
        <label class="label">End Date</label>
        <div class="control">
          <input type="date" class="input" placeholder="End Date" v-model="newCampaign.endDate">
        </div>
      </div>
      <div class="field">
        <div class="control">
          <label class="checkbox">
            <input type="checkbox" v-model="newCampaign.public">
            Make Public
          </label>
        </div>
      </div>
      <div class="field is-grouped">
        <div class="control">
          <button class="button is-link" @click="saveNewCampaign">Save</button>
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
import campaignService from '../services/CampaignService';
export default {
  data() {
    return {
      isLoading: true,
      newCampaign: {
        name: 'test',
        description: 'test',
        public: false,
        endDate: '2023-12-10',
        startDate: '2023-12-07',
        fundingGoal: 2
      }
    }
  },
  computed:{
    currentUser() {
      return this.$store.state.user;
    }
  },
  methods: {
    async saveNewCampaign() {
      if (!this.validateAddForm()) {
        return;
      }
      try {
        this.newCampaign.endDate = `${this.newCampaign.endDate} 00:00:00`;
        this.newCampaign.startDate = `${this.newCampaign.startDate} 00:00:00`;
        this.newCampaign.fundingGoal *= 100;
        this.newCampaign.creatorId = this.currentUser.id;
        const response = await campaignService.addCampaign(this.newCampaign);
        if (response.status === 201) {
          this.$store.commit('SET_NOTIFICATION', { message: 'Created Campaign!', type: 'success' })
          this.isLoading = true;
          this.resetAddForm();
          this.$router.push({ name: 'home' })
        }
      } catch (error) {
        campaignService.handleErrorResponse(this.$store, error, 'creating', 'campaign');
      }
    },
    validateAddForm() {
      let msg = '';
      if (this.newCampaign.name.length === 0) {
        msg += 'The new campaign must have a name. ';
      }
      if (this.newCampaign.fundingGoal < 1 || this.newCampaign.fundingGoal == null) {
        msg += 'The new campaign must have a fundingGoal of at least $1. ';
      }
      if (!this.newCampaign.endDate) {
        msg += 'The new campaign must have an End Date. '
      }
      if (msg.length > 0) {
        this.$store.commit('SET_NOTIFICATION', msg);
        return false;
      }
      return true;
    },
    resetAddForm() {
      this.newCampaign = {
        name: '',
        description: '',
        public: false,
      }
    },
  }

}
</script>

<style scoped>
.content {
  max-width: 500px;
}
</style>