<template>
  <div>
    <div class="form-section">
      <button class="btn btn-submit" v-if="!showAddCampaign" v-on:click="showAddCampaign = true">Add
        campaign</button>
      <form v-if="showAddCampaign" @submit.prevent="saveNewCampaign">
        <fieldset>
          <label for="campaign-title">campaign Title: </label>
          <input type="text" id="campaign-title" name="campaign-title" class="form-control" v-model="newCampaign.title" />
        </fieldset>
        <fieldset>
          <label for="description">Description: </label>
          <input type="text" id="description" name="description" class="form-control" v-model="newCampaign.description" />
        </fieldset>
        <fieldset>
          <label for="funding-goal">Funding Goal ($): </label>
          <input type="number" id="funding-goal" name="funding-goal" class="form-control"
            v-model="newCampaign.fundingGoal" />
        </fieldset>
        <fieldset>
          <label for="end-date">End Date: </label>
          <input type="date" id="end-date" name="end-date" class="form-control" v-model="newCampaign.endDate" />
        </fieldset>
        <fieldset>
          <label for="public">Make Public? </label>
          <input type="checkbox" id="public" name="public" class="form-control" v-model="newCampaign.public" />
        </fieldset>
        <button class="btn btn-submit">Save</button>
        <button class="btn btn-cancel" @click="resetAddForm">Cancel</button>
      </form>
    </div>
  </div>
</template>

<script>
import campaignService from '../services/CampaignService';

export default {
  data() {
    return {
      showAddCampaign: false,
      newCampaign: {}
    }
  },
  methods: {
    async saveNewCampaign() {
      if (!this.validateAddForm) {
        return;
      }
      try {
        const response = await campaignService.addCampaign(this.newCampaign);
        if (response.status === 201) {
          this.$store.commit('SET_NOTIFICATION', { message: 'Created Campaign!', type: 'success' })
          this.isLoading = true;
          this.resetAddForm();
        }
      } catch (error) {
        campaignService.handleErrorResponse(this.$store, error, 'adding', 'campaign');
      }
    },

    validateAddForm() {
      let msg = '';
      if (this.newCampaign.name.length === 0) {
        msg += 'The new campaign must have a title. ';
      }
      if (this.newCampaign.fundingGoal < 1) {
        msg += 'The new campaign must have a fundingGoal of at least $1. ';
      }
      if (msg.length > 0) {
        this.$store.commit('SET_NOTIFICATION', msg);
        return false;
      }
      return true;
    },
    resetAddForm() {
      this.showAddCampaign = false;
      this.newCampaign = {}
    },
    handleErrorResponse(error, verb) {
      if (error.response) {
        this.$store.commit('SET_NOTIFICATION',
          "Error " + verb + " campaign. Response received was '" + error.response.statusText + "'.");
      } else if (error.request) {
        this.$store.commit('SET_NOTIFICATION', "Error " + verb + " campaign. Server could not be reached.");
      } else {
        this.$store.commit('SET_NOTIFICATION', "Error " + verb + " campaign. Request could not be created.");
      }
    }
  }
}
</script>

<style scoped>
.form-section {
  margin: 15px 0;
}

.form-control {
  margin-bottom: 10px;
}

.btn {
  margin-bottom: 35px;
}

.loading {
  flex: 3;
}
</style>