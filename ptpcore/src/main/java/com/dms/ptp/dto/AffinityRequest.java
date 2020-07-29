package com.dms.ptp.dto;

import java.util.List;

public class AffinityRequest {
    private int territory;
    private int platform;
    private int panel;
    private List<Integer> primaryDemos;
    private List<Integer> secondaryDemos;
    private String sourceStart;
    private String sourceEnd;
    private String weekParts;
    private List<String> dayParts;

    public AffinityRequest() {
    }

    public AffinityRequest(int territory, int platform, int panel, List<Integer> primaryDemos, List<Integer> secondaryDemos, String sourceStart, String sourceEnd, String weekParts, List<String> dayParts) {
        this.territory=territory;
        this.platform=platform;
        this.panel=panel;
        this.primaryDemos=primaryDemos;
        this.secondaryDemos=secondaryDemos;
        this.sourceStart=sourceStart;
        this.sourceEnd=sourceEnd;
        this.weekParts=weekParts;
        this.dayParts=dayParts;
    }

    public int getTerritory() {
        return territory;
    }

    public void setTerritory(int territory) {
        this.territory = territory;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public int getPanel() {
        return panel;
    }

    public void setPanel(int panel) {
        this.panel = panel;
    }

    public List<Integer> getPrimaryDemos() {
        return primaryDemos;
    }

    public void setPrimaryDemos(List<Integer> primaryDemos) {
        this.primaryDemos = primaryDemos;
    }

    public List<Integer> getSecondaryDemos() {
        return secondaryDemos;
    }

    public void setSecondaryDemos(List<Integer> secondaryDemos) {
        this.secondaryDemos = secondaryDemos;
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

    public String getWeekParts() {
        return weekParts;
    }

    public void setWeekParts(String weekParts) {
        this.weekParts = weekParts;
    }

    public List<String> getDayParts() {
        return dayParts;
    }

    public void setDayParts(List<String> dayParts) {
        this.dayParts = dayParts;
    }
}
