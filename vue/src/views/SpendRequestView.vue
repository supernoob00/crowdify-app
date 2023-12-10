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
      <p>{{ amountDisplay }}</p>
      <h3>{{ spendRequest.description }}</h3>
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
      return `$${this.spendRequest.amount / 100}`;
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
  },
  async created() {
    await this.getSpendRequest();
    await this.getCampaign();
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

hr {
  margin: 1rem 0;
}
</style>