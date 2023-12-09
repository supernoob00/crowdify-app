package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcVoteDao {

    private final JdbcTemplate jdbcTemplate;
    private final UserDao userDao;
    private final JdbcSpendRequestDao jdbcSpendRequestDao;

    public JdbcVoteDao(JdbcTemplate jdbcTemplate, UserDao userDao, JdbcSpendRequestDao jdbcSpendRequestDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDao = userDao;
        this.jdbcSpendRequestDao = jdbcSpendRequestDao;
    }

    // get votes by spend request id
    public List<Vote> getVotesBySpendRequestId(int spendReqId) {
        List<Vote> voteList = new ArrayList<>();
        String sql = "SELECT vote.* " +
                "from spend_request " +
                "JOIN vote USING(request_id) " +
                "WHERE request_id = ? ;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, spendReqId);
            while (results.next()) {
                voteList.add(mapRowtoVote(results));
            }
        } catch(CannotGetJdbcConnectionException e){
            throw new DaoException("Unable to connect to server or database", e);
        }
        return voteList;
    }

    public List<Vote> getVotesById(int voterId) {
        List<Vote> voteList = new ArrayList<>();

        String sql = "SELECT vote.*, users.username " +
                "FROM vote JOIN users on vote.donor_id = users.user_id " +
                "WHERE donor_id = ?;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, voterId);
            while (results.next()) {
                voteList.add(mapRowtoVote(results));
            }
        } catch(CannotGetJdbcConnectionException e){
        throw new DaoException("Unable to connect to server or database", e);
    }
        return voteList;
    }

    public List<Vote> getVotesByCampaignId(int campaignId) {
        List<Vote> campaignVotes = new ArrayList<>();

        String sql = "SELECT vote.* from vote " +
                "INNER JOIN spend_request using (request_id)" +
                "WHERE campaign_id = ?;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, campaignId);

            while (results.next()) {
                campaignVotes.add(mapRowtoVote(results));
            }
        }
        catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return campaignVotes;
    }

    public Vote createVote(@NotNull NewVoteDto newVoteDto) {
        Vote newVote;
        String sql = "INSERT into vote (donor_id, request_id, vote_approved) " +
                "VALUES (?, ?, ?) RETURNING donor_id";

        try {
            Integer newVoteId = jdbcTemplate.queryForObject(sql, Integer.class,
                    newVoteDto.getDonorId(),
                    newVoteDto.getRequestId(),
                    newVoteDto.isVoteApproved()
            );
            if (newVoteId == null || newVoteId == 0) {
                System.out.println(newVoteId);
                throw new DaoException("Failed to cast vote.");
            }
            newVote = getVoteByDonorAndRequestId(newVoteDto.getDonorId(), newVoteDto.getRequestId())
                    .orElseThrow();

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return newVote;
    }

    public Optional<Vote> getVoteByDonorAndRequestId(int donorId, int requestId) {
        Vote thisVote;
        String sql = "SELECT * FROM vote WHERE donor_id = ? and request_id = ?;";

        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, donorId, requestId);

        if (result.next()) {
            return Optional.of(mapRowtoVote(result));

        }
        }catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return Optional.empty();
    }


    // TODO: finish this - also cannot change vote if associated spend
    //  request is closed
    public Vote changeVote(UpdateVoteDto updateVoteDto) {

        String sql = "UPDATE vote SET " +
                "vote_approved = ? " +
                "WHERE donor_id = ? AND request_id = ?;";

        try {
            jdbcTemplate.update(sql,
                    updateVoteDto.isApproved(),
                    updateVoteDto.getUserId(),
                    updateVoteDto.getRequestId());

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return getVoteByDonorAndRequestId(updateVoteDto.getUserId(), updateVoteDto.getRequestId()).orElseThrow();
    }

    private Vote mapRowtoVote(SqlRowSet rowSet) {
        Vote vote = new Vote();

        vote.setUserId(rowSet.getInt("donor_id"));
        vote.setRequestId(rowSet.getInt("request_id"));
        vote.setApproved(rowSet.getBoolean("vote_approved"));

    return vote;
    }
}
