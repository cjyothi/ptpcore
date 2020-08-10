package com.dms.ptp.response;

public class SCSplitByWeekpart {

    
    private String weekPart;
    private int percentage;
    
    public String getWeekPart() {
        return weekPart;
    }
    public void setWeekPart(String weekPart) {
        this.weekPart = weekPart;
    }
    public int getPercentage() {
        return percentage;
    }
    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }
    
    public SCSplitByWeekpart() {
        super();
    }

    public SCSplitByWeekpart(String weekPart, int percentage) {
        this.weekPart=weekPart;
        this.percentage=percentage;
    }

    @Override
    public String toString() {
        return "SCSplitByWeekpart [weekPart=" + weekPart + ", percentage=" + percentage + "]";
    }
    
    
}
