package com.techelevator.validator;

import com.techelevator.dao.JdbcUserDao;
import com.techelevator.model.NewCampaignDto;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;

public class NewCampaignDtoValidator implements Validator {
    private final JdbcUserDao userDao;

    public NewCampaignDtoValidator(JdbcUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return NewCampaignDtoValidator.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        NewCampaignDto dto = (NewCampaignDto) o;

        // validate dates
        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            errors.reject("End date before start date");
        }

        // TODO: might need a buffer period
        // validate start date after current time
        if (dto.getStartDate().isBefore(LocalDateTime.now())) {
            errors.reject("startDate", "start time is before current time");
        }

        // validate valid creator id
        if (userDao.getUserById(dto.getCreatorId()).isEmpty()) {
            errors.rejectValue("creatorId", "creatorId.invalid");
        }
    }
}
