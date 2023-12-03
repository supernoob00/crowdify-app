package com.techelevator.controller;

import com.techelevator.dao.JdbcCampaignDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.Campaign;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class CampaignController {
    private JdbcCampaignDao jdbcCampaignDao;

    public CampaignController(JdbcCampaignDao jdbcCampaignDao) {
        this.jdbcCampaignDao = jdbcCampaignDao;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/campaigns", method = RequestMethod.GET)
    public List<Campaign> campaignList() {
        return jdbcCampaignDao.getCampaignList();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/campaigns/{id}", method = RequestMethod.GET)
    public Campaign getCampaign(@PathVariable int id) {
        Optional<Campaign> campaign = jdbcCampaignDao.getCampaignById(id);
        return campaign.orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Campaign not found.");
        });
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/campaigns", method = RequestMethod.POST)
    public Campaign addCampaign(@RequestBody Campaign newCampaign) {
        return jdbcCampaignDao.createCampaign(newCampaign);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/campaigns/{id}", method = RequestMethod.DELETE)
    public void deleteCampaign(@PathVariable int id) {

        //TODO call DAO delete method here.
    }
}
