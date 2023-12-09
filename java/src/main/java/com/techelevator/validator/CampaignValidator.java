package com.techelevator.validator;

import com.techelevator.dao.JdbcCampaignDao;
import com.techelevator.model.Campaign;
import com.techelevator.model.Donation;
import com.techelevator.model.User;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CampaignValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Campaign.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Campaign campaign = (Campaign) o;

        // validate dates
        if (campaign.getEndDate().isBefore(campaign.getStartDate())) {
            errors.reject("End date before start date");
        }

        // validate private if deleted
        if (campaign.isPublic() && campaign.isDeleted()) {
            errors.reject("Campaign must be private if deleted");
        }

        // validate locked if deleted
        if (!campaign.isLocked() && campaign.isDeleted()) {
            errors.reject("Campaign must be locked if deleted");
        }

        // validate all donation refunded if deleted
        if (campaign.isDeleted()
                && !campaign.getDonations()
                .stream()
                .allMatch(Donation::isRefunded)) {
            errors.reject("All donations must be refunded if campaign is " +
                    "deleted");
        }

        // validate list of managers contains creator
        if (!campaign.containsManager(
                campaign.getCreator().getId())) {
            errors.reject("List of managers must contain campaign creator");
        }

        // validate no manager donated to campaign
        for (User user : campaign.getManagers()) {
            if (campaign.containsDonor(user.getId())) {
                errors.reject("A manager cannot donate to their own campaign");
            }
        }

        // TODO: should this constraint exist?
        // validate no donations are refunded if unlocked
        if (!campaign.isLocked()
                && campaign.getDonations()
                .stream()
                .anyMatch(Donation::isRefunded)) {
            errors.reject("Donations can only be refunded if the campaign is " +
                    "locked");
        }

        // TODO: can be locked before funding goal/end date?
    }


}
