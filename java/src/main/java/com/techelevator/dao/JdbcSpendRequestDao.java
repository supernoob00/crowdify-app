package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Campaign;
import com.techelevator.model.NewSpendRequestDto;
import com.techelevator.model.SpendRequest;
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
public class JdbcSpendRequestDao {
    private final JdbcTemplate jdbcTemplate;
    private final JdbcCampaignDao jdbcCampaignDao;

    public JdbcSpendRequestDao(JdbcTemplate jdbcTemplate, JdbcCampaignDao jdbcCampaignDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcCampaignDao = jdbcCampaignDao;
    }

    public Optional<SpendRequest> getSpendRequestById(int id) {
        String sql = "SELECT * FROM spend_request WHERE request_id = ?;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                return Optional.of(mapRowToSpendRequest(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return Optional.empty();
    }

    public List<SpendRequest> getSpendRequestsByCampaign(int campaignId) {
        List<SpendRequest> requestList = new ArrayList<>();
        // String sql = "SELECT * FROM spend_request WHERE donor_id = ?;";

        String sql = "SELECT spend_request.* " +
                "FROM spend_request " +
                "JOIN campaign USING (campaign_id) " +
                "WHERE campaign_id = ?;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, campaignId);
            while (results.next()) {
                requestList.add(mapRowToSpendRequest(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return requestList;
    }

    public SpendRequest createSpendRequest(@NotNull NewSpendRequestDto newSpendRequestDto) {
        String sql = "INSERT into spend_request " +
                "(campaign_id, request_amount, request_description, end_date) " +
                "values (?,?,?,?) returning request_id;";
        try {
            // TODO: date needs to be converted to SQL compatible format
            int requestId = jdbcTemplate.queryForObject(sql, Integer.class,
                    newSpendRequestDto.getCampaignId(),
                    newSpendRequestDto.getAmount(),
                    newSpendRequestDto.getDescription(),
                    newSpendRequestDto.getEndDate());
            return getSpendRequestById(requestId).orElseThrow();
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }



    public SpendRequest mapRowToSpendRequest(SqlRowSet rowSet) {
        SpendRequest request = new SpendRequest();

        request.setId(rowSet.getInt("request_id"));
        request.setCampaignId(rowSet.getInt("campaign_id"));
        request.setAmount(rowSet.getInt("request_amount"));
        request.setDescription(rowSet.getString("request_description"));
        request.setApproved(rowSet.getBoolean("request_approved"));

        if (rowSet.getTimestamp("end_date") != null) {
            request.setEndDate(rowSet.getTimestamp("end_date").toLocalDateTime());
        }
        return request;
    }
}
