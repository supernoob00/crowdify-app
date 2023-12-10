package com.techelevator.validator;

import com.techelevator.dao.JdbcCampaignDao;
import com.techelevator.dao.JdbcSpendRequestDao;
import com.techelevator.dao.JdbcVoteDao;
import com.techelevator.model.Campaign;
import com.techelevator.model.SpendRequest;
import com.techelevator.model.UpdateSpendRequestDto;
import com.techelevator.model.Vote;

import java.util.List;

public class UpdateSpendRequestDtoValidator implements Validator<UpdateSpendRequestDto> {
    private final int requestId;
    private final JdbcSpendRequestDao requestDao;
    private final JdbcCampaignDao campaignDao;
    private final JdbcVoteDao voteDao;

    public UpdateSpendRequestDtoValidator(int requestId,
                                          JdbcSpendRequestDao requestDao,
                                          JdbcCampaignDao campaignDao,
                                          JdbcVoteDao voteDao) {
        this.requestId = requestId;
        this.requestDao = requestDao;
        this.campaignDao = campaignDao;
        this.voteDao = voteDao;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UpdateSpendRequestDto.class.equals(aClass);
    }

    @Override
    public void validate(UpdateSpendRequestDto dto, ErrorResult errorResult) {
        if (dto.getAmount() <= 0) {
            errorResult.reject("Spend request amount must be greater than " +
                    "zero");
        }

        SpendRequest request = requestDao.getSpendRequestById(requestId).orElseThrow();
        Campaign campaign = campaignDao.getCampaignById(request.getCampaignId()).orElseThrow();

        // validate spend request end date is before campaign end date
        if (request.getEndDate().isBefore(campaign.getStartDate())) {
            errorResult.reject("Spend request end date cannot be before " +
                    "campaign start date");
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
        if (dto.isApproved() && yesCount * 2 <= votes.size()) {
            errorResult.reject("There must be a majority of voters for a spend" +
                    " request to be approved.");
        }

        // A spend request cannot be made not approved if already approved
        if (request.isApproved() && !dto.isApproved()) {
            errorResult.reject("A spend request cannot be made not approved " +
                    "if already approved");
        }
    }
}
