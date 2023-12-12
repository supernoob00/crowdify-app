package com.techelevator.controller;

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
    public static final String APPROVE_VOTE_COLOR = "rgb(19, 240, 41)";

    @GetMapping(value = "/chart/votes", produces = MediaType.IMAGE_PNG_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public byte[] getChartByVotes() {

        List<Integer> dataList = new ArrayList<>(Arrays.asList(51, 49));
        List<String> colorList = new ArrayList<>(Arrays.asList("rgb(19, 240, 41)", "rgb(242, 1, 1)"));
        String label = "Test DataSet";
        List<DataSet> dataSets = List.of(new DataSet(dataList, colorList, label));

        List<String> labels = new ArrayList<>(Arrays.asList("Yes", "No"));
        Data testData = new Data(dataSets, labels);

        String type = "pie";
        Chart chart = new Chart(type, testData);

        ChartObject chartObject = new ChartObject("2", "transparent", "500", "300", 1.0, "png", chart);

        ChartService chartService = new ChartService();

        return chartService.getChartImg(chartObject);

        /*Path path = Path.of("C:\\Users\\Student\\workspace\\capstones\\java-finalcapstone-team3\\java\\src\\main\\resources\\img.png");
        Path img = Files.createFile(path);
        Files.write(img, imgBytes);*/
    }
}
