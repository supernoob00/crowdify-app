package com.techelevator;

import com.techelevator.service.ChartService;
import com.techelevator.servicemodel.Chart;
import com.techelevator.servicemodel.ChartObject;
import com.techelevator.servicemodel.Data;
import com.techelevator.servicemodel.DataSet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class Application {




    public static void main(String[] args) throws IOException, URISyntaxException {
        SpringApplication.run(Application.class, args);


/*        List<Integer> dataList = new ArrayList<>(Arrays.asList(51, 49));
        List<String> colorList = new ArrayList<>(Arrays.asList("rgb (19, 240, 41)", "rgb (242, 1, 1)"));
        String label = "Test DataSet";

        DataSet dataSets = new DataSet(dataList, colorList, label);
        List<String> labels = new ArrayList<>(Arrays.asList("Yes", "No"));

        Data testData = new Data(dataSets,labels);



        String type = "pie";

        Chart chart = new Chart (type, testData);



        ChartObject chartObject = new ChartObject("2","transparent","500","300",1.0,"png", chart);

        ChartService chartService = new ChartService();

        byte[] imgBytes = chartService.getChartImg(chartObject);

        Path path = Path.of("C:\\Users\\Student\\workspace\\capstones\\java-finalcapstone-team3\\java\\src\\main\\resources\\img.png");
        Path img = Files.createFile(path);
        Files.write(img, imgBytes);*/
    }
}
