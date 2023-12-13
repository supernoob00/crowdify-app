package com.techelevator.servicemodel;

public class ChartOptions {
    private ChartScales scales;

    public ChartOptions(ChartScales scales) {
        this.scales = scales;
    }

    public ChartScales getScales() {
        return scales;
    }

    public void setScales(ChartScales scales) {
        this.scales = scales;
    }
}
