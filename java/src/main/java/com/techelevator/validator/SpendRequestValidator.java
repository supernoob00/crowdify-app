package com.techelevator.validator;

import com.techelevator.dao.JdbcCampaignDao;
import com.techelevator.dao.JdbcSpendRequestDao;
import com.techelevator.dao.JdbcVoteDao;
import com.techelevator.model.Campaign;
import com.techelevator.model.SpendRequest;
import com.techelevator.model.Vote;
import java.util.List;

public class SpendRequestValidator implements Validator {
    private final JdbcCampaignDao campaignDao;
    private final JdbcSpendRequestDao spendRequestDao;
    private final JdbcVoteDao voteDao;

    public SpendRequestValidator(JdbcCampaignDao campaignDao,
                                 JdbcSpendRequestDao spendRequestDao,
                                 JdbcVoteDao voteDao) {
        this.campaignDao = campaignDao;
        this.spendRequestDao = spendRequestDao;
        this.voteDao = voteDao;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return SpendRequest.class.equals(aClass);
    }

    @Override
    public void validate(Object o, ErrorResult errorResult) {
        SpendRequest request = (SpendRequest) o;

        Campaign campaign = campaignDao.getCampaignById(request.getCampaignId()).orElse(null);

        // validate

        // validate campaign id is valid
        if (campaign == null) {
            errorResult.reject("Spend request must be to a valid campaign");
        } else {
            // validate spend request only approved if total (both
            // refunded and non-refunded) donations are greater than or equal
            // to sum of all approved spend requests
            int spendRequestSum = spendRequestDao.approvedTotalByCampaignId(campaign.getId());
            if (request.getAmount() + spendRequestSum > campaign.getDonationTotal()) {
                errorResult.reject("Total amount of approved spend requests " +
                        "exceeds donations");
            }

            // validate spend request should only be approved with majority
            // votes
            List<Vote> votes = voteDao.getVotesByCampaignId(campaign.getId());
            int yesCount = 0;
            for (Vote vote : votes) {
                if (vote.isApproved()) {
                    yesCount++;
                }
            }
            if (yesCount * 2 <= votes.size()) {
                errorResult.reject("There must be a majority of voters for a spend" +
                        " request to be approved.");
            }
        }
    }
}
