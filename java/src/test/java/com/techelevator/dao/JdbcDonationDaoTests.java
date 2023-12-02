package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Donation;
import com.techelevator.model.RegisterUserDto;
import com.techelevator.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class JdbcDonationDaoTests extends BaseDaoTests {
    private JdbcUserDao sut;
    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcUserDao(jdbcTemplate);
    }

    @Test
    public void get_donation_by_id_fails_for_invalid_id() {
//        Assert.assertNull(sut.getDonationById(-1));
    }

    @Test
    public void getDonationById_given_valid_id_returns_donation() {
//        Assert.assertEquals(DONATION_1, sut.getDonationById(1));
//        Assert.assertEquals(DONATION_2, sut.getDonationById(2));
//        Assert.assertEquals(DONATION_3, sut.getDonationById(3));
    }

    @Test
    public void getDonations_returns_all_donations() {
//        List<Donation> donations = sut.getDonations();
//
//        Assert.assertNotNull(donations);
//        Assert.assertEquals(3, donations.size());
//        Assert.assertEquals(DONATION_1, donations.get(0));
//        Assert.assertEquals(DONATION_2, donations.get(1));
//        Assert.assertEquals(DONATION_3, donations.get(2));
    }

    @Test
    public void createDonation_creates_a_donation() {
//        Donation donation = new Donation();
//        Donation createdDonation = sut.createDonation(donation);
//
//        Assert.assertNotNull(createdDonation);
//
//        Donation retrievedDonation = sut.getDonationById(createdDonation.getDonationId());
//        Assert.assertEquals(retrievedDonation, createdDonation);
    }
}
