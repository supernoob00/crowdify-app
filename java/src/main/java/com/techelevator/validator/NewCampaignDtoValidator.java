package com.techelevator.validator;

import com.techelevator.dao.JdbcUserDao;
import com.techelevator.model.NewCampaignDto;

public class NewCampaignDtoValidator implements Validator<NewCampaignDto> {
    private final JdbcUserDao userDao;

    public NewCampaignDtoValidator(JdbcUserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return NewCampaignDtoValidator.class.equals(aClass);
    }

    @Override
    public void validate(NewCampaignDto dto, ErrorResult errorResult) {
        if (dto.getFundingGoal() < 100) {
            errorResult.reject("Minimum funding foal for campaign is $1");
        }

        // validate dates
        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            errorResult.reject("End date before start date");
        }

      /*  // TODO: might need a buffer period
        // validate start date after current time
        if (dto.getStartDate().isBefore(LocalDateTime.now())) {
            errorResult.reject("startDate", "start time is before current time");
        }*/

        // validate valid creator id
        if (userDao.getUserById(dto.getCreatorId()).isEmpty()) {
            errorResult.rejectValue("creatorId", "creatorId.invalid");
        }
    }
}
