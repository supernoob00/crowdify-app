package com.techelevator.validator;

import com.techelevator.dao.JdbcCampaignDao;
import com.techelevator.model.Campaign;
import com.techelevator.model.Donation;
import com.techelevator.model.UpdateCampaignDto;
import com.techelevator.model.UpdateSpendRequestDto;

import java.time.LocalDateTime;

public class UpdateCampaignDtoValidator implements Validator<UpdateCampaignDto> {
    private final int campaignId;
    private final JdbcCampaignDao campaignDao;

    public UpdateCampaignDtoValidator(int campaignId,
                                      JdbcCampaignDao campaignDao) {
        this.campaignId = campaignId;
        this.campaignDao = campaignDao;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UpdateCampaignDto.class.equals(aClass);
    }

    @Override
    public void validate(UpdateCampaignDto dto, ErrorResult errorResult) {
        if (dto.getFundingGoal() < 100) {
            errorResult.reject("Minimum funding foal for campaign is $1");
        }

        // validate date
        if (dto.getEndDate().isBefore(LocalDateTime.now())) {
            errorResult.reject("End date before current date");
        }

        Campaign campaign = campaignDao.getCampaignById(campaignId).orElse(null);

        // validate campaign id is valid
        if (campaign == null) {
            errorResult.reject("Spend request must be to a valid campaign");
        } else {
            // TODO: should this constraint exist?
            // validate no donations are refunded if unlocked
            if (!campaign.isLocked()
                    && campaign.getDonations()
                    .stream()
                    .anyMatch(Donation::isRefunded)) {
                errorResult.reject("Donations can only be refunded if the campaign is " +
                        "locked");
            }

            // validate new campaign end date is not before current date
            if (dto.getEndDate().isBefore(LocalDateTime.now())) {
                errorResult.reject("New campaign end date must not be before " +
                        "current date");
            }

            // TODO:  funding goal can only be increased if already met OR
            //  there are no donors

        }
    }
}
