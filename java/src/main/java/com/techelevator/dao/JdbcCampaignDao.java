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

    public JdbcCampaignDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Campaign> getCampaignList() {
        List<Campaign> campaignList = new ArrayList<>();
        String sql = "SELECT * FROM campaign WHERE deleted = false;";

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

    public List<Campaign> getCampaignsByManagerId(int userId) {
        List<Campaign> managersCampaigns = new ArrayList<>();

        String sql = "SELECT * FROM campaign_manager INNER JOIN campaign using (campaign_id)" +
                "WHERE manager_id = ? and deleted = false;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            while (results.next()) {
                Campaign campaign = mapRowtoCampaign(results);
                managersCampaigns.add(campaign);
            }
                return managersCampaigns;

            } catch (CannotGetJdbcConnectionException e) {
                throw new DaoException("Unable to connect to server or database", e);
        }
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

    public Optional<String> getCampaignNameById(int CampaignId) {
        String sql = "SELECT campaign_name FROM campaign WHERE campaign_id = " +
                "?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, CampaignId);
            if (results.next()) {
                return Optional.of(results.getString("campaign_name"));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return Optional.empty();
    }

    public List<Campaign> getCampaignsByDonorId (int id) {
        List<Campaign> campaignList = new ArrayList<>();
        String sql = "SELECT * from " +
                "campaign " +
                "JOIN donation using (campaign_id) " +
                "JOIN users on donor_id = user_id " +
                "WHERE user_id = ? and deleted = false;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            while (results.next()) {
                campaignList.add(mapRowtoCampaign(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return campaignList;
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
                "end_date = ?, " +
                "public = ?, " +
                "locked = ? WHERE campaign_id = ?";
        try {
            jdbcTemplate.update(sql,
                    updateCampaignDto.getName(),
                    updateCampaignDto.getDescription(),
                    updateCampaignDto.getFundingGoal(),
                    updateCampaignDto.getEndDate(),
                    updateCampaignDto.isPublic(),
                    updateCampaignDto.isLocked(),
                    updateCampaignDto.getCampaignId());

            System.out.println(updateCampaignDto);
            return getCampaignById(updateCampaignDto.getCampaignId());

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    public int markCampaignDeletedById(int campaignId) {
        Campaign markedCampaign = getCampaignById(campaignId).orElse(null);
        if (markedCampaign == null) {
            return 0;
        }

        String sql = "UPDATE campaign SET deleted = true WHERE campaign_id = " +
                "?;";

        try {
            if (!markedCampaign.isLocked() || !markedCampaign.areAllDonationsRefunded()) {
                throw new DataIntegrityViolationException("Donation cannot be " +
                        "deleted because it is not locked or has unreturned " +
                        "donations.");
            }
            return jdbcTemplate.update(sql, campaignId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    private void linkCampaignManager(int campaignId, int managerId, boolean isCreator) {
        String sql = "INSERT INTO campaign_manager (campaign_id, manager_id, creator) " +
                "VALUES (?,?,?)";
        try {
            jdbcTemplate.update(sql, campaignId, managerId, isCreator);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
    }

    public int getTotalFunds(int campaignId) {
        JdbcDonationDao donationDao = new JdbcDonationDao(jdbcTemplate);
        JdbcSpendRequestDao spendRequestDao = new JdbcSpendRequestDao(jdbcTemplate);
        return donationDao.getDonationTotalByCampaignId(campaignId)
                - spendRequestDao.approvedTotalByCampaignId(campaignId);
    }

    private Campaign mapRowtoCampaign(SqlRowSet rowSet) {
        JdbcDonationDao jdbcDonationDao = new JdbcDonationDao(jdbcTemplate);
        JdbcUserDao userDao = new JdbcUserDao(jdbcTemplate);

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
        campaign.setDeleted(rowSet.getBoolean("deleted"));
        return campaign;
    }
}
