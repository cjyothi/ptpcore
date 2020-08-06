package com.dms.ptp.response;

public class SCLooseSpotRate {
    
    
    private String weekPart;
    private String dayPart;
    private double rate;
    
    public String getWeekPart() {
        return weekPart;
    }
    public void setWeekPart(String weekPart) {
        this.weekPart = weekPart;
    }
    public String getDayPart() {
        return dayPart;
    }
    public void setDayPart(String dayPart) {
        this.dayPart = dayPart;
    }
    public double getRate() {
        return rate;
    }
    public void setRate(double rate) {
        this.rate = rate;
    }
    public SCLooseSpotRate() {
        super();
    }
    
    
    public SCLooseSpotRate(String weekPart, String dayPart, double rate) {
        super();
        this.weekPart = weekPart;
        this.dayPart = dayPart;
        this.rate = rate;
    }
    
    @Override
    public String toString() {
        return "SCLooseSpotRate [weekPart=" + weekPart + ", dayPart=" + dayPart + ", rate=" + rate + "]";
    }
    
    
}
