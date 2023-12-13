package com.techelevator.servicemodel;

public class Chart {
    private String type;
    private ChartOptions options;
    private Data data;

    public Chart(String type, Data data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ChartOptions getOptions() {
        return options;
    }

    public void setOptions(ChartOptions options) {
        this.options = options;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Chart{" +
                "type='" + type + '\'' +
                ", data=" + data +
                '}';
    }
}
