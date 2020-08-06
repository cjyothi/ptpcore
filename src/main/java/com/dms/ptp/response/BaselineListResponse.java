package com.dms.ptp.response;

public class BaselineListResponse {
    private int id;
    private String title;
    private String status;
    private String weekPart;
    private int rateCardId;

    public BaselineListResponse() {
    }

    public BaselineListResponse(int id, String title, String status, String weekPart, int rateCardId) {
        this.id=id;
        this.title=title;
        this.status=status;
        this.weekPart=weekPart;
        this.rateCardId=rateCardId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title=title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status=status;
    }

    public String getWeekPart() {
        return weekPart;
    }

    public void setWeekPart(String weekPart) {
        this.weekPart=weekPart;
    }

    public int getRateCardId() {
        return rateCardId;
    }

    public void setRateCardId(int rateCardId) {
        this.rateCardId=rateCardId;
    }
}
