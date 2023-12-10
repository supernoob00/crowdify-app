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
          <router-link v-if="canVote" class="button is-link" :to="{
            name: 'EditSpendRequestView',
            params: {
              campaignId: spendRequest.campaignId,
              spendRequestId: spendRequest.id
            }
          }">
            <i class="fa-solid fa-pen-to-square"></i></router-link>
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
                <div class="field is-grouped">
                  <div class="control">
                    <button :class="approvedButtonClass" @click.prevent="editVote.approved = true">Approve</button>
                  </div>
                  <div class="control">
                    <button :class="rejectedButtonClass" @click.prevent="editVote.approved = false">Reject</button>
                  </div>
                </div>
              </div>
              <div class="field is-grouped">
                <div class="control">
                  <button class="button is-link" type="submit">Submit</button>
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
import { displayMoney } from '../services/Utilities';
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
    isManager() {
      return this.campaign.managers.filter(m => m.username === this.$store.state.user.username).length > 0;
    },
    amountDisplay() {
      return displayMoney(this.spendRequest.amount);
    },
    donorList() {
      const uniqueDonors = new Set();
      this.campaign.donations.forEach(d => {
        if (d.donor != null) {
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
      return this.approvedVotes.length / (this.disapprovedVotes.length + this.approvedVotes.length) * 100
    },
    approvedButtonClass() {
      return this.editVote.approved === undefined ? { button: true } : { button: true, 'is-success': this.editVote.approved };
    },
    rejectedButtonClass() {
      return this.editVote.approved === undefined ? { button: true } : { button: true, 'is-danger': !this.editVote.approved };
    },
    hasVoted() {
      return this.votes.filter(v => v.userId === this.$store.state.user.id).length > 0;
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
    console.log(this.votes);
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
        const response = await campaignService.getVotesBySpendRequestId(this.spendRequest);
        if (response.status === 200) {
          this.votes = response.data;
        }
      } catch (error) {
        campaignService.handleErrorResponse(this.$store, error, 'getting', 'votes');
      }
    },
    closeForm() {
      this.showModal = false;
      this.editVote = {};
    },
    async submitForm() {
      if (this.hasVoted) {
        try {
          const response = await campaignService.updateVote(
            this.spendRequest.id, this.spendRequest.campaignId, this.newVoteDto
          );
          if (response.status === 200) {
            this.$store.commit('SET_NOTIFICATION', { message: 'Updated Vote!', type: 'success' })
          }
        } catch (error) {
          campaignService.handleErrorResponse(this.$store, error, 'updating', 'vote');
        }
      } else {
        try {
          const response = await campaignService.createVote(
            this.spendRequest.id, this.spendRequest.campaignId, this.newVoteDto);
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

.sr-header a {
  margin-top: 1em;
}

.sr-header .buttons .fa-plus {
  margin-right: 10px;
}

.campaign-name {
  color: var(--font-color);
  text-decoration: underline;
}

.campaign-creator {
  font-weight: 600;
}
</style>