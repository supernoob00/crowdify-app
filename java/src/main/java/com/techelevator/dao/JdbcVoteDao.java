package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Campaign;
import com.techelevator.model.Vote;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcVoteDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcVoteDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // get votes by spend request id
    public List<Vote> getVoteListBySpendReqId (int spendReqId) {

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

    // get votes by user id TK


    private Vote mapRowtoVote(SqlRowSet rowSet) {
        Vote vote = new Vote();

        vote.setUserId(rowSet.getInt("donor_id"));
        vote.setRequestId(rowSet.getInt("request_id"));
        vote.setApproved(rowSet.getBoolean("vote_approved"));

    return vote;
    }
}
