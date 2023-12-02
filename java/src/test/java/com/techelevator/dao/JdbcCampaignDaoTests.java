package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Campaign;
import com.techelevator.model.RegisterUserDto;
import com.techelevator.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class JdbcCampaignDaoTests extends BaseDaoTests {
    private JdbcCampaignDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcCampaignDao(jdbcTemplate);
    }

    @Test
    public void getCampaignById_given_invalid_id_returns_null() {
        Campaign invalidCampaign = sut.getCampaignById(-1);
        Assert.assertNull(invalidCampaign);
    }

    @Test
    public void getCampaignById_given_valid_id_returns_campaign() {
        Assert.assertEquals(CAMPAIGN_1, sut.getCampaignById(1));
        Assert.assertEquals(CAMPAIGN_2, sut.getCampaignById(2));
        Assert.assertEquals(CAMPAIGN_3, sut.getCampaignById(3));
    }

    @Test
    public void getCampaigns_returns_all_campaigns() {
        List<Campaign> campaigns = sut.getCampaignList();

        Assert.assertNotNull(campaigns);
        Assert.assertEquals(3, campaigns.size());
        Assert.assertEquals(CAMPAIGN_1, campaigns.get(0));
        Assert.assertEquals(CAMPAIGN_2, campaigns.get(1));
        Assert.assertEquals(CAMPAIGN_3, campaigns.get(2));
    }

    @Test(expected = DaoException.class)
    public void createCampaign_with_null_name() {
        Campaign newCampaign = new Campaign();
        newCampaign.setName(null);
        sut.createCampaign(newCampaign);
    }

    @Test
    public void createCampaign_creates_a_campaign() {
        Campaign newCampaign = new Campaign();
        Campaign createdCampaign = sut.createCampaign(newCampaign);
        Assert.assertNotNull(createdCampaign);
        Campaign retrievedCampaign = sut.getCampaignById(createdCampaign.getId());
        Assert.assertEquals(retrievedCampaign, createdCampaign);
    }
}
