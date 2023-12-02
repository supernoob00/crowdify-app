package com.techelevator.controller;

import com.techelevator.dao.UserDao;
import com.techelevator.model.Campaign;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class CampaignController {
    private UserDao userDao;

    public CampaignController(UserDao userDao) {
        this.userDao = userDao;
    }
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/campaigns", method = RequestMethod.GET)
    public List<Campaign> campaignList () {
        return null;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/campaigns/{id}", method = RequestMethod.GET)
    public Campaign getCampaign (@PathVariable int id) {
        return null;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/campaigns", method = RequestMethod.POST)
    public Campaign addCampaign () {
        return null;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(path = "/campaigns/{id}", method = RequestMethod.DELETE)
    public void deleteCampaign (@PathVariable int id) {
        //call DAO delete method here.
    }
}
