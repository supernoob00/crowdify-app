package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.NewSpendRequestDto;
import com.techelevator.model.SpendRequest;
import com.techelevator.model.UpdateSpendRequestDto;
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
                "(campaign_id, request_name, request_amount, " +
                "request_description, end_date) " +
                "values (?,?,?,?,?) returning request_id;";
        try {
            // TODO: date needs to be converted to SQL compatible format
            int requestId = jdbcTemplate.queryForObject(sql, Integer.class,
                    newSpendRequestDto.getCampaignId(),
                    newSpendRequestDto.getRequestName(),
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

    public SpendRequest updateSpendRequest(UpdateSpendRequestDto updateSpendRequestDto, int spendRequestId) {
        String sql = "UPDATE spend_request SET " +
                "request_amount = ?, " +
                "request_description = ?, " +
                "request_approved = ?, " +
                "end_date = ? " +
                "WHERE request_id = ?;";

        try {
            jdbcTemplate.update(sql,
                    updateSpendRequestDto.getAmount(),
                    updateSpendRequestDto.getDescription(),
                    updateSpendRequestDto.isApproved(),
                    updateSpendRequestDto.getEndDate(),
                    spendRequestId);

            return getSpendRequestById(spendRequestId).orElseThrow();

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    public Integer approvedTotalByCampaignId(int campaignId) {
        String sql = "SELECT SUM (request_amount) FROM spend_request WHERE " +
                "campaign_id = ? AND request_approved";
        try {
            // should throw an NullPointerException if campaign id is invalid;
            // if needed, validating the campaign id should be done beforehand
            // by the caller using the getCampaignById() method
            return Objects.requireNonNullElse(jdbcTemplate.queryForObject(sql,
                    Integer.class, campaignId), 0);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
    }

    public boolean deleteSpendRequestById(int spendRequestId) {
        String sql = "DELETE FROM spend_request where request_id = ?;";
        try {
            int rowsAffected = jdbcTemplate.update(sql, spendRequestId);
            return rowsAffected != 0;
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
    }

    public SpendRequest mapRowToSpendRequest(SqlRowSet rowSet) {
        SpendRequest request = new SpendRequest();

        request.setId(rowSet.getInt("request_id"));
        request.setCampaignId(rowSet.getInt("campaign_id"));
        request.setRequestName(rowSet.getString("request_name"));
        request.setAmount(rowSet.getInt("request_amount"));
        request.setDescription(rowSet.getString("request_description"));
        request.setApproved(rowSet.getBoolean("request_approved"));

        if (rowSet.getTimestamp("end_date") != null) {
            request.setEndDate(rowSet.getTimestamp("end_date").toLocalDateTime());
        }
        return request;
    }
}
