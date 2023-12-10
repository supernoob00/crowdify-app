<template>
  <div>
    <loading-screen v-if="isLoading"></loading-screen>
    <div v-else class="content">
      <div class="header">
        <div>
          <h1>Spend Request for
            <span>
              <router-link class="campaign-name" :to="{ name: 'CampaignView', params: { id: this.campaignId } }">
                {{ campaign.name }}
              </router-link>
            </span>
          </h1>
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
        </div>
      </div>
      <hr>
      <p>{{ spendRequest.description }}</p>
      <hr>
      <h5>votes</h5>
      <p>{{ votes.length }} out of {{ donorList.size }} votes cast</p>
      <p>{{ approvedVotes.length }} approved</p>
      <p>{{ disapprovedVotes.length }} disapproved</p>
      <p>{{ approvalPercent }}% approved</p>
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
    }
  },
  methods: {
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
    async getVotes() {
      try {
        const response = await campaignService.getVotesBySpendRequestId(this.spendRequest);
        if (response.status === 200) {
          this.votes = response.data;
        }
      } catch (error) {
        campaignService.handleErrorResponse(this.$store, error, 'getting', 'votes');
      }
    }
  },
  async created() {
    await this.getCampaign();
    await this.getSpendRequest();
    await this.getVotes();
    this.isLoading = false;
  }
}

</script>

<style scoped>
.content {
  max-width: 75%;
  margin: 10px;
}

.header {
  display: flex;
  justify-content: space-between;
}

.header a {
  margin-top: 1em;
}

.header .buttons .fa-plus {
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