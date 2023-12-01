package com.techelevator.dao;

import com.techelevator.model.Campaign;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class CampaignDao {

private final JdbcTemplate jdbcTemplate;

    public CampaignDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Campaign> getCampaignList () {
        List<Campaign> campaignList = new ArrayList<>();
        String sql = "SELECT * FROM "



    }
}
