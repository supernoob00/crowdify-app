package com.techelevator.controller;

import com.techelevator.dao.JdbcCampaignDao;
import com.techelevator.dao.JdbcUserDao;
import com.techelevator.dao.UserDao;
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
    private final UserDao userDao;

    public CampaignController(JdbcCampaignDao jdbcCampaignDao, JdbcUserDao jdbcUserDao, UserDao userDao) {
        this.jdbcCampaignDao = jdbcCampaignDao;
        this.jdbcUserDao = jdbcUserDao;
        this.userDao = userDao;
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
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/campaigns/{id}", method = RequestMethod.DELETE)
    public void deleteCampaign(@PathVariable int id) {
        //TODO call DAO delete method here. Can only delete when campaign is locked and has 0 zero donations
    }

    @PreAuthorize("isAuthenticated")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/users/{id}/campaigns")
    public List<Campaign> getListOfMyCampaigns(Principal principal, @PathVariable int id) {
        Optional<User> loggedInUser;
        loggedInUser = userDao.getUserByUsername(principal.getName());

        List<Campaign> myCampaigns = new ArrayList<>();
        for (Campaign campaign : jdbcCampaignDao.getCampaignById())
    }
}
