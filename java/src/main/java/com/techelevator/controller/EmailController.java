package com.techelevator.controller;

import com.techelevator.dao.UserDao;
import com.techelevator.model.PasswordRequestDto;
import com.techelevator.model.User;
import com.techelevator.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RestController
@CrossOrigin
public class EmailController {
    private EmailService emailService;
    private UserDao userDao;

    public EmailController(EmailService emailService, UserDao userDao) {
        this.emailService = emailService;
        this.userDao = userDao;
    }

    @PostMapping("/forgotpassword")
    @ResponseStatus(HttpStatus.OK)
    public void forgotPasswordEmail(@RequestBody PasswordRequestDto passwordRequestDto) {
        User user = userDao.getUserById(passwordRequestDto.getUserId()).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User id " +
                    "does not exist");
        });

        if (user.getEmail() == null ||
                !user.getEmail().equals(passwordRequestDto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email " +
                    "is not correct or does not exist");
        }

        emailService.sendForgotPasswordMail(user);
    }
}
