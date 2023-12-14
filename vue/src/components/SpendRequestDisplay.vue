<template>
  <router-link class="is-size-5" :class="{ 'disabled-button': campaign.deleted }" :to="{
    name: 'SpendRequestView',
    params: { campaignId: spendRequest.campaignId, spendRequestId: spendRequest.id }
  }">
    <div class="box">
      <p class="has-text-weight-semibold">{{ spendRequest.name }}</p>
      <h4> {{ amountDisplay }}</h4>
      <p class="is-italic title is-6 has-text-weight-normal">{{ spendRequest.description }}</p>
      <p>{{ "(" + status + ")" }}</p>
    </div>
  </router-link>
</template>

<script setup>
import { defineProps, onBeforeMount, ref, computed } from 'vue';
import CampaignService from '../services/CampaignService';
import Util from '../services/Util';

const props = defineProps(['spendRequest']);

const amountDisplay = computed(() => `$${Util.formatToMoney(props.spendRequest.amount)}`)
const status = computed(() => props.spendRequest.approved ? 'Approved' : 'Pending')

const campaign = ref({});
async function getCampaign() {
  try {
    const response = await CampaignService.getCampaign(props.spendRequest.campaignId);
    if (response.status === 200) {
      campaign.value = response.data;
    }
  } catch (error) {
    CampaignService.handleErrorResponse(this.$store, error, 'getting', 'campaign');
  }
}
onBeforeMount(async () => {
  await getCampaign();
})

</script>

<style scoped>
.disabled-button {
  pointer-events: none;
}

a {
  color: var(--link-color)
}
</style>