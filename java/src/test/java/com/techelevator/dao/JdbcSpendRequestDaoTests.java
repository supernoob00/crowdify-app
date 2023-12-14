package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class JdbcSpendRequestDaoTests extends BaseDaoTests {
    private JdbcSpendRequestDao sut;
    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcSpendRequestDao(jdbcTemplate);
    }

    @Test
    public void getSpendRequestById_returns_a_spend_request_with_valid_id() {
        Assert.assertEquals(REQUEST_1, sut.getSpendRequestById(REQUEST_1.getId()).orElseThrow());
        Assert.assertEquals(REQUEST_2, sut.getSpendRequestById(REQUEST_2.getId()).orElseThrow());
    }

    @Test
    public void getSpendRequestById_returns_empty_when_given_invalid_request_id() {
        Optional<SpendRequest> affected = sut.getSpendRequestById(-1);
        Assert.assertTrue(affected.isEmpty());
    }

    @Test
    public void getSpendRequestsByCampaign_returns_a_spend_request_with_valid_id() {
        List<SpendRequest> returnedSpendReqsList = sut.getSpendRequestsByCampaign(1);
        Assert.assertEquals(REQUEST_1, returnedSpendReqsList.get(0));

        returnedSpendReqsList = sut.getSpendRequestsByCampaign(2);
        Assert.assertEquals(REQUEST_2, returnedSpendReqsList.get(0));
        Assert.assertEquals(REQUEST_3, returnedSpendReqsList.get(1));
    }

    @Test
    public void getSpendRequestsByCampaign_returns_empty_list_given_invalid_campaign_id() {
        List<SpendRequest> requestList = sut.getSpendRequestsByCampaign(-1);
        Assert.assertTrue(requestList.isEmpty());
    }

    @Test
    public void createSpendRequest_creates_new_spend_request() {
        NewSpendRequestDto newSpendRequest = new NewSpendRequestDto(
                1,
                "Test Name",
                1000,
                "Test Description",
                LocalDateTime.of(2024, 1, 25, 0, 0)
        );
        SpendRequest createdRequest = sut.createSpendRequest(newSpendRequest);
        Optional <SpendRequest> retrievedRequest = sut.getSpendRequestById(createdRequest.getId());
        Assert.assertTrue(retrievedRequest.isPresent());
        Assert.assertEquals(retrievedRequest.orElseThrow(), createdRequest);
    }

    @Test (expected = DaoException.class)
    public void createSpendRequest_throws_exception_given_invalid_data() {
        NewSpendRequestDto newSpendRequest = new NewSpendRequestDto();
        newSpendRequest.setCampaignId(1);
        newSpendRequest.setRequestName("Test Name");
        newSpendRequest.setAmount(-1000);
        newSpendRequest.setDescription("Test Description");
        newSpendRequest.setEndDate(LocalDateTime.of(2021,12,15,0,0));
        sut.createSpendRequest(newSpendRequest);
    }

    @Test
    public void updateSpendRequest_updates_a_spend_request() {
        UpdateSpendRequestDto requestToUpdate = new UpdateSpendRequestDto(
                3,
                1000,
                "Mo Money!",
                true,
                LocalDateTime.of(2024, 2, 4, 0, 0)
        );

        SpendRequest updatedRequest = sut.updateSpendRequest(requestToUpdate, 3);
        Assert.assertNotNull(updatedRequest);

        Optional<SpendRequest> retrievedRequest = sut.getSpendRequestById(updatedRequest.getId());
        Assert.assertEquals(retrievedRequest.orElseThrow(), updatedRequest);
    }

    @Test(expected = DaoException.class)
    public void updateSpendRequest_throws_exception_given_invalid_data() {
        UpdateSpendRequestDto requestToUpdate = new UpdateSpendRequestDto();
        requestToUpdate.setAmount(-1000);
        requestToUpdate.setDescription("Test Description");
        requestToUpdate.setApproved(true);
        requestToUpdate.setEndDate(LocalDateTime.of(2024, 2, 4, 0, 0));

        sut.updateSpendRequest(requestToUpdate, 2);
    }

    @Test
    public void deleteSpendRequestById_deletes_a_spend_request_given_valid_request_id() {
        boolean affected = sut.deleteSpendRequestById(CAMPAIGN_3.getId());
        Assert.assertTrue(affected);
    }

    @Test (expected = DaoException.class)
    public void deleteSpendRequestById_throws_exception_when_spend_request_has_unrefunded_donations() {
        boolean affected = sut.deleteSpendRequestById(REQUEST_2.getId());
        Assert.assertFalse(affected);
    }
}
