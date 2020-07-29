package com.dms.ptp.response;

import com.dms.ptp.dto.AffinityResponse;


public class BaselineResponse {
   private int id;
   private String title;
   private String status;
   private String weekPart;
   private int ratecardId;
   private AffinityResponse affinityResponse;

    public BaselineResponse() {
    }

    public BaselineResponse(int id, String title, String status, String weekPart, int ratecardId, AffinityResponse affinityResponse) {
        this.id=id;
        this.title=title;
        this.status=status;
        this.weekPart=weekPart;
        this.ratecardId=ratecardId;
        this.affinityResponse=affinityResponse;
    }

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
        this.weekPart=weekPart;
    }

    public int getRatecardId() {
        return ratecardId;
    }

    public void setRatecardId(int ratecardId) {
        this.ratecardId=ratecardId;
    }

    public AffinityResponse getAffinityResponse() {
        return affinityResponse;
    }

    public void setAffinityResponse(AffinityResponse affinityResponse) {
        this.affinityResponse = affinityResponse;
    }
    
    
}
