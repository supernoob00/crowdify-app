package com.techelevator.controller;

import com.techelevator.dao.JdbcCampaignDao;
import com.techelevator.dao.JdbcDonationDao;
import com.techelevator.dao.JdbcUserDao;
import com.techelevator.model.*;
import com.techelevator.validator.NewCampaignDtoValidator;
import org.springframework.http.HttpStatus;
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
public class CampaignController {
    private final JdbcCampaignDao jdbcCampaignDao;
    private final JdbcUserDao jdbcUserDao;
    private final JdbcDonationDao jdbcDonationDao;

    public CampaignController(JdbcCampaignDao jdbcCampaignDao,
                              JdbcUserDao jdbcUserDao,
                              JdbcDonationDao jdbcDonationDao) {
        this.jdbcCampaignDao = jdbcCampaignDao;
        this.jdbcUserDao = jdbcUserDao;
        this.jdbcDonationDao = jdbcDonationDao;
    }

    /* show all public campaigns and campaigns you own */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/campaigns", method = RequestMethod.GET)
    public List<Campaign> campaignList(Principal principal) {
        Optional<User> loggedInUser;
        if (principal == null) {
            loggedInUser = Optional.empty();
        } else {
            loggedInUser = jdbcUserDao.getUserByUsername(principal.getName());
        }
        List<Campaign> campaigns = new ArrayList<>();

        for (Campaign campaign : jdbcCampaignDao.getCampaignList()) {
            if (campaign.isPublic()
                    || (loggedInUser.isPresent()
                        && campaign.getManagers().contains(loggedInUser.get()))) {
                campaigns.add(campaign);
            }
        }
        return campaigns;
    }

    /* show campaign if user has view permissions */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/campaigns/{id}", method = RequestMethod.GET)
    public Campaign getCampaign(@PathVariable int id, Principal principal) {
        Campaign campaign = jdbcCampaignDao.getCampaignById(id).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Campaign not found.");
        });;

        if (campaign.isPublic()) {
            return campaign;
        }

        // campaign is private, and no user is logged in
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are " +
                    "not authorized to view this campaign.");
        }

        // check if id of logged in user matches any manager or donor of
        // campaign
        int userId = AuthenticationController.getUserIdFromPrincipal(principal, jdbcUserDao);
        if (campaign.containsManager(userId) || campaign.containsDonor(userId)) {
            return campaign;
        }
        // no match found
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are " +
                "not authorized to view this campaign.");
    }

    // TODO: check same name, valid times, valid fund request
    /* create a new campaign with the creator as the current logged in user */
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/campaigns", method = RequestMethod.POST)
    public Campaign addCampaign(@Valid @RequestBody NewCampaignDto newCampaignDto,
                                BindingResult result) {
        NewCampaignDtoValidator validator = new NewCampaignDtoValidator(jdbcUserDao);
        validator.validate(newCampaignDto, result);

        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid data");
        }
        return jdbcCampaignDao.createCampaign(newCampaignDto);
    }

    // TODO: should return error if dto and url do not match
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/campaigns/{id}", method = RequestMethod.PUT)
    public Campaign updateCampaign(@Valid @RequestBody UpdateCampaignDto updateCampaignDto,
                                   @PathVariable int id,
                                   Principal principal) {
        Campaign campaignToUpdate = jdbcCampaignDao.getCampaignById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Campaign not found."));

        int userId = AuthenticationController.getUserIdFromPrincipal(principal, jdbcUserDao);

        if (campaignToUpdate.containsManager(userId)) {
            return jdbcCampaignDao.updateCampaign(updateCampaignDto).orElseThrow();
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are " +
                "not authorized to update this campaign.");
    }

    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/campaigns/{id}", method = RequestMethod.DELETE)
    public void deleteCampaign(@PathVariable int id, Principal principal) {
        Campaign campaignToDelete = jdbcCampaignDao.getCampaignById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Campaign not found."));

        int userId = AuthenticationController.getUserIdFromPrincipal(principal, jdbcUserDao);

        if (campaignToDelete.containsManager(userId)) {
            jdbcCampaignDao.markCampaignDeletedById(id);
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are " +
                "not authorized to delete this campaign.");
    }

    /* Gets all campaigns user is a manager for */
    @PreAuthorize("isAuthenticated")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/{id}/campaigns")
    public List<Campaign> getUserCampaigns(Principal principal, @PathVariable int id) {
        User loggedInUser = jdbcUserDao.getUserByUsername(principal.getName()).orElseThrow();
        if (loggedInUser.getId() != id) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are " +
                    "not authorized to view this user's campaigns.");
        }
        return jdbcCampaignDao.getCampaignsByManagerId(loggedInUser.getId());
    }
}
