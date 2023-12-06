<template>
  <main class="content">
    <h1>Donate to {{ campaign.name }}</h1>
    <loading-screen v-if="isLoading"></loading-screen>
    <div v-else class="body">
      <form @submit.prevent="submitForm">
        <div class="field">
          <label class="label">Comment</label>
          <div class="control">
            <textarea class="textarea" placeholder="Optional" v-model="newDonationDto.comment"></textarea>
          </div>
        </div>
        <div class="field">
          <label class="label">Amount ($)</label>
          <div class="control">
            <input type="Number" class="input" placeholder="$1 Minimum" v-model="newDonationDto.amount">
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
              @click="$router.push({ name: 'CampaignView', params: { id: campaignId } })">Cancel</button>
          </div>
        </div>
      </form>
    </div>
  </main>
</template>

<script>
import campaignService from '../services/CampaignService';
import LoadingScreen from '../components/LoadingScreen.vue';
export default {
  components: {
    LoadingScreen
  },
  data() {
    return {
      newDonationDto: {
        comment: 'Test Comment',
        amount: 10
      },
      campaign: {},
      isLoading: true,
    }
  },
  computed: {
    campaignId() {
      return parseInt(this.$route.params.id);
    },
    currentUser() {
      return this.$store.state.user;
    },
  },
  methods: {
    async getCampaign() {
      try {
        const response = await campaignService.getCampaign(this.campaignId);
        if (response.status === 200) {
          this.campaign = response.data;
        }
      } catch (error) {
        campaignService.handleErrorResponse(this.$store, error, 'retrieving', 'campaign');
      } finally {
        this.isLoading = false;
      }
    },
    async submitForm() {
      if (!this.validateAddForm()) {
        return;
      }
      const rawUserInput = { ...this.newDonationDto };
      this.newDonationDto.campaignId = this.campaignId;
      this.newDonationDto.donorId = this.currentUser.id;
      this.newDonationDto.amount *= 100;
      try {
        this.isLoading = true;
        const response = await campaignService.createDonation(this.newDonationDto);
        if (response.status === 201) {
          this.$store.commit('SET_NOTIFICATION', { message: 'Created Donation!', type: 'success' })
          this.resetAddForm();
          this.$router.push({ name: 'CampaignView', params: { id: this.campaignId } })
        }
      } catch (error) {
        this.newDonationDto = rawUserInput;
        campaignService.handleErrorResponse(this.$store, error, 'creating', 'donation');
      } finally {
        this.isLoading = false;
      }
    },
    validateAddForm() {
      let msg = '';
      if (this.newDonationDto.amount < 1 || this.newDonationDto.amount == null) {
        msg += 'A donation must have an amount of at least $1. ';
      }
      if (msg.length > 0) {
        this.$store.commit('SET_NOTIFICATION', msg);
        return false;
      }
      return true;
    },
    resetAddForm() {
      this.newDonationDto = {
        comment: '',
        amount: null,
      }
    },
  },
  created() {
    this.getCampaign();
  }
}
</script>

<style scoped>
.body {
  max-width: 500px;
}
</style>