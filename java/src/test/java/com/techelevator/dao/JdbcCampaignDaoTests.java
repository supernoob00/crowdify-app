package com.techelevator.dao;

import com.techelevator.exception.DaoException;
import com.techelevator.model.Campaign;
import com.techelevator.model.NewCampaignDto;
import com.techelevator.model.UpdateCampaignDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.fail;

public class JdbcCampaignDaoTests extends BaseDaoTests {
    private JdbcCampaignDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        JdbcDonationDao jdbcDonationDao = new JdbcDonationDao(
                jdbcTemplate);
        JdbcUserDao jdbcUserDao = new JdbcUserDao(jdbcTemplate);
        sut = new JdbcCampaignDao(jdbcTemplate);
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
        sut.markCampaignDeletedById(1);
    }

    @Test
    public void delete_campaign_removes_campaign() {
        int affected = sut.markCampaignDeletedById(4);
        Assert.assertEquals(1, affected);

        Campaign markedAsDeleted = sut.getCampaignById(CAMPAIGN_4.getId()).orElseThrow();
        Assert.assertTrue(markedAsDeleted.isDeleted());
    }

    @Test(expected = DaoException.class)
    public void markCampaignDeletedById_fails_to_delete_campaign_with_unrefunded_donations() {
        int expected = sut.markCampaignDeletedById(CAMPAIGN_1.getId());
        Assert.assertEquals(expected, sut.getCampaignById(CAMPAIGN_1.getId()));
    }


    @Test
    public void getManagersCampaignsList_returns_empty_list_with_invalid_id() {
        List<Campaign> myCampaigns = sut.getCampaignsByManagerId(-1);
        Assert.assertTrue(myCampaigns.isEmpty());
    }

    @Test
    public void getManagersCampaignsList_returns_accurate_list() {
        List<Campaign> myCampaigns = sut.getCampaignsByManagerId(2);
        Assert.assertEquals(CAMPAIGN_2, myCampaigns.get(0));

        List<Campaign> myCampaigns2 = sut.getCampaignsByManagerId(1);
        Assert.assertEquals(CAMPAIGN_1, myCampaigns2.get(0));
//        Assert.assertEquals(CAMPAIGN_4, myCampaigns2.get(1));

    }

    @Test
    public void updateCampaign_updates_a_campaign() {
        UpdateCampaignDto campaignToUpdate = new UpdateCampaignDto(
                3,
                "Test Name",
                "Test Description",
                5000000,
                LocalDateTime.of(2024, 1, 8, 0, 0),
                LocalDateTime.of(2024, 1, 18, 0, 0),
                false,
                true
        );


        Optional<Campaign> updatedCampaign = sut.updateCampaign(campaignToUpdate);
        Assert.assertNotNull(updatedCampaign);
        Assert.assertTrue(updatedCampaign.isPresent());

        Optional<Campaign> retrievedCampaign = sut.getCampaignById(updatedCampaign.get().getId());
        Assert.assertEquals(retrievedCampaign.orElseThrow(), updatedCampaign.orElseThrow());
    }

    @Test(expected = DaoException.class)
    public void updateCampaign_does_not_update_given_given_invalid_data() {
        UpdateCampaignDto campaignToUpdate = new UpdateCampaignDto(
                3,
                "",
                "",
                -5000000,
                LocalDateTime.of(2022, 1, 8, 0, 0),
                LocalDateTime.of(2021, 1, 18, 0, 0),
                true,
                false
        );

        Optional<Campaign> updatedCampaign = sut.updateCampaign(campaignToUpdate);

        Assert.assertNotEquals(updatedCampaign.orElseThrow(), CAMPAIGN_3);
    }

    @Test
    public void getCampaignsByDonorId_returns_a_list_of_campaigns_given_a_valid_id() {
        //Non manager, one donation
        List<Campaign> myCampaigns = sut.getCampaignsByDonorId(USER_4.getId());
        Assert.assertEquals(CAMPAIGN_2, myCampaigns.get(0));

        //Non manager, multiple donations
        List<Campaign> myCampaigns2 = sut.getCampaignsByDonorId(USER_5.getId());
        Assert.assertEquals(CAMPAIGN_1, myCampaigns2.get(0));
        Assert.assertEquals(CAMPAIGN_3, myCampaigns2.get(1));

        //Manager, one donation
        List<Campaign> myCampaigns3 = sut.getCampaignsByDonorId(USER_2.getId());
        Assert.assertEquals(CAMPAIGN_2, myCampaigns3.get(0));

        //Manager, multiple donations
        List<Campaign> myCampaigns4 = sut.getCampaignsByDonorId(USER_1.getId());
        Assert.assertEquals(CAMPAIGN_1, myCampaigns4.get(0));
        Assert.assertEquals(CAMPAIGN_2, myCampaigns4.get(1));
    }

    @Test
    public void getTotalFunds_returns_sum_of_donations_given_valid_id() {
        int sumCampaign1 = sut.getTotalFunds(CAMPAIGN_1.getId());
        Assert.assertEquals(CAMPAIGN_1.getDonationTotal(), sumCampaign1);

        int sumCampaign2 = sut.getTotalFunds(CAMPAIGN_2.getId());
        Assert.assertEquals(CAMPAIGN_2.getDonationTotal(), sumCampaign2);
    }

//    @Test
//    public void linkCampaignManager_sets_campaign_manager_given_valid_campaign_and_manager_id() {
//        NewCampaignDto expected = new NewCampaignDto(
//                "Test Name",
//                "Test Description",
//                3000,
//                5,
//                LocalDateTime.of(2023,12,24,0,0),
//                LocalDateTime.of(2024,1,24,0,0),
//                true
//        );
//        Campaign createdCampaign = sut.createCampaign(expected);
//        linkCampaignManager
//    }
}
