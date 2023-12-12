<template>
  <div class="campaigns block campaign-collection">
    <campaign-card v-for="campaign in publicNotOwnedCampaigns" :key="campaign.id" :campaign="campaign"></campaign-card>
  </div>

  <div class="campaign-collection" v-if="currentUser.username !== undefined">
    <h3>My Public Campaigns</h3>
    <div class="campaigns block">
      <campaign-card v-for="campaign in publicOwnedCampaigns" :key="campaign.id" :campaign="campaign"></campaign-card>
      <span v-if="publicOwnedCampaigns.length === 0">You have no public campaigns.</span>
    </div>
  </div>

  <div class="campaign-collection" v-if="currentUser.username !== undefined">
    <h3>My Private Campaigns</h3>
    <div class="campaigns block">
      <campaign-card v-for="campaign in privateOwnedCampaigns" :key="campaign.id" :campaign="campaign"></campaign-card>
      <span v-if="privateOwnedCampaigns.length === 0">You have no private campaigns.</span>
    </div>
  </div>
</template>

<script>
import CampaignCard from './CampaignCard.vue';
export default {
  components: {
    CampaignCard
  },
  props: ['campaigns'],
  computed: {
    currentUser() {
      return this.$store.state.user;
    },
    publicNotOwnedCampaigns() {
      return this.campaigns.filter(c => c.public && !this.isManager(c));
    },
    privateOwnedCampaigns() {
      return this.campaigns.filter(c => !c.public && this.isManager(c));
    },
    publicOwnedCampaigns() {
      return this.campaigns.filter(c => c.public && this.isManager(c));
    }
  },
  methods: {
    isManager(c) {
      return c.managers.filter(m => m.username === this.currentUser.username).length > 0;
    }
  }
}
</script>

<style scoped>
.content {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.campaigns {
  display: flex;
  margin-right: 20px;
  flex-wrap: wrap;
  column-gap: 20px;
  row-gap: 25px;
  justify-content: flex-start;
  align-items: center;
}

.campaign-collection {
  margin-bottom: 36px;
}

.managed-private {
  background-color: var(--user-is-manager-private);
  border: none;
}

.managed-public {
  background-color: var(--user-is-manager-public);
  border: none;
}

a:link,
a:visited {
  color: black;
  text-decoration: none;
}

/* .fund-percent {
  color: green;
} */
</style>
