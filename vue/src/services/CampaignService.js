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

  getSpendRequestById(id) {
    return axios.get(`/spend-requests/${id}`);
  },

  createSpendRequest(campaignId, newSpendRequestDto) {
    return axios.get(`/campaigns/${campaignId}/spend-requests/create`, newSpendRequestDto);
  },

  putSpendRequest(id, updateSpendRequestDto) {
    return axios.get(`/spend-requests/${id}/edit`, updateSpendRequestDto);
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