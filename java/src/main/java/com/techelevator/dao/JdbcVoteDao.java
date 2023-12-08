package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Vote;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcVoteDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcVoteDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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

    //TODO: needs finished
//    public Vote createVote(Vote vote) {
//        String sql = "INSERT into vote (donor_id, request_id, vote_approved) VALUES (?, ?, ?);";
//
//    }

    // TODO: finish this - also cannot change vote if associated spend
    //  request is closed
    public int changeVote(int voteId, boolean newValue) {
        return 0;
    }

    private Vote mapRowtoVote(SqlRowSet rowSet) {
        Vote vote = new Vote();

        vote.setUserId(rowSet.getInt("donor_id"));
        vote.setRequestId(rowSet.getInt("request_id"));
        vote.setApproved(rowSet.getBoolean("vote_approved"));

    return vote;
    }
}
