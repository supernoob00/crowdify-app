package com.techelevator.validator;

import com.techelevator.dao.JdbcCampaignDao;
import com.techelevator.dao.JdbcSpendRequestDao;
import com.techelevator.dao.JdbcUserDao;
import com.techelevator.model.*;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;
import java.util.Objects;

public class NewVoteDtoValidator implements Validator {
    private final JdbcUserDao userDao;
    private final JdbcSpendRequestDao requestDao;
    private final JdbcCampaignDao campaignDao;

    public NewVoteDtoValidator(JdbcUserDao userDao,
                               JdbcSpendRequestDao requestDao,
                               JdbcCampaignDao campaignDao) {
        this.userDao = userDao;
        this.requestDao = requestDao;
        this.campaignDao = campaignDao;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return NewVoteDto.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        NewVoteDto dto = (NewVoteDto) o;

        // validate voter id is valid
        User voter = userDao.getUserById(dto.getDonorId()).orElse(null);
        if (voter == null) {
            errors.rejectValue("voterId", "invalid");
        }

        // validate spend request id is valid
        SpendRequest request = requestDao.getSpendRequestById(dto.getRequestId()).orElse(null);
        if (request == null) {
            errors.rejectValue("userId", "invalid");
        } else {
            // validate spend request not already approved
            if (request.isApproved()) {
                errors.reject("Spend request being voted for is already approved");
            }

            // validate current date before spend request end date
            // TODO: add time buffer
//            if (request.getEndDate().isBefore(LocalDateTime.now())) {
//                errors.reject("Spend request end date must be after current
//                date");
//            }

            Campaign campaign = campaignDao.getCampaignById(
                    request.getCampaignId()).orElseThrow();

            // validate voter id is a donor of the campaign associated with the
            // spend request
            if (voter != null && campaign.getDonations().stream()
                    .map(Donation::getDonor)
                    .filter(Objects::nonNull)
                    .map(User::getId)
                    .noneMatch(id -> id == voter.getId())) {
                errors.reject("Voter is not a donor of the spend request");
            }

            // validate voter id is not a manager of the campaign associated with
            // the spend request
            if (voter != null && campaign.getManagers().stream()
                    .map(User::getId)
                    .anyMatch(id -> id == voter.getId())) {
                errors.reject("Manager cannot vote for their own spend " +
                        "request");
            }
        }
    }
}
