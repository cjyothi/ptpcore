package com.dms.ptp.dto;

public class RateCardInput {
    
    private String title;
    private String start_date;
    
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart_date() {
        return start_date;
    }
    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }
    @Override
    public String toString() {
        return "RateCardInput [title=" + title + ", start_date=" + start_date + "]";
    }
    
    public RateCardInput() {
        super();
    }
    
}
