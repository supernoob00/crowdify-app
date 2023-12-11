package com.techelevator.validator;

import com.techelevator.dao.JdbcCampaignDao;
import com.techelevator.model.Campaign;
import com.techelevator.model.NewSpendRequestDto;

import java.time.LocalDateTime;

public class NewSpendRequestDtoValidator implements Validator<NewSpendRequestDto> {
    private final JdbcCampaignDao campaignDao;

    public NewSpendRequestDtoValidator(JdbcCampaignDao campaignDao) {
        this.campaignDao = campaignDao;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return NewSpendRequestDto.class.equals(aClass);
    }

    @Override
    public void validate(NewSpendRequestDto dto, ErrorResult errorResult) {
        if (dto.getAmount() <= 0) {
            errorResult.reject("Spend request must be positive");
        }

        // TODO: add buffer period
//        if (dto.getEndDate().isBefore(LocalDateTime.now())) {
//            errorResult.reject("");
//        }

        Campaign campaign = campaignDao.getCampaignById(dto.getCampaignId()).orElse(null);
        if (campaign == null) {
            errorResult.reject("Spend request must be for a valid campaign");
        } else {
            if (campaign.isLocked() || campaign.isDeleted()) {
                errorResult.reject("Campaign must not be locked or deleted");
            }

            if (dto.getAmount() > campaignDao.getTotalFunds(dto.getCampaignId())) {
                errorResult.reject("Must have enough funds for spend request");
            }

            // validate spend request end date is before campaign end date
            if (dto.getEndDate().isBefore(campaign.getStartDate())) {
                errorResult.reject("Spend request end date cannot be before " +
                        "campaign start date");
            }
        }
    }
}
