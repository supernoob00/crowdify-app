package com.techelevator.validator;

import com.techelevator.dao.JdbcCampaignDao;
import com.techelevator.model.Campaign;
import com.techelevator.model.Donation;

public class DonationValidator implements Validator {
    private final JdbcCampaignDao campaignDao;

    public DonationValidator(JdbcCampaignDao campaignDao) {
        this.campaignDao = campaignDao;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Donation.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Donation donation = (Donation) o;

        // validate anonymous if user is null
        if (donation.getDonor() == null && !donation.isAnonymous()) {
            errors.reject("Donation must be marked anonymous if user is null");
        }

        Campaign campaign = campaignDao.getCampaignById(donation.getCampaignId()).orElse(null);

        // validate campaign id is valid
        if (campaign == null) {
            errors.reject("Donation must be to a valid campaign");
        } else {
            // validate donation is refunded if campaign is deleted
            if (campaign.isDeleted() && !donation.isRefunded()) {
                errors.reject("All donations to a deleted campaign must be " +
                        "refunded");
            }

            // TODO: validate campaign not private (?)
        }
    }
}
