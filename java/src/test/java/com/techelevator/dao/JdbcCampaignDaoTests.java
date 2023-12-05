package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Campaign;
import com.techelevator.model.NewCampaignDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class JdbcCampaignDaoTests extends BaseDaoTests {
    private JdbcCampaignDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        JdbcDonationDao jdbcDonationDao = new JdbcDonationDao(
                jdbcTemplate, new JdbcUserDao(jdbcTemplate));
        JdbcUserDao jdbcUserDao = new JdbcUserDao(jdbcTemplate);
        sut = new JdbcCampaignDao(jdbcTemplate, jdbcDonationDao, jdbcUserDao);
    }

    @Test
    public void getCampaignById_given_invalid_id_returns_empty_optional() {
        Optional<Campaign> invalidCampaign = sut.getCampaignById(-1);
        Assert.assertTrue(invalidCampaign.isEmpty());
    }

    @Test
    public void getCampaignById_given_valid_id_returns_campaign() {
        Assert.assertEquals(CAMPAIGN_1, sut.getCampaignById(1).orElseThrow());
        Assert.assertEquals(CAMPAIGN_2, sut.getCampaignById(2).orElseThrow());
        Assert.assertEquals(CAMPAIGN_3, sut.getCampaignById(3).orElseThrow());
    }

    @Test
    public void getCampaigns_returns_all_campaigns() {
        List<Campaign> campaigns = sut.getCampaignList();

        Assert.assertNotNull(campaigns);
        Assert.assertEquals(3, campaigns.size());
        System.out.println(CAMPAIGN_1);
        System.out.println(campaigns.get(0));
        Assert.assertEquals(CAMPAIGN_1, campaigns.get(0));
        Assert.assertEquals(CAMPAIGN_2, campaigns.get(1));
        Assert.assertEquals(CAMPAIGN_3, campaigns.get(2));
    }

    @Test(expected = DaoException.class)
    public void createCampaign_with_null_values_throws_exception() {
        NewCampaignDto newCampaign = new NewCampaignDto();
        sut.createCampaign(newCampaign);
    }

    @Test
    public void createCampaign_creates_a_campaign() {
        NewCampaignDto newCampaign = new NewCampaignDto(
                "Test Campaign",
                "This is a test",
                6000,
                1,
                LocalDateTime.of(2023, 12, 2, 0, 0),
                LocalDateTime.of(2024, 12, 6, 0, 0),
                true);
        Campaign createdCampaign = sut.createCampaign(newCampaign);
        Optional<Campaign> retrievedCampaign = sut.getCampaignById(createdCampaign.getId());
        Assert.assertTrue(retrievedCampaign.isPresent());
        Assert.assertEquals(retrievedCampaign.orElseThrow(), createdCampaign);
    }

    @Test(expected = DaoException.class)
    public void delete_unlocked_campaign_fails() {
        sut.markedCampaignDeletedById(1);
    }

    @Test
    public void delete_campaign_removes_campaign() {
        int affected = sut.markedCampaignDeletedById(4);
        Assert.assertEquals(1, affected);
        Optional<Campaign> deleted = sut.getCampaignById(1);
        Assert.assertTrue(deleted.isEmpty());
    }
}
