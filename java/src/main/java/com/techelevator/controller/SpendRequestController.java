package com.techelevator.controller;

import com.techelevator.dao.JdbcSpendRequestDao;
import com.techelevator.dao.JdbcVoteDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.SpendRequest;
import com.techelevator.model.User;
import com.techelevator.model.Vote;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class SpendRequestController {

    private final UserDao userDao;
    private final JdbcSpendRequestDao jdbcSpendRequestDao;
    private final JdbcVoteDao jdbcVoteDao;

    public SpendRequestController(UserDao userDao, JdbcSpendRequestDao jdbcSpendRequestDao, JdbcVoteDao jdbcVoteDao) {
        this.userDao = userDao;
        this.jdbcSpendRequestDao = jdbcSpendRequestDao;
        this.jdbcVoteDao = jdbcVoteDao;
    }

    @GetMapping("/campaigns/{campaignId}/spend-req")
    @ResponseStatus(HttpStatus.OK)

    public List<SpendRequest> getSpendReqByCampaignId (Principal principal, @PathVariable int userId, @PathVariable int campaignId) {
        Optional<User> loggedInUser = userDao.getUserById(userId);
        if (loggedInUser.isPresent() && !loggedInUser.get().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to view these spend requests.");
        }
        return new ArrayList<>(jdbcSpendRequestDao.getSpendRequestsByCampaign(campaignId));
    }

    @GetMapping("/campaigns/{campaignId}/spend-req/{spend-req-id}/votes")
    @ResponseStatus(HttpStatus.OK)
    public List<Vote> getVotesBySpendReq(Principal principal, @PathVariable int userId, @PathVariable int campId, @PathVariable int spendReqId) {
        Optional<User> loggedInUser = userDao.getUserById(userId);

        if (loggedInUser.isPresent() && !loggedInUser.get().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to get these votes.");
        }
        return new ArrayList<>(jdbcVoteDao.getVoteListBySpendReqId(spendReqId));
    }
}
