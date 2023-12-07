<template>
  <div>
    <loading-screen v-if="isLoading"></loading-screen>
    <form v-else @submit.prevent="submitForm">
      <div class="field">
        <label class="label">Name</label>
        <div class="control">
          <input type="text" class="input" placeholder="" v-model="editSpendRequest.name">
        </div>
      </div>
      <div class="field">
        <label class="label">Description</label>
        <div class="control">
          <textarea class="textarea" placeholder="e.g Super easy way to save the world!"
            v-model="editSpendRequest.description"></textarea>
        </div>
      </div>
      <div class="field">
        <label class="label">Amount ($)</label>
        <div class="control">
          <input type="Number" class="input" placeholder="$1 Minimum" v-model="editSpendRequest.amount">
        </div>
      </div>
      <div class="field">
        <label class="label">End Date</label>
        <div class="control">
          <input type="date" class="input" :min="minEndDate" placeholder="End Date" v-model="editSpendRequest.endDate">
        </div>
      </div>
      <div class="field is-grouped">
        <div class="control">
          <button class="button is-link" type="submit">Save</button>
        </div>
        <div class="control">
          <button class="button is-light" @click="resetAddForm">Reset Form</button>
        </div>
        <div class="control">
          <button class="button is-danger"
            @click="$router.push({ name: 'CampaignView', params: { id: editSpendRequest.campaignId } })">Cancel</button>
        </div>
      </div>
    </form>
  </div>
</template>

<script>
import campaignService from '../services/CampaignService';
import LoadingScreen from './LoadingScreen.vue';
export default {
  components: {
    LoadingScreen
  },
  props: ['spendRequest'],
  data() {
    return {
      isLoading: false,
      editSpendRequest: { ...this.spendRequest },
      minEndDate: new Date(new Date().getFullYear(), new Date().getMonth(), new Date().getDate() + 1).toJSON().slice(0, 10)
    }
  },
  computed: {
    currentUser() {
      return this.$store.state.user;
    },
    newSpendRequestDto() {
      const dto = {};
      dto.name = this.editSpendRequest.name;
      dto.description = this.editSpendRequest.description;
      dto.amount = this.editSpendRequest.amount * 100;
      dto.campaignId = this.editSpendRequest.campaignId;
      dto.endDate = `${this.editSpendRequest.endDate} 00:00:00`;
      dto.public = this.editSpendRequest.public;
      return dto;
    },
    updateSpendRequestDto() {
      const dto = {};
      dto.id = this.editSpendRequest.id;
      dto.name = this.editSpendRequest.name;
      dto.description = this.editSpendRequest.description;
      dto.amount = this.editSpendRequest.amount * 100;
      dto.campaignId = this.editSpendRequest.campaignId;
      dto.endDate = `${this.editSpendRequest.endDate} 00:00:00`;
      dto.public = this.editSpendRequest.public;
      dto.locked = this.editSpendRequest.locked;
      return dto;
    }
  },
  methods: {
    async submitForm() {
      if (!this.validateAddForm()) {
        return;
      }
      this.isLoading = true;
      if (this.editSpendRequest.id === -1) {
        try {
          const response = await campaignService.addCampaign(this.newSpendRequestDto);
          if (response.status === 201) {
            const createdCampaign = response.data;
            this.$store.commit('SET_NOTIFICATION', { message: 'Created Campaign!', type: 'success' })
            this.resetAddForm();
            this.$router.push({ name: 'CampaignView', params: { id: createdCampaign.id } })
          }
        } catch (error) {
          campaignService.handleErrorResponse(this.$store, error, 'creating', 'campaign');
        } finally {
          this.isLoading = false;
        }
      } else {
        try {
          const response = await campaignService.updateCampaign(this.updateSpendRequestDto, this.editSpendRequest.id);
          if (response.status === 200) {
            this.$store.commit('SET_NOTIFICATION', { message: 'Updated Campaign!', type: 'success' });
            this.resetAddForm();
            this.$router.push({ name: 'CampaignView', params: { id: this.editSpendRequest.id } });
          }
        } catch (error) {
          campaignService.handleErrorResponse(this.$store, error, 'updating', 'campaign');
        } finally {
          this.isLoading = false;
        }
      }
    },
    validateAddForm() {
      let msg = '';
      if (this.editSpendRequest.name.length === 0) {
        msg += 'The SpendRequest must have a name. ';
      }
      if (this.editSpendRequest.description.length === 0) {
        msg += 'The SpendRequest must have a description. ';
      }
      if (this.editSpendRequest.amount < 1 || this.editSpendRequest.amount == null) {
        msg += 'The SpendRequest must have a amount of at least $1. ';
      }
      if (!this.editSpendRequest.endDate) {
        msg += 'The SpendRequest must have an End Date. '
      }
      if (msg.length > 0) {
        this.$store.commit('SET_NOTIFICATION', msg);
        return false;
      }
      return true;
    },
    resetAddForm() {
      this.editSpendRequest = {
        name: '',
        description: '',
        fundingGoal: null,
        startDate: this.minStartDate,
        endDate: this.minEndDate,
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