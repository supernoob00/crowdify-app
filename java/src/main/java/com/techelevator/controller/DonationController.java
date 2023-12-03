package com.techelevator.controller;

import com.techelevator.dao.JdbcDonationDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.Donation;
import com.techelevator.model.DonationDto;
import com.techelevator.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@PreAuthorize("isAuthenticated")
public class DonationController {
    private final JdbcDonationDao jdbcDonationDao;
    private final UserDao userDao;

    public DonationController(JdbcDonationDao jdbcDonationDao, UserDao userDao) {
        this.jdbcDonationDao = jdbcDonationDao;
        this.userDao = userDao;
    }

    @PostMapping("/donate")
    @ResponseStatus(HttpStatus.CREATED)
    public Donation createDonation(Principal principal, @RequestBody DonationDto donationDto) {

        Donation donation = new Donation();

        // donor ID?

        donation.setCampaignId(donationDto.getCampaignId());
        donation.setAmount(donationDto.getDonationAmount());
        donation.setDate(donationDto.getDonationDate().toLocalDateTime());
        donation.setComment(donationDto.getDonationComment());
        donation.setStatus(donationDto.getDonationStatus());

        boolean result = isCorrectUser(principal, donation);

        if (!result) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't access this donation.");
        }

        if (donationDto.getDonationAmount() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please choose a positive number.");
        }
        return jdbcDonationDao.createDonation(donation);
    }


    public boolean isCorrectUser (Principal principal, Donation donation) {
        String username = principal.getName();
        User user = userDao.getUserByUsername(username);
        int loggedInUserID = user.getId();
        // TODO is this the right way to get the donor id?
        return loggedInUserID == donation.getDonor().getId();
    }

}
