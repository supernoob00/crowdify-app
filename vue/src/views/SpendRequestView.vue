<template>
  <div>
    <loading-screen v-if="isLoading"></loading-screen>
    <div v-else class="content">
      <div class="sr-header">
        <div>
          <h1>{{ spendRequest.name }}</h1>
          <h4>Spend Request for
            <span>
              <router-link class="campaign-name" :to="{ name: 'CampaignView', params: { id: this.campaignId } }">
                {{ campaign.name }}
              </router-link>
            </span>
          </h4>
          <span>Created by </span>
          <span class="campaign-creator">{{ campaign.creator.username }}</span>
          <p class="request-amount">{{ amountDisplay }}</p>
        </div>
        <div class="buttons">
          <router-link v-if="isManager" class="button is-link" :to="{
            name: 'EditSpendRequestView',
            params: {
              campaignId: spendRequest.campaignId,
              spendRequestId: spendRequest.id
            }
          }">
            <i class="fa-solid fa-pen-to-square"></i></router-link>
          <button class="button is-danger" v-if="isManager" @click="deleteSpendRequest">
            <i class="fa-solid fa-trash"></i></button>
        </div>
      </div>
      <hr>
      <p>{{ spendRequest.description }}</p>
      <hr>
      <div class="sr-header">
        <h5>votes</h5>
        <div v-if="canVote" class="buttons">
          <button class="button is-link" @click="showModal = true">{{ voteText }}</button>
        </div>
      </div>
      <p>{{ votes.length }} out of {{ donorList.size }} votes cast</p>
      <p>{{ approvedVotes.length }} approved</p>
      <p>{{ disapprovedVotes.length }} rejected</p>
      <p>{{ approvalPercent }}% approved</p>

      <div class="modal" :class="{ 'is-active': showModal }">
        <div class="modal-background" @click="closeForm"></div>
        <div class="modal-card">
          <header class="modal-card-head">
            <p class="modal-card-title">Vote for "{{ spendRequest.name }}"</p>
            <button class="delete" aria-label="close" @click="closeForm"></button>
          </header>
          <section class="modal-card-body">
            <form @submit.prevent="submitForm">
              <div class="field">
                <div class="buttons has-addons is-centered">
                  <button :class="approvedButtonClass" @click.prevent="editVote.approved = true">Approve</button>
                  <button :class="abstainButtonClass" @click.prevent="editVote.approved = null"
                    v-if="hasVoted">Abstain</button>
                  <button :class="rejectedButtonClass" @click.prevent="editVote.approved = false">Reject</button>
                </div>
              </div>
              <div class="control">
                <div class="buttons is-centered">
                  <button class="button is-link is-centered" type="submit">Submit</button>
                </div>
              </div>
            </form>
          </section>
        </div>
      </div>
      <!-- See below only for managers (?) -->
      <!-- <div v-for="(vote, index) in votes" :key="index">{{ vote }}</div> -->
    </div>
  </div>
</template>

<script>
import campaignService from '../services/CampaignService';
import LoadingScreen from '../components/LoadingScreen.vue';
export default {
  components: {
    LoadingScreen,
  },
  data() {
    return {
      campaign: {},
      spendRequest: {},
      votes: {},
      isLoading: true,
      editVote: { id: -1 },
      showModal: false,
    }
  },
  computed: {
    spendRequestId() {
      return parseInt(this.$route.params.spendRequestId);
    },
    campaignId() {
      return parseInt(this.$route.params.campaignId);
    },
    amountDisplay() {
      return `$${this.spendRequest.amount / 100}`
    },
    donorList() {
      const uniqueDonors = new Set();
      this.campaign.donations.forEach(d => {
        if (d.donor != null &&
          !this.campaign.managers.filter(m => m.username === d.donor.username).length > 0) {
          uniqueDonors.add(d.donor.username);
        }
      });
      return uniqueDonors;
    },
    approvedVotes() {
      return this.votes.filter(v => v.approved);
    },
    disapprovedVotes() {
      return this.votes.filter(v => !v.approved);
    },
    approvalPercent() {
      if ((this.approvedVotes.length + this.disapprovedVotes.length) === 0) {
        return 0;
      }
      return this.approvedVotes.length / (this.disapprovedVotes.length + this.approvedVotes.length) * 100
    },
    approvedButtonClass() {
      return { button: true, 'is-success': this.editVote.approved };
    },
    rejectedButtonClass() {
      return { button: true, 'is-danger': !this.editVote.approved && this.editVote.approved != null };
    },
    abstainButtonClass() {
      return { button: true, 'is-info': this.editVote.approved === null }
    },
    hasVoted() {
      return this.votes.filter(v => v.user.id === this.$store.state.user.id).length > 0;
    },
    isManager() {
      return this.campaign.managers.filter(m => m.username === this.$store.state.user.username).length > 0;
    },
    canVote() {
      return !this.isManager;
    },
    voteText() {
      return this.hasVoted ? 'Update Vote' : 'Vote';
    },
    newVoteDto() {
      return {
        userId: this.$store.state.user.id,
        requestId: this.spendRequest.id,
        approved: this.editVote.approved
      }
    },
  },
  async created() {
    await this.getCampaign();
    await this.getSpendRequest();
    await this.getVotes();
    this.isLoading = false;
  },
  methods: {
    async getCampaign() {
      try {
        const response = await campaignService.getCampaign(this.campaignId);
        if (response.status === 200) {
          this.campaign = response.data;
        }
      } catch (error) {
        campaignService.handleErrorResponse(this.$store, error, 'getting', 'campaign');
      }
    },
    async getSpendRequest() {
      try {
        const response = await campaignService.getSpendRequestById(this.campaignId, this.spendRequestId);
        if (response.status === 200) {
          this.spendRequest = response.data;
        }
      } catch (error) {
        campaignService.handleErrorResponse(this.$store, error, 'getting', 'spend request')
      }
    },
    async getVotes() {
      try {
        const response = await campaignService.getVotesBySpendRequestId(this.campaignId, this.spendRequestId);
        if (response.status === 200) {
          this.votes = response.data;
        }
      } catch (error) {
        campaignService.handleErrorResponse(this.$store, error, 'getting', 'votes');
      }
    },
    async deleteSpendRequest() {
      if (!confirm("Are you sure you want to delete this spend request?")) {
        return;
      }
      try {
        const response = await campaignService.deleteSpendRequestById(this.campaignId, this.spendRequestId);
        if (response.status === 204) {
          this.$store.commit("SET_NOTIFICATION", { message: 'Deleted SpendRequest!', type: 'success' });
          this.$router.push({ name: 'CampaignView', params: { id: this.campaignId } });
        }
      } catch (error) {
        campaignService.handleErrorResponse(this.$store, error, 'deleting', 'spendRequest');
      }
    },
    closeForm() {
      this.showModal = false;
      this.editVote = {};
    },
    async submitForm() {
      if (this.hasVoted && this.editVote.approved === null) {
        try {
          const response = await campaignService.deleteVote(this.campaignId, this.spendRequestId);
          if (response.status === 204) {
            this.$store.commit('SET_NOTIFICATION', { message: 'Deleted Vote!', type: 'success' })
          }
        } catch (error) {
          campaignService.handleErrorResponse(this.$store, error, 'deleting', 'vote');
        }
      } else if (this.hasVoted) {
        try {
          const response = await campaignService.updateVote(this.campaignId, this.spendRequestId, this.newVoteDto);
          if (response.status === 200) {
            this.$store.commit('SET_NOTIFICATION', { message: 'Updated Vote!', type: 'success' })
          }
        } catch (error) {
          campaignService.handleErrorResponse(this.$store, error, 'updating', 'vote');
        }
      } else {
        try {
          const response = await campaignService.createVote(this.campaignId, this.spendRequestId, this.newVoteDto);
          if (response.status === 201) {
            this.$store.commit('SET_NOTIFICATION', { message: 'Voted!', type: 'success' })
          }
        } catch (error) {
          campaignService.handleErrorResponse(this.$store, error, 'adding', 'vote');
        }
      }
      this.closeForm();
      this.getVotes();
    }
  }
}

</script>

<style scoped>
.content {
  max-width: 75%;
  margin: 10px;
}

.sr-header {
  display: flex;
  justify-content: space-between;
}

.sr-header .buttons>* {
  margin-top: 1em;
}

.campaign-name {
  color: var(--font-color);
  text-decoration: underline;
}

.campaign-creator {
  font-weight: 600;
}
</style>