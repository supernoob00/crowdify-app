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
    public void getDonationsByCampaignId_returns_all_donations_for_valid_campaign_id() {
        List<Donation> donations = sut.getDonationsByCampaignId(CAMPAIGN_1.getId());

        Assert.assertNotNull(donations);
        Assert.assertEquals(2, donations.size());
        Assert.assertEquals(DONATION_1, donations.get(0));
        Assert.assertEquals(DONATION_5, donations.get(1));
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

    @Test (expected = DaoException.class)
    public void updateDonation_throws_exception_given_invalid_data() {
        UpdateDonationDto donation = new UpdateDonationDto(
                1,
                -1000,
                "Test",
                false
        );
        Optional<Donation> updatedDonation = Optional.ofNullable(sut.updateDonation(donation, 1));
        Assert.assertTrue(updatedDonation.isEmpty());

    }

    @Test
    public void refundDonations_sets_all_donations_to_refunded_given_valid_campaign_id() {
        List<Donation> expected = sut.getDonationsByCampaignId(CAMPAIGN_1.getId());

        for (Donation donation : expected) {
            donation.setRefunded(true);
        }
        sut.refundDonations(CAMPAIGN_1.getId());
        List<Donation> actual = sut.getDonationsByCampaignId(CAMPAIGN_1.getId());

        Assert.assertEquals(expected, actual);
    }

}
