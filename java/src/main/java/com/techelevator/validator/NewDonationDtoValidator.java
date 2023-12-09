package com.techelevator.validator;

import com.techelevator.dao.JdbcCampaignDao;
import com.techelevator.model.Campaign;
import com.techelevator.model.NewDonationDto;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class NewDonationDtoValidator implements Validator {
    private final JdbcCampaignDao campaignDao;

    public NewDonationDtoValidator(JdbcCampaignDao campaignDao) {
        this.campaignDao = campaignDao;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return NewDonationDtoValidator.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        NewDonationDto dto = (NewDonationDto) o;
        Campaign campaign = campaignDao.getCampaignById(dto.getCampaignId()).orElse(null);

        // validate anonymous if user is null
        if (dto.getDonorId() == null && !dto.isAnonymous()) {
            errors.reject("Donation must be marked anonymous if user is null");
        }

        // validate donor is not a manager

        // validate campaign id is valid
        if (campaign == null) {
            errors.reject("Donation must be to a valid campaign");
        } else {
            // validate receiving campaign is not locked or deleted
            if (campaign.isLocked() || campaign.isDeleted()) {
                errors.reject("Campaign receiving donation must not be locked or " +
                        "deleted");
            }

            // validate campaign not private (?)
        }
    }
}
