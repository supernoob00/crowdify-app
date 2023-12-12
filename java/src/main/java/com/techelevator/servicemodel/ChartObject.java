package com.techelevator.servicemodel;

public class ChartObject {
    private String version;
    private String backgroundColor;
    private String width;
    private String height;
    private double devicePixelRatio;
    private String format;
    private Chart chart;

    public ChartObject(String version, String backgroundColor, String width, String height, double devicePixelRatio, String format, Chart chart) {
        this.version = version;
        this.backgroundColor = backgroundColor;
        this.width = width;
        this.height = height;
        this.devicePixelRatio = devicePixelRatio;
        this.format = format;
        this.chart = chart;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public double getDevicePixelRatio() {
        return devicePixelRatio;
    }

    public void setDevicePixelRatio(double devicePixelRatio) {
        this.devicePixelRatio = devicePixelRatio;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Chart getChart() {
        return chart;
    }

    public void setChart(Chart chart) {
        this.chart = chart;
    }

    @Override
    public String toString() {
        return "ChartObject{" +
                "version='" + version + '\'' +
                ", backgroundColor='" + backgroundColor + '\'' +
                ", width='" + width + '\'' +
                ", height='" + height + '\'' +
                ", devicePixelRatio=" + devicePixelRatio +
                ", format='" + format + '\'' +
                ", chart=" + chart +
                '}';
    }
}
