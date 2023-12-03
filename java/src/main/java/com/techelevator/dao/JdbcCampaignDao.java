package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Campaign;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcCampaignDao {
    private final JdbcTemplate jdbcTemplate;
    private final JdbcDonationDao jdbcDonationDao;

    public JdbcCampaignDao(JdbcTemplate jdbcTemplate, JdbcDonationDao jdbcDonationDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcDonationDao = jdbcDonationDao;
    }

    public List<Campaign> getCampaignList() {
        List<Campaign> campaignList = new ArrayList<>();
        String sql = "SELECT * FROM campaign;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                campaignList.add(mapRowtoCampaign(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return campaignList;
    }

    public Optional<Campaign> getCampaignById(int CampaignId) {
        String sql = "SELECT * FROM campaign WHERE campaign_id = ?;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, CampaignId);
            if (results.next()) {
                return Optional.of(mapRowtoCampaign(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return Optional.empty();
    }

    public Campaign createCampaign(@NotNull Campaign campaign) {
        Campaign newCampaign;

        // TODO do we also need to insert default values for the booleans? what happens if they're not met?
        //  maybe public/private doesn't need a default?

        String sql = "INSERT into campaign (campaign_name, description, funding_goal, start_date, end_date) " +
                "values(?,?,?,?,?) returning campaign_id;";

        try {
            int campaignId = jdbcTemplate.queryForObject(sql, int.class,
                    campaign.getName(),
                    campaign.getDescription(),
                    campaign.getFundingGoal(),
                    campaign.getStartDate(),
                    campaign.getEndDate());

            newCampaign = getCampaignById(campaignId).get();

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return newCampaign;
    }

    public void deleteCampaignById (int campaignId) {

//TODO do we want managers to be able to delete campaigns entirely, or should they remain in the database
// as deleted but hidden from the front end?
    }

    private Campaign mapRowtoCampaign(SqlRowSet rowSet) {
        Campaign campaign = new Campaign();
        campaign.setId(rowSet.getInt("campaign_id"));
        campaign.setName(rowSet.getString("campaign_name"));
        campaign.setDescription(rowSet.getString("description"));
        campaign.setFundingGoal(rowSet.getInt("funding_goal"));
        campaign.setStartDate(rowSet.getTimestamp("start_date").toLocalDateTime());
        campaign.setEndDate(rowSet.getTimestamp("end_date").toLocalDateTime());
        campaign.setLocked(rowSet.getBoolean("locked"));
        campaign.setPublic(rowSet.getBoolean("public"));
        campaign.setDonations(jdbcDonationDao.getDonationList(rowSet.getInt("campaign_id")));
        //TODO: add list of managers
        return campaign;
    }
}
