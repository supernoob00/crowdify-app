package com.techelevator.controller;

import com.techelevator.dao.*;
import com.techelevator.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
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
    private final JdbcUserDao jdbcUserDao;

    private final JdbcCampaignDao jdbcCampaignDao;

    public SpendRequestController(UserDao userDao,
                                  JdbcSpendRequestDao jdbcSpendRequestDao,
                                  JdbcVoteDao jdbcVoteDao,
                                  JdbcUserDao jdbcUserDao,
                                  JdbcCampaignDao jdbcCampaignDao) {
        this.userDao = userDao;
        this.jdbcSpendRequestDao = jdbcSpendRequestDao;
        this.jdbcVoteDao = jdbcVoteDao;
        this.jdbcUserDao = jdbcUserDao;
        this.jdbcCampaignDao = jdbcCampaignDao;
    }

    @GetMapping("/spend-requests/{id}")
    @ResponseStatus(HttpStatus.OK)
    public SpendRequest getSpendRequestById(@PathVariable int id, Principal principal) {

        SpendRequest spendRequest = jdbcSpendRequestDao.getSpendRequestById(id).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Spend request not found.");
        });

        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are " +
                    "not authorized to view this spend request.");
        }
        Campaign requestCampaign = jdbcCampaignDao.getCampaignById(spendRequest.getCampaignId()).orElseThrow();
        int userId = AuthenticationController.getUserIdFromPrincipal(principal, userDao);

        if (!requestCampaign.containsDonor(userId) && !requestCampaign.containsManager(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are " +
                    "not authorized to view this campaign.");
        }
        return spendRequest;
    }

    @GetMapping("/campaigns/{campaignId}/spend-requests")
    @ResponseStatus(HttpStatus.OK)
    public List<SpendRequest> getSpendReqByCampaignId(Principal principal, @PathVariable int campaignId) {

        List<SpendRequest> spendRequests = jdbcSpendRequestDao.getSpendRequestsByCampaign(campaignId);
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are " +
                    "not authorized to view this spend request.");
        }

        int userId = AuthenticationController.getUserIdFromPrincipal(principal, userDao);

        List<Campaign> donorCampaigns = jdbcCampaignDao.getCampaignsByDonorId(userId);

        Campaign requestCampaign = jdbcCampaignDao.getCampaignById(campaignId).orElseThrow();

        for (Campaign campaign : donorCampaigns) {
            if (campaignId != campaign.getId() && !requestCampaign.containsManager(userId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are " +
                        "not authorized to view this campaign.");
            }
        }
        return spendRequests;
    }

    @PostMapping("/campaigns/{campaignId}/spend-requests")
    @ResponseStatus(HttpStatus.CREATED)
    public SpendRequest newRequest(@Valid @RequestBody NewSpendRequestDto newSpendRequestDto, @PathVariable int campaignId, Principal principal) {
        newSpendRequestDto.setCampaignId(campaignId);

        boolean isManager = false;
        List<User> managerList = userDao.getManagersByCampaignId(campaignId);
        int userId = AuthenticationController.getUserIdFromPrincipal(principal, userDao);

        for (int i = 0; i < managerList.size(); i++) {

            if (userId == managerList.get(i).getId()) {
                isManager = true;
                break;
            }
        }
        if (!isManager) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to create a spend request.");
        }
        return jdbcSpendRequestDao.createSpendRequest(newSpendRequestDto);
    }

    // IN PROGRESS.
    /*@PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/spend-requests/{id}")
    public SpendRequest updateSpendRequest(@Valid @RequestBody UpdateSpendRequestDto updateSpendRequestDto,
                                           @PathVariable int spendRequestId, Principal principal) {

        boolean isManager = false;
        List<User> managerList =
        int userId = AuthenticationController.getUserIdFromPrincipal(principal, userDao);

        for (int i = 0; i < managerList.size(); i++) {

            if (userId == managerList.get(i).getId()) {
                isManager = true;
                break;
            }
        }
        if (!isManager) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to create a spend request.");
        }
        return jdbcSpendRequestDao.getSpendRequestById(updateSpendRequestDto.getId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Spend request not found."));
    }
*/
    // TODO doesn't work yet.
    @GetMapping("/campaigns/{campaignId}/spend-request/{spend-req-id}/votes")
    @ResponseStatus(HttpStatus.OK)
    public List<Vote> getVotesBySpendReq(Principal principal, @PathVariable int userId, @PathVariable int campId,
                                         @PathVariable int spendReqId) {
        Optional<User> loggedInUser = userDao.getUserById(userId);

        if (loggedInUser.isPresent() && !loggedInUser.get().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to get these votes.");
        }
        return new ArrayList<>(jdbcVoteDao.getVotesBySpendRequestId(spendReqId));
    }

    public boolean isCorrectUser(Principal principal, NewDonationDto newDonationDto) {
        String username = principal.getName();
        Optional<User> optionalUser = userDao.getUserByUsername(username);
        User user = optionalUser.orElseThrow();
        int loggedInUserID = user.getId();
        return loggedInUserID == newDonationDto.getDonorId();
    }
}