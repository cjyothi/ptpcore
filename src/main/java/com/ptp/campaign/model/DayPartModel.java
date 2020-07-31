package com.ptp.campaign.model;

public class DayPartModel {

    private String code;
    private String title;
    private double percentage;
    private boolean is_average;
   
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public double getPercentage() {
        return percentage;
    }
    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
    public boolean isIs_average() {
        return is_average;
    }
    public void setIs_average(boolean is_average) {
        this.is_average = is_average;
    }

}
