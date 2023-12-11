package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class JdbcDonationDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcDonationDao(JdbcTemplate jdbcTemplate) {
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

    public Donation createDonation(@NotNull NewDonationDto newDonationDto) {
        String sql = "INSERT INTO donation " +
                "(donor_id, campaign_id, donation_amount, donation_comment, " +
                "anonymous) " +
                "VALUES (?,?,?,?,?) RETURNING donation_id;";
        try {
            Integer donationId = jdbcTemplate.queryForObject(sql, Integer.class,
                    newDonationDto.getDonorId(),
                    newDonationDto.getCampaignId(),
                    newDonationDto.getAmount(),
                    newDonationDto.getComment(),
                    newDonationDto.isAnonymous()
            );
            // TODO: print statement for testing purposes
            if (donationId == null || donationId == 0) {
                System.out.println(donationId);
                throw new DaoException("Failed to donate.");
            }
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

    public int getDonationTotalByCampaignId(int campaignId) {
        String sql = "SELECT SUM (donation_amount) FROM donation WHERE " +
                "NOT refunded AND campaign_id = ?;";
        try {
            return Objects.requireNonNull(
                    jdbcTemplate.queryForObject(sql, Integer.class, campaignId));
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
    }

    public Donation updateDonation(UpdateDonationDto updateDonationDto, int donationId) {

        String sql = "UPDATE donation SET " +
                "donation_amount = ?, " +
                "donation_comment = ?, " +
                "refunded = ? " +
                "WHERE donation_id = ?;";
        try {
            jdbcTemplate.update(sql,
                    updateDonationDto.getAmount(),
                    updateDonationDto.getComment(),
                    updateDonationDto.isRefunded(),
                    donationId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return getDonationById(donationId).orElseThrow();
    }

    public void refundDonations(int campaignId) {
        String sql = "UPDATE donation SET refunded = true WHERE campaign_id =" +
                " ?;";
        try {
            jdbcTemplate.update(sql, campaignId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    public Donation mapRowToDonation(SqlRowSet results) {
        JdbcUserDao userDao = new JdbcUserDao(jdbcTemplate);
        JdbcSpendRequestDao spendRequestDao = new JdbcSpendRequestDao(jdbcTemplate);

        Donation donation = new Donation();
        User donor = userDao.getUserById(results.getInt("donor_id")).orElse(null);

        donation.setDonationId(results.getInt("donation_id"));
        donation.setDonor(donor);

        JdbcCampaignDao campaignDao = new JdbcCampaignDao(jdbcTemplate);
        int campaignId = results.getInt("campaign_id");
        String campaignName = campaignDao.getCampaignNameById(campaignId).orElseThrow();

        donation.setCampaignId(campaignId);
        donation.setCampaignName(campaignName);

        donation.setAmount(results.getInt("donation_amount"));
        donation.setDate(results.getTimestamp("donation_date").toLocalDateTime());
        donation.setComment(results.getString("donation_comment"));
        donation.setRefunded(results.getBoolean("refunded"));
        donation.setAnonymous(results.getBoolean("anonymous"));
        return donation;
    }
}