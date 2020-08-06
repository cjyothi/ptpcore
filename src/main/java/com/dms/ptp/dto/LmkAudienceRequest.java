package com.dms.ptp.dto;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public class LmkAudienceRequest {
    private List<Integer> channels;
    private List<Integer> targetMarkets;
    private String sourceStart;
    private String sourceEnd;
    private String weekpart;
     private List<String> daypart;

    public LmkAudienceRequest() {
    }


    public LmkAudienceRequest(List<Integer> channels, List<Integer> targetMarkets, String sourceStart, String sourceEnd, String weekpart, List<String> daypart) {
        this.channels = channels;
        this.targetMarkets = targetMarkets;
        this.sourceStart = sourceStart;
        this.sourceEnd = sourceEnd;
        this.weekpart = weekpart;
        this.daypart = daypart;
    }

    public List<Integer> getChannels() {
        return channels;
    }

    public void setChannels(List<Integer> channels) {
        this.channels = channels;
    }

    public List<Integer> getTargetMarkets() {
        return targetMarkets;
    }

    public void setTargetMarkets(List<Integer> targetMarkets) {
        this.targetMarkets = targetMarkets;
    }

    public String getSourceStart() {
        return sourceStart;
    }

    public void setSourceStart(String sourceStart) {
        this.sourceStart = sourceStart;
    }

    public String getSourceEnd() {
        return sourceEnd;
    }

    public void setSourceEnd(String sourceEnd) {
        this.sourceEnd = sourceEnd;
    }

    public String getWeekpart() {
        return weekpart;
    }

    public void setWeekpart(String weekpart) {
        this.weekpart = weekpart;
    }

    public List<String> getDaypart() {
        return daypart;
    }

    public void setDaypart(List<String> daypart) {
        this.daypart = daypart;
    }
}
