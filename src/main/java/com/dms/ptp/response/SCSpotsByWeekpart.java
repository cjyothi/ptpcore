package com.dms.ptp.response;

public class SCSpotsByWeekpart {
    
    private String weekPart;
    private String dayPart;
    private int spots;
    
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
    public int getSpots() {
        return spots;
    }
    public void setSpots(int spots) {
        this.spots = spots;
    }
    
    public SCSpotsByWeekpart() {
        super();
    }

    public SCSpotsByWeekpart(String weekPart, String dayPart, int spots) {
        this.weekPart=weekPart;
        this.dayPart=dayPart;
        this.spots=spots;
    }

    @Override
    public String toString() {
        return "SCSpotsByWeekpart [weekPart=" + weekPart + ", dayPart=" + dayPart + ", spots=" + spots + "]";
    }
    
}
