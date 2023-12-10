package com.techelevator.service;

import com.techelevator.dao.JdbcUserDao;
import com.techelevator.model.EmailDetails;
import com.techelevator.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public void sendForgotPasswordMail(User user) throws MailException {
        String subject = "Your Forgotten Password";
        String message = "This is a test message";

        EmailDetails email = new EmailDetails(user.getEmail(), message,
                subject);
        sendSimpleMail(email);
    }

    private void sendSimpleMail(EmailDetails details) throws MailException
    {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(sender);
        mailMessage.setTo(details.getRecipient());
        mailMessage.setText(details.getMsgBody());
        mailMessage.setSubject(details.getSubject());

        javaMailSender.send(mailMessage);
    }
}
