package com.dms.ptp.response;

public class SCAudienceInfo {
    
    private String weekPart;
    private String dayPart;
    private double impact;
    private double rating;
    
    
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
    public double getImpact() {
        return impact;
    }
    public void setImpact(double impact) {
        this.impact = impact;
    }
    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
    
    
    public SCAudienceInfo() {
        super();
    }
    
    
    public SCAudienceInfo(String weekPart, String dayPart, double impact, double rating) {
        super();
        this.weekPart = weekPart;
        this.dayPart = dayPart;
        this.impact = impact;
        this.rating = rating;
    }
    
    @Override
    public String toString() {
        return "SCAudienceInfo [weekPart=" + weekPart + ", dayPart=" + dayPart + ", impact=" + impact + ", rating="
                + rating + "]";
    }
    
    
}
