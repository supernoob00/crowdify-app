import axios from "axios";

export default {
  getCampaign(id) {
    return axios.get(`/campaigns/${id}`);
  },

  listCampaigns() {
    return axios.get('/campaigns');
  },

  addCampaign(newCampaignDto) {
    return axios.post('/campaigns', newCampaignDto);
  },

  updateCampaign(updateCampaignDto, id) {
    return axios.put(`/campaigns/${id}`, updateCampaignDto);
  },

  createDonation(newDonationDto) {
    return axios.post('/donations', newDonationDto);
  },

  getDonationsByUser(userId) {
    return axios.get(`/users/${userId}/donations`);
  },

  getSpendRequestById(campaignId, spendRequestId) {
    console.log(`/campaigns/${campaignId}/spend-requests/${spendRequestId}`);
    return axios.get(`/campaigns/${campaignId}/spend-requests/${spendRequestId}`);
  },

  createSpendRequest(newSpendRequestDto) {
    return axios.post(`/campaigns/${newSpendRequestDto.campaignId}/spend-requests`, newSpendRequestDto);
  },

  putSpendRequest(updateSpendRequestDto) {
    return axios.put(`/spend-requests/${updateSpendRequestDto.id}`, updateSpendRequestDto);
  },

  getSpendRequestsByCampaignId(id) {
    return axios.get(`/campaigns/${id}/spend-requests`);
  },

  handleErrorResponse(store, error, verb, object) {
    if (error.response) {
      store.commit('SET_NOTIFICATION',
        `Error ${verb} ${object}. Response received was '${error.response.statusText}'.`);
    } else if (error.request) {
      store.commit('SET_NOTIFICATION', `Error ${verb} ${object}. Server could not be reached.`);
    } else {
      store.commit('SET_NOTIFICATION', `Error ${verb} ${object}. Request could not be created.`);
    }
  }
}