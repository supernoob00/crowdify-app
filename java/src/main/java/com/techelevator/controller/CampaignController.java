package com.techelevator.controller;

import com.techelevator.dao.JdbcCampaignDao;
import com.techelevator.dao.JdbcDonationDao;
import com.techelevator.dao.JdbcUserDao;
import com.techelevator.model.Campaign;
import com.techelevator.model.NewCampaignDto;
import com.techelevator.model.UpdateCampaignDto;
import com.techelevator.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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

    // show all public campaigns and campaigns you own
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
                        && loggedInUser.get().getUsername().equals(principal.getName()))) {
                campaigns.add(campaign);
            }
        }
        return campaigns;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/campaigns/{id}", method = RequestMethod.GET)
    public Campaign getCampaign(@PathVariable int id) {
        Optional<Campaign> campaign = jdbcCampaignDao.getCampaignById(id);
        return campaign.orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Campaign not found.");
        });
    }

    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/campaigns", method = RequestMethod.POST)
    public Campaign addCampaign(@Valid @RequestBody NewCampaignDto newCampaignDto) {
        return jdbcCampaignDao.createCampaign(newCampaignDto);
    }

    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/campaigns/{id}", method = RequestMethod.PUT)
    public Campaign updateCampaign(@Valid @RequestBody UpdateCampaignDto updateCampaignDto) {
        return jdbcCampaignDao.updateCampaign(updateCampaignDto).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        });
    }

    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/campaigns/{id}", method = RequestMethod.DELETE)
    public void deleteCampaign(@PathVariable int id) {
        int deletedCount = jdbcCampaignDao.markedCampaignDeletedById(id);
        if (deletedCount == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
