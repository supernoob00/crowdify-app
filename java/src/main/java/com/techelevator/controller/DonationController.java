package com.techelevator.controller;

import com.techelevator.dao.*;
import com.techelevator.model.*;
import com.techelevator.validator.CampaignValidator;
import com.techelevator.validator.NewDonationDtoValidator;
import com.techelevator.validator.UpdateDonationDtoValidator;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class DonationController {
    private final JdbcCampaignDao jdbcCampaignDao;
    private final JdbcDonationDao jdbcDonationDao;
    private final UserDao userDao;
    private final JdbcVoteDao jdbcVoteDao;

    private final JdbcUserDao jdbcUserDao;

    public DonationController(JdbcCampaignDao jdbcCampaignDao,
                              JdbcDonationDao jdbcDonationDao,
                              UserDao userDao, JdbcVoteDao jdbcVoteDao,
                              JdbcUserDao jdbcUserDao) {
        this.jdbcCampaignDao = jdbcCampaignDao;
        this.jdbcDonationDao = jdbcDonationDao;
        this.userDao = userDao;
        this.jdbcVoteDao = jdbcVoteDao;
        this.jdbcUserDao = jdbcUserDao;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/users/{id}/donations")
    @ResponseStatus(HttpStatus.OK)
    public List<Donation> getDonationsByUserId(Principal principal, @PathVariable int id) {
        Optional<User> loggedInUser = userDao.getUserById(id);
        if (loggedInUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User id " +
                    "does not exist.");
        }
        if (!loggedInUser.get().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to get these donations.");
        }
        return new ArrayList<>(jdbcDonationDao.getDonationsByUserId(id));
    }


    // TODO: user can donate to private donations
    @PostMapping("/donations")
    @ResponseStatus(HttpStatus.CREATED)
    public Donation createDonation(Principal principal, @Valid @RequestBody NewDonationDto newDonationDto, BindingResult result) {
        // check if valid campaign
        Campaign campaign = jdbcCampaignDao.getCampaignById(
                newDonationDto.getCampaignId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Campaign not found."));

        // check if campaign is locked, private, or DTO donor id does not
        // match current user
        if (campaign.isLocked()
                || !campaign.isPublic()
                || !donationDtoMatchesPrincipal(principal, newDonationDto)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized create this donation.");
        }

        NewDonationDtoValidator validator = new NewDonationDtoValidator(jdbcCampaignDao);
        validator.validate(newDonationDto, result);

        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    result.getAllErrors().toString());
        }

        return jdbcDonationDao.createDonation(newDonationDto);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/donations/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Donation updateDonation(@PathVariable int donationId,
                                   Principal principal,
                                   @Valid @RequestBody UpdateDonationDto updateDonationDto,
                                   BindingResult result) {
        Donation donationToUpdate = jdbcDonationDao.getDonationById(donationId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Donation not found."));

        int userId = AuthenticationController.getUserIdFromPrincipal(principal, jdbcUserDao);
        List<Donation> donations = getDonationsByUserId(principal,donationId);

            
        }


        // TODO: currently nothing in the updateDonationDtoValidator class.

     /*   UpdateDonationDtoValidator validator = new UpdateDonationDtoValidator();
        validator.validate(updateDonationDto, result);

        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    result.getAllErrors().toString());*/


        return null;
    }

/*

        if (campaignToUpdate.containsManager(userId)) {
            return jdbcCampaignDao.updateCampaign(updateCampaignDto).orElseThrow();
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are " +
                "not authorized to update this campaign.");
    }*/



    public boolean donationDtoMatchesPrincipal(@Nullable Principal principal,
                                               NewDonationDto newDonationDto) {
        if (principal == null) {
            return newDonationDto.getDonorId() == null
                    && newDonationDto.isAnonymous();
        }
        String username = principal.getName();
        Optional<User> optionalUser = userDao.getUserByUsername(username);
        User user = optionalUser.orElseThrow();
        int loggedInUserID = user.getId();
        return loggedInUserID == newDonationDto.getDonorId();
    }
}
