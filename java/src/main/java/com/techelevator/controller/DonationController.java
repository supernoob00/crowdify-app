package com.techelevator.controller;

import com.techelevator.dao.JdbcDonationDao;
import com.techelevator.dao.JdbcSpendRequestDao;
import com.techelevator.dao.JdbcVoteDao;
import com.techelevator.dao.UserDao;
import com.techelevator.model.*;
import org.springframework.boot.context.properties.bind.DefaultValue;
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
public class DonationController {
    private final JdbcDonationDao jdbcDonationDao;
    private final UserDao userDao;
    private final JdbcVoteDao jdbcVoteDao;

    public DonationController(JdbcDonationDao jdbcDonationDao, UserDao userDao, JdbcVoteDao jdbcVoteDao) {
        this.jdbcDonationDao = jdbcDonationDao;
        this.userDao = userDao;
        this.jdbcVoteDao = jdbcVoteDao;
    }

    //TODO: Get donations by username controller endpoint/DAO method
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/users/{id}/donations")
    @ResponseStatus(HttpStatus.OK)
    public List<Donation> getDonationsByUserId(Principal principal, @PathVariable int id) {
        //Reformatted method a bit. If id and logged in user don't match, we throw exception
        Optional<User> loggedInUser = userDao.getUserById(id);
        if (loggedInUser.isPresent() && !loggedInUser.get().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to get these donations.");
        }
        return new ArrayList<>(jdbcDonationDao.getDonationsByUserId(id));
    }


    @PostMapping("/donations")
    @ResponseStatus(HttpStatus.CREATED)
    public Donation createDonation(Principal principal, @Valid @RequestBody NewDonationDto newDonationDto) {
        if (principal == null) {
            return jdbcDonationDao.createDonation(newDonationDto);
        }
        if (!isCorrectUser(principal, newDonationDto)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized create this donation.");
        }
        return jdbcDonationDao.createDonation(newDonationDto);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/donations/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Donation updateDonation(Principal principal, @Valid @RequestBody UpdateDonationDto updateDonationDto) {
        //TODO: Update donation controller method (comment and refunded status as well?)
        return null;
    }


    public boolean isCorrectUser(Principal principal, NewDonationDto newDonationDto) {
        String username = principal.getName();
        Optional<User> optionalUser = userDao.getUserByUsername(username);
        User user = optionalUser.orElseThrow();
        int loggedInUserID = user.getId();
        return loggedInUserID == newDonationDto.getDonorId();
    }
}
