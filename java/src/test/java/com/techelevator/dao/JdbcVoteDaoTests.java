package com.techelevator.dao;

import com.techelevator.model.NewVoteDto;
import com.techelevator.model.Vote;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

public class JdbcVoteDaoTests extends BaseDaoTests{

    private JdbcVoteDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcVoteDao(jdbcTemplate);
    }

    @Test
    public void createVote_creates_new_vote() {
        NewVoteDto newVote = new NewVoteDto(5, 1, true);
        Vote createdVote = sut.createVote(newVote);
        Optional<Vote> retrievedVote = sut.getVoteByDonorAndRequestId(5,1);

        Assert.assertTrue(retrievedVote.isPresent());
        Assert.assertEquals(retrievedVote.orElseThrow(), createdVote);
    }

    @Test
    public void managers_of_campaigns_can_vote_for_spend_reqs_of_campaigns_they_do_not_manage() {
        Optional<Vote> vote = sut.getVoteByDonorAndRequestId(1,2);
        Assert.assertTrue(vote.isPresent());
    }

    @Test
    public void getVoteByDonorAndRequestId_returns_correct_vote() {
        Optional<Vote> vote = sut.getVoteByDonorAndRequestId(1,2);
        Assert.assertTrue(vote.isPresent());
        Assert.assertEquals(vote.get(), VOTE_1);
    }

    @Test
    public void getVotesBySpendRequest_returns_list_of_votes_given_valid_id() {
        List<Vote> votes = sut.getVotesBySpendRequestId(REQUEST_2.getId());

        Assert.assertEquals(2, votes.size());
        Assert.assertEquals(List.of(VOTE_1, VOTE_2), votes);
    }

    @Test
    public void getVotesById_returns_list_of_votes_for_valid_donor_id() {
        List<Vote> votes = sut.getVotesById(USER_1.getId());

        Assert.assertEquals(1, votes.size());
        Assert.assertEquals(List.of(VOTE_1), votes);
    }

    @Test
    public void getVotesByCampaignId_returns_list_of_votes_for_valid_campaign_id() {
        List<Vote> votes = sut.getVotesByCampaignId(CAMPAIGN_2.getId());

        Assert.assertEquals(2, votes.size());
        Assert.assertEquals(List.of(VOTE_1, VOTE_2), votes);
    }
}
