<template>
  <loading-screen v-if="isLoading"></loading-screen>
  <div v-else class="content">
    <div class="header">
      <div>
        <h1 id="campaign-name">{{ campaign.name }}</h1>
      </div>
      <!-- THIS DIV IS FOR MANAGERS -->
      <div v-if="isManager" class="buttons manager-actions">
        <router-link class="button is-link" :to="{ name: 'EditCampaignView', params: { id: campaign.id } }">
          <i class="fa-solid fa-pen-to-square"></i></router-link>
        <button v-if="isCreator" class="button is-danger" @click="deleteCampaign" :disabled="!isLocked"
          :class="{ 'tooltip-button': !isLocked }" data-title="Campaign must be locked to delete it">
          <i class="fa-solid fa-trash"></i></button>
        <button class="button is-link" @click="goToCreateSpendRequestView" :disabled="!isLocked"
          :class="{ 'tooltip-button': !isLocked }" data-title="Campaign must be locked to create a spend request">
          <i class="fa-solid fa-plus"></i>Add Spend Request</button>
      </div>
    </div>

    <div class="campaign-owner-info">
      <div>
        <span>Created by </span>
        <span class="campaign-creator">{{ campaign.creator.username }}</span>
      </div>
      <div v-if="this.campaign.managers.length > 1">
        <!--TODO: This is hideous, change it later to use flexbox-->
        <span>&nbsp|&nbsp</span>
        <span v-if="this.campaign.managers.length > 0">Other owners: </span>
        <span class="campaign-other-owners">{{ nonCreatorManagerNames }}</span>
      </div>
    </div>

    <hr>
    <div id="campaign-description" class="block box">
      <h5>Description</h5>
      <hr width="120px">
      {{ campaign.description }}
    </div>
    <div class="progress-container box">
      <div class="block" id="progress-meter-heading">
        <h3 class="amount-raised">${{ totalDonated }}</h3>
        <span class="goal-text"> raised of ${{ fundingGoal }} Goal</span>
      </div>
      <progress class="progress is-success is-small" :value="totalDonated" :max="fundingGoal"></progress>
      <div class="num-donations">{{ numberOfDonations }} donations</div>
      <!--TODO: broken if not allowed to view-->
      <div v-if="spendRequestsObj.canView">{{ `Funds Remaining: ${totalFunds}` }}</div>
    </div>
    <!--TODO: format these dates-->
    <h5>From {{ viewDates.startDate }} to {{ viewDates.endDate }}</h5>
    <hr>
    <div class="side-info">

      <section class="donations">
        <header class="donation-header">
          <h2 class="block">Donations</h2>
          <button data-title="This campaign is locked for further donations." :disabled="isLocked"
            :class="{ 'tooltip-button': isLocked }" class="donate-button button is-link block"
            @click="goToCreateDonationView">
            Donate
          </button>
        </header>
        <donation-display v-for="donation in donationsSortedByAmount" :key="donation.id" :donation="donation">
        </donation-display>
      </section>

      <section class="spend-requests" v-if="spendRequestsObj.canView">
        <h2 class="block">Spend Requests</h2>
        <p v-if="spendRequestsObj.list.length === 0">There are no spend requests created for this campaign yet.</p>
        <spend-request-display v-for="spendRequest in spendRequestsObj.list" :key="spendRequest.id"
          :spend-request="spendRequest"></spend-request-display>
      </section>
    </div>
  </div>
</template>

<script>
import DonationDisplay from './DonationDisplay.vue';
import SpendRequestDisplay from './SpendRequestDisplay.vue';
import campaignService from '../services/CampaignService';
import LoadingScreen from './LoadingScreen.vue';
import Util from '../services/Util';

export default {
  components: {
    DonationDisplay,
    SpendRequestDisplay,
    LoadingScreen
  },
  props: ['campaign', 'spendRequestsObj'],
  data() {
    return {
      isLoading: false
    }
  },
  computed: {
    isPublic() {
      return this.campaign.public;
    },
    isLocked() {
      return this.campaign.locked;
    },
    totalDonated() {
      const totalDonated = this.campaign.donations.reduce((sum, d) => d.refunded ? 0 : sum + d.amount, 0);
      return Util.formatToMoney(totalDonated);
    },
    totalApprovedRequestAmount() {
      return this.spendRequestsObj.list
        .filter(req => req.approved)
        .map(req => req.amount)
        .reduce((a, b) => a + b, 0);
    },
    totalFunds() {
      return this.totalDonated - this.totalApprovedRequestAmount;
    },
    fundingGoal() {
      return this.campaign.fundingGoal / 100;
    },
    numberOfDonations() {
      return this.campaign.donations.filter(donation => !donation.refunded).length;
    },
    donationsSortedByAmount() {
      const donationsCopy = [...this.campaign.donations].filter(donation => !donation.refunded);
      return donationsCopy.sort((d1, d2) => d2.amount - d1.amount);
    },
    isManager() {
      return this.campaign.managers.filter(m => m.username === this.$store.state.user.username).length > 0;
    },
    isCreator() {
      return this.campaign.creator.username === this.$store.state.user.username;
    },
    nonCreatorManagerNames() {
      const names = this.campaign.managers
        .map(m => m.username)
        .filter(name => name !== this.campaign.creator.username);
      let list = "";
      for (const name of names) {
        list += name + " ";
      }
      return list;
    },
    viewDates() {
      const uptoDateIndex = 10;
      const uptoTimeIndex = 16;
      return {
        startDate: this.campaign.startDate.slice(0, uptoDateIndex),
        endDate: this.campaign.endDate.slice(0, uptoTimeIndex) // TODO: change slice index
      }
    }
  },
  methods: {
    async deleteCampaign() {
      if (!confirm("Are you sure you want to delete this campaign?")) {
        return;
      }
      this.isLoading = true;
      try {
        const response = await campaignService.deleteCampaign(this.campaign.id);
        if (response.status === 204) {
          this.$store.commit('SET_NOTIFICATION', {
            message: 'Deleted Campaign! Any outstanding donations associated have been refunded to the donors',
            type: 'success'
          });
          this.$router.push({ name: 'home' })
        }
      } catch (error) {
        campaignService.handleErrorResponse(this.$store, error, 'deleting', 'campaign');
      } finally {
        this.isLoading = false;
      }
    },
    goToCreateDonationView() {
      if (!this.isLocked) {
        this.$router.push({ name: 'CreateDonationView', params: { id: this.campaign.id } });
      }
    },
    goToCreateSpendRequestView() {
      this.$router.push({ name: 'CreateSpendRequestView', params: { id: this.campaign.id } })
    }
  }
}
</script>

<style scoped>
.content {
  /* max-width: 800px; */
  margin: 10px;
}

.header {
  display: flex;
  justify-content: space-between;
}

.header a,
.header button {
  margin-top: 1em;
}

.header .buttons .fa-plus {
  margin-right: 10px;
}

.campaign-owner-info {
  display: flex;
}

.progress-container {
  max-width: 500px;
}

.progress-container .goal-text,
.progress-container .num-donations {
  color: grey;
}

.progress-container .amount-raised {
  display: inline;
}

.progress-container progress {
  margin-bottom: 0.5rem;
}

.campaign-creator {
  font-weight: 600;
}

.campaign-other-owners {
  font-weight: 600;
}

.side-info {
  display: flex;
}

.side-info>section {
  width: 50%;
}

.donation-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.donation-header>* {
  margin-right: 10px;
}

.donate-button {
  margin-left: 20px;
}

.spend-requests>h2 {
  margin-bottom: 41px;
}
</style>
