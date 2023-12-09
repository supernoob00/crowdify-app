package com.techelevator.validator;

import com.techelevator.dao.JdbcCampaignDao;
import com.techelevator.model.Campaign;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UpdateCampaignDto implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return false;
    }

    @Override
    public void validate(Object o, Errors errors) {
        UpdateCampaignDto dto = (UpdateCampaignDto) o;
        // validate dates
        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            errors.reject("End date before start date");
        }
    }
}
