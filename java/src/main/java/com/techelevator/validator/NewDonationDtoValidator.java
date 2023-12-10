package com.techelevator.validator;

import com.techelevator.dao.JdbcCampaignDao;
import com.techelevator.model.Campaign;
import com.techelevator.model.NewDonationDto;

public class NewDonationDtoValidator implements Validator<NewDonationDto> {
    private final JdbcCampaignDao campaignDao;

    public NewDonationDtoValidator(JdbcCampaignDao campaignDao) {
        this.campaignDao = campaignDao;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return NewDonationDtoValidator.class.equals(aClass);
    }

    @Override
    public void validate(NewDonationDto dto, ErrorResult errorResult) {
        // validate positive amount
        if (dto.getAmount() <= 0) {
            errorResult.reject("Donation amount must be positive");
        }

        // validate anonymous if user is null
        if (dto.getDonorId() == null && !dto.isAnonymous()) {
            errorResult.reject("Donation must be marked anonymous if user is null");
        }

        Campaign campaign = campaignDao.getCampaignById(dto.getCampaignId()).orElse(null);

        // validate campaign id is valid
        if (campaign == null) {
            errorResult.reject("Donation must be to a valid campaign");
        } else {
            // validate receiving campaign is not locked or deleted
            if (campaign.isLocked() || campaign.isDeleted()) {
                errorResult.reject("Campaign receiving donation must not be locked or " +
                        "deleted");
            }
            // TODO: validate campaign not private (?)
        }
    }
}
