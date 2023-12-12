<template>
  <div class="content">
    <div class="header">
      <div>
        <h3 class="has-text-danger">This campaign was deleted.</h3>
        <h1 id="campaign-name">{{ campaign.name }}</h1>
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

    <div id="campaign-description" class="block">{{ campaign.description }}</div>
    <h6>From {{ viewDates.startDate }} to {{ viewDates.endDate }}</h6>
    <div class="progress-container">
      <div class="block" id="progress-meter-heading">
        <h3 class="amount-raised">${{ totalDonated }}</h3>
        <span class="goal-text"> raised of ${{ fundingGoal }} Goal</span>
      </div>
      <progress class="progress is-danger is-small" :value="totalDonated" :max="fundingGoal"></progress>
      <div class="num-donations">{{ numberOfDonations }} donations</div>
    </div>
    <hr>
    <div class="side-info">

      <section class="donations">
        <header class="donation-header">
          <h2 class="block">Donations</h2>
        </header>
        <donation-display v-for="donation in donationsSortedByAmount" :key="donation.id" :donation="donation">
        </donation-display>
      </section>

      <!-- <section>
        <h2 class="block">Spend Requests</h2>
        <p v-if="spendRequestsObj.list.length === 0">There are no spend requests created for this campaign yet.</p>
        <spend-request-display v-for="spendRequest in spendRequestsObj.list" :key="spendRequest.id"
          :spend-request="spendRequest"></spend-request-display>
      </section> -->
    </div>
  </div>
</template>

<script>
import DonationDisplay from './DonationDisplay.vue';
// import SpendRequestDisplay from './SpendRequestDisplay.vue';
import Util from '../services/Util';

export default {
  components: {
    DonationDisplay,
    // SpendRequestDisplay,
  },
  props: ['campaign', 'spendRequestsObj'],
  computed: {
    totalDonated() {
      const totalDonated = this.campaign.donations.reduce((sum, d) => sum + d.amount, 0);
      return Util.formatToMoney(totalDonated);
    },
    fundingGoal() {
      return this.campaign.fundingGoal / 100;
    },
    numberOfDonations() {
      return this.campaign.donations.length;
    },
    donationsSortedByAmount() {
      const donationsCopy = [...this.campaign.donations];
      return donationsCopy.sort((d1, d2) => d2.amount - d1.amount);
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
}
</script>

<style scoped>
.content {
  max-width: 800px;
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
</style>
