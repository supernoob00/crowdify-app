<template>
  <div>
    <p>For {{ campaign.name }}</p>
    <p>{{ spendRequest.description }}</p>
  </div>
</template>

<script setup>
import { defineProps, onBeforeMount, ref } from 'vue';
import campaignService from '../services/CampaignService';
const props = defineProps(['spendRequest']);
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

<style scoped></style>