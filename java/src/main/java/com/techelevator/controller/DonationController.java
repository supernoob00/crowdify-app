package com.techelevator.controller;

import com.techelevator.dao.JdbcDonationDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.Donation;
import com.techelevator.model.NewDonationDto;
import com.techelevator.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@RestController
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class DonationController {
    private final JdbcDonationDao jdbcDonationDao;
    private final UserDao userDao;

    public DonationController(JdbcDonationDao jdbcDonationDao, UserDao userDao) {
        this.jdbcDonationDao = jdbcDonationDao;
        this.userDao = userDao;
    }

    //TODO: Get donations by username controller endpoint/DAO method

    @PostMapping("/donations")
    @ResponseStatus(HttpStatus.CREATED)
    public Donation createDonation(Principal principal, @Valid @RequestBody NewDonationDto newDonationDto) {
        if (!isCorrectUser(principal, newDonationDto)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized create this donation.");
        }
        return jdbcDonationDao.createDonation(newDonationDto);
    }

    //TODO: Update donation (comment only)

    public boolean isCorrectUser(Principal principal, NewDonationDto newDonationDto) {
        String username = principal.getName();
        Optional<User> optionalUser = userDao.getUserByUsername(username);
        User user = optionalUser.orElseThrow();
        int loggedInUserID = user.getId();
        return loggedInUserID == newDonationDto.getDonorId();
    }

}
