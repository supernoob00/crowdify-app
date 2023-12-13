package com.techelevator.controller;

import com.techelevator.dao.JdbcCampaignDao;
import com.techelevator.dao.JdbcUserDao;
import com.techelevator.dao.JdbcVoteDao;
import com.techelevator.model.Campaign;
import com.techelevator.model.Vote;
import com.techelevator.service.ChartService;
import com.techelevator.servicemodel.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;




@RestController
@CrossOrigin
public class ChartController {
    public static final String APPROVE_VOTE_COLOR = "rgb(19, 240, 41)";
    public static final String REJECT_VOTE_COLOR = "rgb(242, 1, 1)";
    public static final String NO_VOTE_COLOR = "rgb(111,110,110)";
    public static final List<String> COLOR_LIST =
            new ArrayList<>(Arrays.asList(APPROVE_VOTE_COLOR, REJECT_VOTE_COLOR));
    public static final String PIE_CHART = "pie";
    public static final List<String> VOTE_LABELS = new ArrayList<>(Arrays.asList("Yes", "No"));
    public static final List<String> NO_VOTE_LABEL = new ArrayList<>(Arrays.asList("No Votes"));

    private final JdbcVoteDao jdbcVoteDao;
    private final JdbcCampaignDao jdbcCampaignDao;

    public ChartController(JdbcVoteDao jdbcVoteDao, JdbcCampaignDao jdbcCampaignDao) {
        this.jdbcVoteDao = jdbcVoteDao;
        this.jdbcCampaignDao = jdbcCampaignDao;
    }

    @GetMapping(value = "/spend-requests/{requestId}/chart", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public byte[] getChartByVotes(@PathVariable int requestId) {
        //  create the datalist
        List<Vote> voteList = jdbcVoteDao.getVotesBySpendRequestId(requestId);

        List<String> colorList = new ArrayList<>();

        if (!voteList.isEmpty()) {
            colorList.add(APPROVE_VOTE_COLOR);
            colorList.add(REJECT_VOTE_COLOR);

        } else {
            colorList.add(NO_VOTE_COLOR);
        }

        List<Integer> voteValues = getVoteData(voteList);

        String label = "Spend Request Votes";
        List<DataSet> dataSets = List.of(new DataSet(voteValues, colorList, label));

        // create the data
        Data data;
        if (voteList.isEmpty()) {
            data = new Data(dataSets, NO_VOTE_LABEL);
        } else {
            data = new Data(dataSets, VOTE_LABELS);
        }

        // create the chart
        Chart chart = new Chart(PIE_CHART, data);// create the chart object

       /* if (voteList.isEmpty()) {


        }*/
       /*     ChartScales scales = new ChartScales();
            scales.setyAxes(List.of(new AxisDisplay(false)));
            scales.setxAxes(List.of(new AxisDisplay(true)));

            ChartOptions options = new ChartOptions(scales);
            chart.setOptions(options);
        }*/

        ChartObject chartObject = new ChartObject(
                "2",
                "transparent",
                "200",
                "200",
                1.0,
                "png", chart);

        ChartService chartService = new ChartService();
        return chartService.getChartImg(chartObject);
    }

//    @GetMapping(value = "/campaigns/{campaignId}/chart", produces = MediaType.IMAGE_PNG_VALUE)
//    @ResponseStatus(HttpStatus.CREATED)
//    public byte[] getLineChartByCampaign(@PathVariable int campaignId) {
//        Campaign campaign =
//        List<Integer> xAxis = new ArrayList<>();
//        int numOfDays;
//
//        if ()
//
//        for ()
//    }

    private List<Integer> getVoteData (List<Vote>voteList) {
        List<Integer> newVoteList = new ArrayList<>();

        if (voteList.isEmpty()) {
            newVoteList.add(1);
        } else {
            int yesVotes = 0;
            int noVotes = 0;

            for (Vote vote : voteList) {
                if (vote.isApproved()) {
                    yesVotes++;
                } else {
                    noVotes++;
                }
            }
            newVoteList.add(yesVotes);
            newVoteList.add(noVotes);
        }
        return newVoteList;
    }
}
