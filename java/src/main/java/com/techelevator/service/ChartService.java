package com.techelevator.service;

import com.techelevator.servicemodel.ChartObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

/* This is sent to api */
public class ChartService {

private static final String API_BASE_URL = "https://quickchart.io/chart";
private final RestTemplate restTemplate = new RestTemplate();

    public byte[] getChartImg(ChartObject newChart) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ChartObject> entity = new HttpEntity<>(newChart, headers);
        System.out.println(entity);

        byte[] returnedChart;
        try {
            returnedChart = restTemplate.postForObject(API_BASE_URL, entity, byte[].class);
        } catch (RestClientResponseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        System.out.println(returnedChart);
        return returnedChart;
    }
}
