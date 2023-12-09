package com.techelevator.validator;

import com.techelevator.dao.JdbcCampaignDao;
import com.techelevator.model.Campaign;
import com.techelevator.model.Donation;
import com.techelevator.model.UpdateCampaignDto;

import java.time.LocalDateTime;

public class UpdateCampaignDtoValidator implements Validator {
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
    public void validate(Object o, Errors errors) {
        UpdateCampaignDto dto = (UpdateCampaignDto) o;

        // validate date
        if (dto.getEndDate().isBefore(LocalDateTime.now())) {
            errors.reject("End date before current date");
        }

        Campaign campaign = campaignDao.getCampaignById(campaignId).orElse(null);

        // validate campaign id is valid
        if (campaign == null) {
            errors.reject("Spend request must be to a valid campaign");
        } else {
            // TODO: should this constraint exist?
            // validate no donations are refunded if unlocked
            if (!campaign.isLocked()
                    && campaign.getDonations()
                    .stream()
                    .anyMatch(Donation::isRefunded)) {
                errors.reject("Donations can only be refunded if the campaign is " +
                        "locked");
            }

            // TODO:  funding goal can only be increased if already met OR
            //  there are no donors
        }
    }
}
