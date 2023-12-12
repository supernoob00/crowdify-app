package com.techelevator.controller;

import com.techelevator.dao.JdbcUserDao;
import com.techelevator.dao.JdbcVoteDao;
import com.techelevator.model.Vote;
import com.techelevator.service.ChartService;
import com.techelevator.servicemodel.Chart;
import com.techelevator.servicemodel.ChartObject;
import com.techelevator.servicemodel.Data;
import com.techelevator.servicemodel.DataSet;
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

    private final JdbcVoteDao jdbcVoteDao;
    public static final String APPROVE_VOTE_COLOR = "rgb(19, 240, 41)";
    public static final String REJECT_VOTE_COLOR = "rgb(242, 1, 1)";
    public static final String NO_VOTE_COLOR = "rgb(164, 159, 159)";
    public static final List<String> COLORLIST = new ArrayList<>(Arrays.asList(APPROVE_VOTE_COLOR, REJECT_VOTE_COLOR));
    public static final String PIE_CHART = "pie";
    public static final List<String> VOTE_LABELS = new ArrayList<>(Arrays.asList("Yes", "No"));
    public static final List<String> NO_VOTE_LABEL = new ArrayList<>(Arrays.asList("No Votes"));
    private List<String> labels = new ArrayList<>();
    List<String> colorList = new ArrayList<>();


    public ChartController(JdbcVoteDao jdbcVoteDao) {
        this.jdbcVoteDao = jdbcVoteDao;
    }

    @GetMapping(value = "/spend-requests/{requestId}/chart", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public byte[] getChartByVotes(@PathVariable int requestId) {

        //  create the datalist
        List<Vote> voteList = jdbcVoteDao.getVotesBySpendRequestId(requestId);
        List<Integer> voteValues = getVoteData(voteList);
        if (voteValues.isEmpty()) {
            colorList.add(NO_VOTE_COLOR);
        } else {
            colorList.add(APPROVE_VOTE_COLOR);
            colorList.add(REJECT_VOTE_COLOR);
        }
        String label = "Spend Request Votes";
        List<DataSet> dataSets = List.of(new DataSet(voteValues, colorList, label));

        // create the data

        if (voteValues.isEmpty()) {
            labels = new ArrayList<>(NO_VOTE_LABEL);
        } else {
            labels = new ArrayList<>(VOTE_LABELS);
        }
        Data data = new Data(dataSets, labels);

        // create the chart

        Chart chart = new Chart(PIE_CHART, data);

        // create the chart object

        ChartObject chartObject = new ChartObject("2", "transparent", "500", "300", 1.0, "png", chart);

        ChartService chartService = new ChartService();

        return chartService.getChartImg(chartObject);

       /* if (sumVotes == 0) {  // set some third value for no votes
        }*/

        // List<Integer> dataList = new ArrayList<>(Arrays.asList(51, 49));
        // List<String> colorList = new ArrayList<>(Arrays.asList("rgb(19, 240, 41)", "rgb(242, 1, 1)"));
        // String label = "Test DataSet";
        // List<DataSet> dataSets = List.of(new DataSet(dataList, colorList, label));

        // List<String> labels = new ArrayList<>(Arrays.asList("Yes", "No"));
        // Data testData = new Data(dataSets, labels);

        /*String type = "pie";
        Chart chart = new Chart(type, data);*/

    /*    ChartObject chartObject = new ChartObject("2", "transparent", "500", "300", 1.0, "png", chart);

        ChartService chartService = new ChartService();

        return chartService.getChartImg(chartObject);*/

        /*Path path = Path.of("C:\\Users\\Student\\workspace\\capstones\\java-finalcapstone-team3\\java\\src\\main\\resources\\img.png");
        Path img = Files.createFile(path);
        Files.write(img, imgBytes);*/
    }


    public List<Integer> getVoteData (List<Vote>voteList) {
        List<Integer> newVoteList = new ArrayList<>();

        int yesVotes = 0;
        int noVotes = 0;

        for (Vote vote : voteList) {
            if (vote.isApproved()) {
                yesVotes++;
            } else { noVotes++;
            }
        }
        newVoteList.add(yesVotes);
        newVoteList.add(noVotes);

        return newVoteList;
    }

}
