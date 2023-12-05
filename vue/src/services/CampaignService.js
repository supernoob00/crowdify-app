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

  updateCampaign(updateCampaignDto) {
    return axios.put('/campaigns', updateCampaignDto);
  },

  createDonation(newDonationDto) {
    return axios.post('/donations', newDonationDto);
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