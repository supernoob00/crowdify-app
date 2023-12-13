package com.techelevator.servicemodel;

import java.util.List;

public class ChartScales {
    private List<AxisDisplay> xAxes;

    private List<AxisDisplay> yAxes;

    public ChartScales() {
    }

    public void setxAxes(List<AxisDisplay> xAxes) {
        this.xAxes = xAxes;
    }

    public void setyAxes(List<AxisDisplay> yAxes) {
        this.yAxes = yAxes;
    }
}
