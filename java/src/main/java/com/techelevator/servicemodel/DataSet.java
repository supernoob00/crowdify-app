package com.techelevator.servicemodel;

import java.util.List;

public class DataSet {
    private List<Integer> data;
    private List<String> backgroundColor;
    private String label;

    public DataSet(List<Integer> data, List<String> backgroundColor, String label) {
        this.data = data;
        this.backgroundColor = backgroundColor;
        this.label = label;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }

    public List<String> getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(List<String> backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "DataSet{" +
                "data=" + data +
                ", backgroundColor=" + backgroundColor +
                ", label='" + label + '\'' +
                '}';
    }
}
