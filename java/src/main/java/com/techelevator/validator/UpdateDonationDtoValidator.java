package com.techelevator.validator;

import com.techelevator.dao.JdbcCampaignDao;
import com.techelevator.dao.JdbcDonationDao;
import com.techelevator.dao.JdbcUserDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.Campaign;
import com.techelevator.model.Donation;
import com.techelevator.model.UpdateDonationDto;
import com.techelevator.model.User;

public class UpdateDonationDtoValidator implements Validator<UpdateDonationDto> {
    private int donationId;
    private JdbcDonationDao donationDao;
    private JdbcCampaignDao campaignDao;

    public UpdateDonationDtoValidator(int donationId,
                                      JdbcDonationDao donationDao,
                                      JdbcCampaignDao campaignDao) {
        this.donationId = donationId;
        this.donationDao = donationDao;
        this.campaignDao = campaignDao;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UpdateDonationDto.class.equals(aClass);
    }

    @Override
    public void validate(UpdateDonationDto dto, ErrorResult errorResult) {
        Donation donation = donationDao.getDonationById(donationId).orElse(null);

        // validate donation id is valid
        if (donation == null) {
            errorResult.reject("Donation must have valid id");
        } else {
            if (donation.isRefunded()) {
                errorResult.reject("Cannot update a refunded donation");
            }

            if (dto.getAmount() <= 0) {
                errorResult.reject("Donation must be greater than zero");
            }

            User voter = donation.getDonor();
            if (voter == null) {
                errorResult.reject("Donation must have been made by a logged " +
                        "in user to update");
            }

            if (dto.isRefunded() && dto.getAmount() != donation.getAmount()) {
                errorResult.reject("Cannot set donation to refunded while changing donation amount");
            }

            Campaign campaign = campaignDao.getCampaignById(donation.getCampaignId()).orElseThrow();
            if (campaign.isDeleted()) {
                errorResult.reject("Cannot update donation to deleted " +
                        "campaign");
            }
        }
    }
}
