package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Donation;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

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

    public Donation createDonation(Donation donationToCreate) {
        String sql = "INSERT into donation " +
                "(donor_id, campaign_id, donation_amount, donation_date, donation_comment, donation_status " +
                "values (?,?,?,?,?,?) returning donation_id;";

        try {
            int donationId = jdbcTemplate.queryForObject(sql, Integer.class,
                    // TODO same question about donor id
                    donationToCreate.getDonor().getId(),
                    donationToCreate.getCampaignId(),
                    donationToCreate.getAmount(),
                    donationToCreate.getDate(),
                    donationToCreate.getComment(),
                    donationToCreate.getStatus());
            return getDonationById(donationId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }


  /*





*/

    // get donation by Id

    public Donation getDonationById(int id) {
        String sql = "SELECT * FROM donation WHERE donation_id = ?;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);

            if (results.next()) {
                Donation retrievedDonation = mapRowToTransfer(results);
                return retrievedDonation;
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return null;

    }


    public Donation mapRowToTransfer(SqlRowSet results) {
        Donation donation = new Donation();

        donation.setDonationId(results.getInt("donation_id"));
        // donor id?
        donation.setCampaignId(results.getInt("campaign_id"));
        donation.setAmount(results.getInt("donation_amount"));
        donation.setDate(results.getTimestamp("donation_date").toLocalDateTime());
        donation.setComment(results.getString("donation_comment"));
        donation.setStatus(results.getString("donation_status"));

        return donation;
    }
}