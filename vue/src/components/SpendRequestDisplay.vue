<template>
  <div class="box">
    <router-link class="is-size-5" :class="{ 'disabled-button': campaign.deleted }" :to="{
      name: 'SpendRequestView',
      params: { campaignId: spendRequest.campaignId, spendRequestId: spendRequest.id }
    }">
      {{ spendRequest.name }}
    </router-link>
    <h4> {{ amountDisplay }}</h4>
    <p class="is-italic">{{ spendRequest.description }}</p>
    <p>{{ "(" + status + ")" }}</p>
  </div>
</template>

<script setup>
import { defineProps, onBeforeMount, ref, computed } from 'vue';
import campaignService from '../services/CampaignService';
import Util from '../services/Util';

const props = defineProps(['spendRequest']);

const amountDisplay = computed(() => `$${Util.formatToMoney(props.spendRequest.amount)}`)
const status = computed(() => props.spendRequest.approved ? 'Approved' : 'Pending')

const campaign = ref({});
async function getCampaign() {
  try {
    const response = await campaignService.getCampaign(props.spendRequest.campaignId);
    if (response.status === 200) {
      campaign.value = response.data;
    }
  } catch (error) {
    campaignService.handleErrorResponse(this.$store, error, 'getting', 'campaign');
  }
}
onBeforeMount(async () => {
  await getCampaign();
})

</script>

<style scoped>
.disabled-button {
  pointer-events: none;
  color: var(--font-color);
}
</style>