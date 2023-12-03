package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Donation;
import com.techelevator.model.SpendRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcSpendRequestDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcSpendRequestDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
        String sql = "SELECT * FROM spend_request WHERE donor_id = ?;";

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

    public SpendRequest createSpendRequest(@NotNull SpendRequest requestToCreate) {
        String sql = "INSERT into spend_request " +
                "(campaign_id, request_amount, request_description, " +
                "request_approved, end_date " +
                "values (?,?,?,?,?) returning request_id;";

        try {
            // TODO: date needs to be converted to SQL compatible format
            int requestId = jdbcTemplate.queryForObject(sql, Integer.class,
                    requestToCreate.getCampaign_id(),
                    requestToCreate.getAmount(),
                    requestToCreate.getDescription(),
                    requestToCreate.isApproved(),
                    requestToCreate.getEndDate());
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
        request.setCampaign_id(rowSet.getInt("campaign_id"));
        request.setAmount(rowSet.getInt("request_amount"));
        request.setDescription(rowSet.getString("request_description"));
        request.setApproved(rowSet.getBoolean("request_approved"));
        request.setEndDate(rowSet.getTimestamp("end_date").toLocalDateTime());
        return request;
    }
}
