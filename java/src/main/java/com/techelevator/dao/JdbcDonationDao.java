package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Donation;
import com.techelevator.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcDonationDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserDao userDao;

    public JdbcDonationDao(JdbcTemplate jdbcTemplate, UserDao userDao) {
        this.userDao = userDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Donation> getDonationsByUserId(int userId) {
        List<Donation> donationList = new ArrayList<>();
        String sql = "SELECT * FROM donation WHERE donor_id = ?;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            while (results.next()) {
                donationList.add(mapRowToDonation(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return donationList;
    }

    public List<Donation> getDonationsByCampaignId(int campaignId) {
        List<Donation> donationList = new ArrayList<>();
        String sql = "SELECT * from donation WHERE campaign_id = ?;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, campaignId);
            while (results.next()) {
                donationList.add(mapRowToDonation(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return donationList;
    }

    public Donation createDonation(@NotNull Donation donationToCreate) {
        String sql = "INSERT into donation " +
                "(donor_id, campaign_id, donation_amount, donation_date, donation_comment, donation_status " +
                "values (?,?,?,?,?,?) returning donation_id;";

        try {
            // TODO: we shouldn't be putting date in
            int donationId = jdbcTemplate.queryForObject(sql, Integer.class,
                    // TODO same question about donor id
                    donationToCreate.getDonor().getId(),
                    donationToCreate.getCampaignId(),
                    donationToCreate.getAmount(),
                    donationToCreate.getDate(),
                    donationToCreate.getComment(),
                    donationToCreate.getStatus());
            return getDonationById(donationId).orElseThrow();
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    public Optional<Donation> getDonationById(int id) {
        String sql = "SELECT * FROM donation WHERE donation_id = ?;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);

            if (results.next()) {
                Donation retrievedDonation = mapRowToDonation(results);
                return Optional.of(retrievedDonation);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return Optional.empty();
    }


    public Donation mapRowToDonation(SqlRowSet results) {
        Donation donation = new Donation();
        User donor = userDao.getUserById(results.getInt("donor_id")).orElseThrow();

        donation.setDonationId(results.getInt("donation_id"));
        donation.setDonor(donor);
        donation.setCampaignId(results.getInt("campaign_id"));
        donation.setAmount(results.getInt("donation_amount"));
        donation.setDate(results.getTimestamp("donation_date").toLocalDateTime());
        donation.setComment(results.getString("donation_comment"));
        donation.setStatus(results.getString("donation_status"));

        return donation;
    }
}