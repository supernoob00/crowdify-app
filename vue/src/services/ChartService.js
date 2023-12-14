export default {
  getSpendRequestChart(requestId, token) {
    return fetch(`${import.meta.env.VITE_REMOTE_API}/spend-requests/${requestId}/chart`, {
      headers: {
        'Authorization': 'Bearer ' + token
      }
    });
  },
  getCampaignChart(campaignId, token) {
    return fetch(`${import.meta.env.VITE_REMOTE_API}/campaigns/${campaignId}/chart`, {
      headers: {
        'Authorization': 'Bearer ' + token
      }
    });
  }
}