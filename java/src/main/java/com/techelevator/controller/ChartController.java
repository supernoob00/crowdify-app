package com.techelevator.controller;

import com.techelevator.dao.JdbcCampaignDao;
import com.techelevator.dao.JdbcDonationDao;
import com.techelevator.dao.JdbcUserDao;
import com.techelevator.dao.JdbcVoteDao;
import com.techelevator.model.Campaign;
import com.techelevator.model.Donation;
import com.techelevator.model.Vote;
import com.techelevator.service.ChartService;
import com.techelevator.servicemodel.*;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


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
    private final JdbcDonationDao jdbcDonationDao;

    public ChartController(JdbcVoteDao jdbcVoteDao,
                           JdbcCampaignDao jdbcCampaignDao,
                           JdbcDonationDao jdbcDonationDao) {
        this.jdbcVoteDao = jdbcVoteDao;
        this.jdbcCampaignDao = jdbcCampaignDao;
        this.jdbcDonationDao = jdbcDonationDao;
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

        ChartObject chartObject = new ChartObject(
                "2",
                "transparent",
                "200",
                "200",
                1.0,
                "png",
                chart);

        ChartService chartService = new ChartService();
        return chartService.getChartImg(chartObject);
    }


    @GetMapping(value = "/campaigns/{campaignId}/chart",
            produces =
            MediaType.IMAGE_PNG_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public byte[] getLineChartByCampaign(@PathVariable int campaignId) {
        Campaign campaign = jdbcCampaignDao.getCampaignById(campaignId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Campaign" +
                    " not found");
                });
        LocalDateTime start = LocalDateTime.now().minusDays(30);

        // create x-axis, which is the number of days since the campaign
        // start date
        List<String> dates = donationChartXAxis(start, LocalDateTime.now());

        // create the y-axis, which is the sum of all donations up to the
        // specific day on the x-axis
        List<Integer> donationTotal = new ArrayList<>();

        // sort the donations by date (earliest to latest)
        List<Donation> donations =
                jdbcDonationDao.getDonationsByCampaignId(campaignId).stream()
                        .filter(d -> d.getDate().isAfter(start))
                        .sorted(Comparator.comparing(Donation::getDate))
                        .collect(Collectors.toList());

        // populate y-axis with values
        Iterator<Donation> donationIt = donations.iterator();
        Donation currDonation = donationIt.hasNext() ? donationIt.next() : null;
        int currSum = 0;

        for (int i = 0; i < 30; ) {
            if (donationIt.hasNext() && ChronoUnit.DAYS.between(start, currDonation.getDate()) == i) {
                currSum += currDonation.getAmount() / 100;
                currDonation = donationIt.next();
            } else {
                donationTotal.add(currSum);
                i++;
            }
        }

        List<DataSet> dataSets = List.of(new DataSet(donationTotal,
                List.of("rgb(34,139,34)"), "Donation history"));
        Data data = new Data(dataSets, dates);
        Chart chart = new Chart("line", data);

        ChartObject chartObject = new ChartObject(
                "2",
                "transparent",
                "500",
                "500",
                1.0,
                "png",
                chart);

        ChartService chartService = new ChartService();
        return chartService.getChartImg(chartObject);
    }

    private List<String> donationChartXAxis(LocalDateTime start,
                                            LocalDateTime end) {
        if (end.equals(start) || end.isBefore(start)) {
            throw new IllegalArgumentException("End date cannot be before " +
                    "start date");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd");

        List<String> axis = new ArrayList<>();

        long daysApart = ChronoUnit.DAYS.between(start, end);

        LocalDate currDate = start.toLocalDate();
        LocalDate endDate = end.toLocalDate();

        if (daysApart < 90) {
            while (currDate.isBefore(endDate)) {
                axis.add(formatter.format(currDate));
                currDate = currDate.plusDays(1);
            }
        }
        return axis;
    }

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
