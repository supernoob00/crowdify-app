package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Campaign;
import com.techelevator.model.NewCampaignDto;
import com.techelevator.model.UpdateCampaignDto;
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
public class JdbcCampaignDao {
    private final JdbcTemplate jdbcTemplate;
    private final JdbcDonationDao jdbcDonationDao;
    private final UserDao userDao;

    public JdbcCampaignDao(JdbcTemplate jdbcTemplate, JdbcDonationDao jdbcDonationDao, UserDao userDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcDonationDao = jdbcDonationDao;
        this.userDao = userDao;
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

    // TODO: insert campaign creator into database as well
    public Campaign createCampaign(@NotNull NewCampaignDto newCampaignDto) {
        Campaign createdCampaign;
        String sql = "INSERT INTO campaign (campaign_name, description, funding_goal, start_date, end_date, public) " +
                "VALUES (?,?,?,?,?,?) returning campaign_id;";
        try {
            Integer newCampaignId = jdbcTemplate.queryForObject(sql, int.class,
                    newCampaignDto.getName(),
                    newCampaignDto.getDescription(),
                    newCampaignDto.getFundingGoal(),
                    newCampaignDto.getStartDate(),
                    newCampaignDto.getEndDate(),
                    newCampaignDto.isPublic());
            if (newCampaignId == null) {
                throw new DaoException("Failed to create campaign");
            }
            linkCampaignManager(newCampaignId, newCampaignDto.getCreatorId(), true);
            createdCampaign = getCampaignById(newCampaignId).get();

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return createdCampaign;
    }

    public Optional<Campaign> updateCampaign(UpdateCampaignDto updateCampaignDto) {
        String sql = "UPDATE campaign SET " +
                "campaign_name = ?, " +
                "description = ?, " +
                "funding_goal = ?, " +
                "start_date = ?, " +
                "end_date = ?, " +
                "public = ?, " +
                "locked = ? WHERE campaign_id = ?";
        try {
            jdbcTemplate.update(sql,
                    updateCampaignDto.getName(),
                    updateCampaignDto.getDescription(),
                    updateCampaignDto.getFundingGoal(),
                    updateCampaignDto.getStartDate(),
                    updateCampaignDto.getEndDate(),
                    updateCampaignDto.isPublic(),
                    updateCampaignDto.isLocked(),
                    updateCampaignDto.getId());

            return getCampaignById(updateCampaignDto.getId());

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    public void deleteCampaignById(int campaignId) {
        //TODO do we want managers to be able to delete campaigns entirely, or should they remain in the database
        // as deleted but hidden from the front end?


    }

    private int linkCampaignManager(int campaignId, int managerId, boolean isCreator) {
        String sql = "INSERT INTO campaign_manager (campaign_id, manager_id, creator) " +
                "VALUES (?,?,?)";
        try {
            return jdbcTemplate.update(sql, campaignId, managerId, isCreator);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
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
        campaign.setDonations(jdbcDonationDao.getDonationsByCampaignId(rowSet.getInt("campaign_id")));
        campaign.setManagers(userDao.getManagersByCampaignId(rowSet.getInt("campaign_id")));
        campaign.setCreator(userDao.getCreatorByCampaignId(rowSet.getInt("campaign_id")).orElseThrow());
        return campaign;
    }
}
