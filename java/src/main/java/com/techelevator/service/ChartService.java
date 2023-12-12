package com.techelevator.service;

import com.techelevator.servicemodel.ChartObject;
import com.techelevator.servicemodel.Data;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

/* This is sent to api */
public class ChartService {

private static final String API_BASE_URL = "https://quickchart.io/chart";
private final RestTemplate restTemplate = new RestTemplate();

public ChartObject addChart (ChartObject newChart) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<ChartObject> entity = new HttpEntity<>(newChart, headers);

    ChartObject returnedChart = null;
    try {
        returnedChart = restTemplate.postForObject(API_BASE_URL, entity, ChartObject.class);
    } catch (RestClientResponseException e) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    return returnedChart;
}
}
