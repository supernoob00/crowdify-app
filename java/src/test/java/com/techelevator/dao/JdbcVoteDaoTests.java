package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.NewVoteDto;
import com.techelevator.model.UpdateVoteDto;
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

    @Test (expected = DaoException.class)
    public void createSpendRequest_throws_exception_given_invalid_data() {
        NewVoteDto voteToCreate = new NewVoteDto();
        voteToCreate.setDonorId(-1);
        voteToCreate.setRequestId(-1);
        voteToCreate.setVoteApproved(false);
        sut.createVote(voteToCreate);
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
    public void getVoteByDonorAndRequestId_returns_empty_given_invalid_data() {
        Optional<Vote> vote = sut.getVoteByDonorAndRequestId(-1,-1);
        Assert.assertTrue(vote.isEmpty());
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

    @Test
    public void changeVote_changes_vote() {
        UpdateVoteDto voteToChange = new UpdateVoteDto(
                4,
                2,
                false
        );
        Vote updatedVote = sut.changeVote(voteToChange);
        Assert.assertNotNull(updatedVote);

        Optional<Vote> retrievedVote = sut.getVoteByDonorAndRequestId(USER_4.getId(), REQUEST_2.getId());
        Assert.assertEquals(retrievedVote.orElseThrow(), updatedVote);
    }

    @Test (expected = Exception.class)
    public void changeVote_throws_exception_given_invalid_data() {
        UpdateVoteDto voteToChange = new UpdateVoteDto();
        voteToChange.setApproved(false);
        voteToChange.setUserId(-1);
        voteToChange.setRequestId(-1);

        sut.changeVote(voteToChange);
    }

    @Test
    public void deleteVoteById_deletes_vote_given_a_valid_donor_id() {
        boolean affected = sut.deleteVoteById(USER_4.getId(), REQUEST_2.getId());
        Assert.assertTrue(affected);

        Optional<Vote> deletedVote = sut.getVoteByDonorAndRequestId(4,2);
        Assert.assertTrue(deletedVote.isEmpty());
    }

    @Test
    public void deleteVoteById_fails_when_given_invalid_donor_and_request_id() {
        boolean affected = sut.deleteVoteById(-1, -1);
        Assert.assertFalse(affected);
    }

    @Test
    public void deleteVoteBySpendRequestId_deletes_votes_given_a_valid_request_id() {
        boolean affected = sut.deleteVoteBySpendRequestId(REQUEST_2.getId());
        Assert.assertTrue(affected);

        List<Vote> deletedVotes = sut.getVotesBySpendRequestId(REQUEST_2.getId());
        Assert.assertTrue(deletedVotes.isEmpty());
    }
}
