package com.techelevator.dao;

import com.techelevator.model.Donation;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class JdbcDonationDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcDonationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // get a list of donations made by the logged-in user

    public List<Donation> donationList(int userId) {
        List<Donation> donationList = new ArrayList<>();

       // String sql =

        return null;
    }


    // get a list of donations to a campaign for the campaign manager


    // make a new donation to a campaign





}
