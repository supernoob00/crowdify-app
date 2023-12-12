package com.techelevator.controller;

import com.techelevator.dao.*;
import com.techelevator.model.*;
import com.techelevator.validator.*;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
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

    @GetMapping("/campaigns/{campaignId}/spend-requests/{requestId}")
    @ResponseStatus(HttpStatus.OK)
    public SpendRequest getSpendRequestById(@PathVariable int campaignId, @PathVariable int requestId, Principal principal) {

        SpendRequest spendRequest = jdbcSpendRequestDao.getSpendRequestById(requestId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Spend request not found.");
        });

        if (spendRequest.getCampaignId() != campaignId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid path.");
        }

        Campaign requestCampaign = jdbcCampaignDao.getCampaignById(campaignId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Campaign not found");
        });
        int userId = AuthenticationController.getUserIdFromPrincipal(principal, userDao);

        if (!requestCampaign.containsDonor(userId) && !requestCampaign.containsManager(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are " +
                    "not authorized to view this spend request.");
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
            if (campaignId != campaign.getId() && !requestCampaign.containsManager(userId)
                    && !requestCampaign.containsDonor(userId)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are " +
                        "not authorized to view this campaign.");
            }
        }
        return spendRequests;
    }

    @PostMapping("/campaigns/{campaignId}/spend-requests")
    @ResponseStatus(HttpStatus.CREATED)
    public SpendRequest newRequest(@Valid @RequestBody NewSpendRequestDto newSpendRequestDto,
                                   @PathVariable int campaignId,
                                   Principal principal) {
        newSpendRequestDto.setCampaignId(campaignId);
        Campaign campaign = jdbcCampaignDao.getCampaignById(campaignId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Campaign not found");
        });
        int loggedInUserId = AuthenticationController.getUserIdFromPrincipal(principal, userDao);
        if (!campaign.containsManager(loggedInUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to update a spend request.");
        }
        ErrorResult result = new ErrorResult();
        Validator<NewSpendRequestDto> validator = new NewSpendRequestDtoValidator(jdbcCampaignDao);
        validator.validate(newSpendRequestDto, result);

        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, result.getCauses().toString());
        }
        return jdbcSpendRequestDao.createSpendRequest(newSpendRequestDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("campaigns/{campaignId}/spend-requests/{requestId}")
    public SpendRequest updateSpendRequest(@Valid @RequestBody UpdateSpendRequestDto updateSpendRequestDto,
                                           @PathVariable int campaignId,
                                           @PathVariable int requestId,
                                           Principal principal) {

        Campaign campaign = jdbcCampaignDao.getCampaignById(campaignId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Campaign not found");
        });
        int loggedInUserId = AuthenticationController.getUserIdFromPrincipal(principal, userDao);
        if (!campaign.containsManager(loggedInUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to update a spend request.");
        }
        ErrorResult result = new ErrorResult();
        Validator<UpdateSpendRequestDto> validator = new UpdateSpendRequestDtoValidator(requestId,
                jdbcSpendRequestDao, jdbcCampaignDao, jdbcVoteDao);
        validator.validate(updateSpendRequestDto, result);

        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    result.getCauses().toString());
        }
        return jdbcSpendRequestDao.updateSpendRequest(updateSpendRequestDto, requestId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/campaigns/{campaignId}/spend-requests/{requestId}")
    public boolean deleteSpendRequest(Principal principal,
                                      @PathVariable int campaignId,
                                      @PathVariable int requestId) {
        SpendRequest spendRequest = jdbcSpendRequestDao.getSpendRequestById(requestId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Spend request not found.");
        });
        if (spendRequest.getCampaignId() != campaignId) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid path.");
        }
        Campaign requestCampaign = jdbcCampaignDao.getCampaignById(campaignId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Campaign not found");
        });
        int userId = AuthenticationController.getUserIdFromPrincipal(principal, userDao);
        if (!requestCampaign.containsManager(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You're not allowed to delete this spend request");
        }
        if (!jdbcVoteDao.deleteVoteBySpendRequestId(requestId)
                || !jdbcSpendRequestDao.deleteSpendRequestById(requestId)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "There was an error deleting this spend request");
        }
        return true;
    }

    // **************************************** vote handling *******************************************
    @GetMapping("campaigns/{campaignId}/spend-requests/{requestId}/votes")
    @ResponseStatus(HttpStatus.OK)
    public List<Vote> getVotesBySpendReq(Principal principal,
                                         @PathVariable int campaignId,
                                         @PathVariable int requestId) {
        Optional<User> loggedInUser = userDao.getUserByUsername(principal.getName());

        return new ArrayList<>(jdbcVoteDao.getVotesBySpendRequestId(requestId));
    }

// get votes by user id ??

// create vote

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/campaigns/{campaignId}/spend-requests/{requestId}/votes")

    public Vote createVote(@Valid @RequestBody NewVoteDto newvoteDto,
                           Principal principal,
                           @PathVariable int campaignId,
                           @PathVariable int requestId) {
        int loggedInUserId = AuthenticationController.getUserIdFromPrincipal(principal, userDao);
        Campaign campaign = jdbcCampaignDao.getCampaignById(campaignId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Campaign not found");
        });
        if (!campaign.containsDonor(loggedInUserId) &&
                !campaign.containsManager(loggedInUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to vote in this campaign.");
        }

        ErrorResult result = new ErrorResult();
        Validator<NewVoteDto> validator = new NewVoteDtoValidator(jdbcUserDao,
                jdbcSpendRequestDao, jdbcCampaignDao);
        validator.validate(newvoteDto, result);

        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, result.getCauses().toString());
        }
        return jdbcVoteDao.createVote(newvoteDto);
    }

// update vote

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/campaigns/{campaignId}/spend-requests/{requestId}/votes")

    public Vote updateVote(@Valid @RequestBody UpdateVoteDto updateVoteDto,
                           Principal principal,
                           @PathVariable int campaignId,
                           @PathVariable int requestId) {

        int loggedInUserId = AuthenticationController.getUserIdFromPrincipal(principal, userDao);
        Campaign campaign = jdbcCampaignDao.getCampaignById(campaignId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Campaign not found");
        });
        if (!campaign.containsDonor(loggedInUserId) &&
                !campaign.containsManager(loggedInUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to vote in this campaign.");
        }

        SpendRequest spendRequest = jdbcSpendRequestDao.getSpendRequestById(updateVoteDto.getRequestId()).orElseThrow();

        ErrorResult result = new ErrorResult();
        if (spendRequest.isApproved()) {
            result.reject("This spend request is no longer accepting votes");
        }

//TODO: add a new validator for updateVoteDto
//        Validator<Vote> validator = new VoteValidator(jdbcUserDao,
//                jdbcSpendRequestDao, jdbcCampaignDao);
//        validator.validate(updateVoteDto, result);

        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    result.getCauses().toString());
        }

        return jdbcVoteDao.changeVote(updateVoteDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/campaigns/{campaignId}/spend-requests/{requestId}/votes")
    public boolean deleteVote(Principal principal,
                              @PathVariable int campaignId,
                              @PathVariable int requestId) {
        int loggedInUserId = AuthenticationController.getUserIdFromPrincipal(principal, userDao);
        return jdbcVoteDao.deleteVoteById(loggedInUserId, requestId);
    }

    public boolean isCorrectUser(Principal principal, NewDonationDto newDonationDto) {
        String username = principal.getName();
        Optional<User> optionalUser = userDao.getUserByUsername(username);
        User user = optionalUser.orElseThrow();
        int loggedInUserID = user.getId();
        return loggedInUserID == newDonationDto.getDonorId();
    }
}