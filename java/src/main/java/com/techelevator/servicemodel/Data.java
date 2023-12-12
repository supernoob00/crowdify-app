package com.techelevator.servicemodel;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Data {

    @JsonProperty("datasets")
    private List<DataSet> dataSets;
    private List<String> labels;

    public Data(List<DataSet> dataSets, List<String> labels) {
        this.dataSets = dataSets;
        this.labels = labels;
    }

    public List<DataSet> getDataSets() {
        return dataSets;
    }

    public void setDataSets(List<DataSet> dataSets) {
        this.dataSets = dataSets;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    @Override
    public String toString() {
        return "Data{" +
                "dataSets=" + dataSets +
                ", labels=" + labels +
                '}';
    }
}
