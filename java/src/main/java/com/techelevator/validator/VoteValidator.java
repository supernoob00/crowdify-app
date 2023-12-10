package com.techelevator.validator;

import com.techelevator.dao.JdbcCampaignDao;
import com.techelevator.dao.JdbcSpendRequestDao;
import com.techelevator.dao.JdbcUserDao;
import com.techelevator.model.*;

import java.util.Objects;

public class VoteValidator implements Validator<Vote> {
    private final JdbcUserDao userDao;
    private final JdbcSpendRequestDao requestDao;
    private final JdbcCampaignDao campaignDao;

    public VoteValidator(JdbcUserDao userDao,
                               JdbcSpendRequestDao requestDao,
                               JdbcCampaignDao campaignDao) {
        this.userDao = userDao;
        this.requestDao = requestDao;
        this.campaignDao = campaignDao;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }

    @Override
    public void validate(Vote vote, ErrorResult errorResult) {
        // validate voter id is valid
        User voter = userDao.getUserById(vote.getUser().getId()).orElse(null);
        if (voter == null) {
            errorResult.rejectValue("voterId", "invalid");
        }

        // validate spend request id is valid
        SpendRequest request =
                requestDao.getSpendRequestById(vote.getRequestId()).orElse(null);
        if (request == null) {
            errorResult.reject("Request id is invalid");
        } else {
            // validate current date before spend request end date
            // TODO: add time buffer
//            if (request.getEndDate().isBefore(LocalDateTime.now())) {
//                errorResult.reject("Spend request end date must be after current
//                date");
//            }

            Campaign campaign = campaignDao.getCampaignById(
                    request.getCampaignId()).orElseThrow();

            // validate associated campaign is not deleted
            if (campaign.isDeleted()) {
                errorResult.reject("Campaign cannot be deleted");
            }

            // validate voter id is a donor of the campaign associated with the
            // spend request
            if (voter != null && campaign.getDonations().stream()
                    .map(Donation::getDonor)
                    .filter(Objects::nonNull)
                    .map(User::getId)
                    .noneMatch(id -> id == voter.getId())) {
                errorResult.reject("Voter is not a donor of the spend request");
            }

            // validate voter id is not a manager of the campaign associated with
            // the spend request
            if (voter != null && campaign.getManagers().stream()
                    .map(User::getId)
                    .anyMatch(id -> id == voter.getId())) {
                errorResult.reject("Manager cannot vote for their own spend " +
                        "request");
            }
        }
    }
}
