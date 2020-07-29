package com.dms.ptp.dto;

public class AffUpdateRequest {
    
    private int id;
    private String title;
    private String status;
    private String weekPart;
    private int ratecardId;
    private AffinityResponse affinity;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getWeekPart() {
        return weekPart;
    }
    public void setWeekPart(String weekPart) {
        this.weekPart = weekPart;
    }
    public int getRatecardId() {
        return ratecardId;
    }
    public void setRatecardId(int ratecardId) {
        this.ratecardId = ratecardId;
    }
    public AffinityResponse getAffinity() {
        return affinity;
    }
    public void setAffinity(AffinityResponse affinity) {
        this.affinity = affinity;
    }
    @Override
    public String toString() {
        return "AffUpdateRequest [id=" + id + ", title=" + title + ", status=" + status + ", weekPart=" + weekPart
                + ", ratecardId=" + ratecardId + ", affinity=" + affinity + "]";
    }
    public AffUpdateRequest() {
        super();
    }
    
}