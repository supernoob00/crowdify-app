package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.validation.constraints.AssertTrue;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class JdbcDonationDaoTests extends BaseDaoTests {
    private JdbcDonationDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        JdbcUserDao userDao = new JdbcUserDao(jdbcTemplate);
        sut = new JdbcDonationDao(jdbcTemplate);
    }

    @Test
    public void get_donation_by_id_fails_for_invalid_id() {
        Assert.assertTrue(sut.getDonationById(-1).isEmpty());
    }

    @Test
    public void getDonationById_given_valid_id_returns_donation() {
        Assert.assertEquals(DONATION_1, sut.getDonationById(1).orElseThrow());
        Assert.assertEquals(DONATION_2, sut.getDonationById(2).orElseThrow());
        Assert.assertEquals(DONATION_3, sut.getDonationById(3).orElseThrow());
    }

    // TODO: test not needed right now
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
        NewDonationDto donation = new NewDonationDto(1, 1, 5000, "test",
                false, false);
        Donation createdDonation = sut.createDonation(donation);

        Donation retrievedDonation =
                sut.getDonationById(createdDonation.getDonationId()).orElseThrow();
        Assert.assertEquals(retrievedDonation, createdDonation);
    }

    @Test
    public void getDonationsByUserId_returns_empty_list_with_invalid_id() {
        List<Donation> donations = sut.getDonationsByUserId(-1);

        Assert.assertTrue(donations.isEmpty());
    }

    @Test
    public void getDonationsByUserId_returns_correct_donations() {
        List<Donation> donations = sut.getDonationsByUserId(1);

        Assert.assertEquals(2, donations.size());
        Assert.assertEquals(List.of(DONATION_1, DONATION_4), donations);
    }

    @Test
    public void updateDonation_updates_a_donation() {
        UpdateDonationDto donationToUpdate = new UpdateDonationDto(
                1,
               2000,
                "Test Comment",
                true
        );

        Donation updatedDonation = sut.updateDonation(donationToUpdate, 1);
        Assert.assertNotNull(updatedDonation);

        Optional<Donation> retrievedDonation = sut.getDonationById(updatedDonation.getDonationId());
        Assert.assertEquals(retrievedDonation.orElseThrow(), updatedDonation);
    }

}
