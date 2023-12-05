<template>
  <form @submit.prevent="">
    <div class="field">
      <label class="label">Campaign Name</label>
      <div class="control">
        <input type="text" class="input" placeholder="e.g Solving World Hunger!" v-model="editCampaign.name">
      </div>
    </div>
    <div class="field">
      <label class="label">Description</label>
      <div class="control">
        <textarea class="textarea" placeholder="e.g Super easy way to save the world!"
          v-model="editCampaign.description"></textarea>
      </div>
    </div>
    <div class="field">
      <label class="label">Funding Goal ($)</label>
      <div class="control">
        <input type="Number" class="input" placeholder="$1 Minimum" v-model="editCampaign.fundingGoal">
      </div>
    </div>
    <div class="field">
      <label class="label">Start Date</label>
      <div class="control">
        <input type="date" class="input" :min="minStartDate" placeholder="Start Date" v-model="editCampaign.startDate">
      </div>
    </div>
    <div class="field">
      <label class="label">End Date</label>
      <div class="control">
        <input type="date" class="input" :min="minEndDate" placeholder="End Date" v-model="editCampaign.endDate">
      </div>
    </div>
    <div class="field">
      <div class="control">
        <label class="checkbox">
          <input type="checkbox" v-model="editCampaign.public">
          Make Public
        </label>
      </div>
    </div>
    <div class="field is-grouped">
      <div class="control">
        <button class="button is-link" @click="submitForm">Save</button>
      </div>
      <div class="control">
        <button class="button is-light" @click="resetAddForm">Reset Form</button>
      </div>
      <div class="control">
        <button class="button is-danger" @click="$router.push({ name: 'home' })">Cancel</button>
      </div>
    </div>
  </form>
</template>

<script>
import campaignService from '../services/CampaignService';
export default {
  props: ['campaign'],
  data() {
    return {
      isLoading: true,
      editCampaign: { ...this.campaign },
      minStartDate: new Date().toJSON().slice(0, 10),
      minEndDate: new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate() + 1).toJSON().slice(0, 10)
    }
  },
  computed: {
    currentUser() {
      return this.$store.state.user;
    }
  },
  methods: {
    async submitForm() {
      if (!this.validateAddForm()) {
        return;
      }
      const rawUserInput = { ...this.editCampaign };
      this.editCampaign.endDate = `${this.editCampaign.endDate} 00:00:00`;
      this.editCampaign.startDate = `${this.editCampaign.startDate} 00:00:00`;
      this.editCampaign.fundingGoal *= 100;
      if (this.editCampaign.id === -1) {
        try {
          // need this here since creation passes unique DTO
          this.editCampaign.creatorId = this.currentUser.id;
          const response = await campaignService.addCampaign(this.editCampaign);
          if (response.status === 201) {
            const createdCampaign = response.data;
            this.$store.commit('SET_NOTIFICATION', { message: 'Created Campaign!', type: 'success' })
            this.isLoading = true;
            this.resetAddForm();
            this.$router.push({ name: 'CampaignView', params: { id: createdCampaign.id } })
          }
        } catch (error) {
          this.editCampaign = rawUserInput;
          campaignService.handleErrorResponse(this.$store, error, 'creating', 'campaign');
        }
      } else {
        try {
          console.log(this.editCampaign)
          const response = await campaignService.updateCampaign(this.editCampaign);
          if (response.status === 201) {
            this.$store.commit('SET_NOTIFICATION', { message: 'Updated Campaign!', type: 'success' })
            this.isLoading = true;
            this.resetAddForm();
            this.$router.push({ name: 'CampaignView', params: { id: this.editCampaign.id } })
          }
        } catch (error) {
          this.editCampaign = rawUserInput;
          campaignService.handleErrorResponse(this.$store, error, 'updating', 'campaign');
        }
      }
    },
    validateAddForm() {
      let msg = '';
      if (this.editCampaign.name.length === 0) {
        msg += 'The campaign must have a name. ';
      }
      if (this.editCampaign.fundingGoal < 1 || this.editCampaign.fundingGoal == null) {
        msg += 'The campaign must have a fundingGoal of at least $1. ';
      }
      if (!this.editCampaign.endDate) {
        msg += 'The campaign must have an End Date. '
      }
      if (msg.length > 0) {
        this.$store.commit('SET_NOTIFICATION', msg);
        return false;
      }
      return true;
    },
    resetAddForm() {
      this.editCampaign = {
        name: '',
        description: '',
        fundingGoal: null,
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