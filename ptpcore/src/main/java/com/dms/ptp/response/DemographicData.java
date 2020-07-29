package com.dms.ptp.response;

/**
 * Response for Demographics
 */
public class DemographicData {

    private int demoId;
    private String type;
    private String description;
    private String segment;

    public DemographicData(int demoId, String type, String description, String segment) {
        this.demoId = demoId;
        this.type = type;
        this.segment = segment;
        this.description = description;
    }

    public DemographicData() {
    }

    public int getDemoId() {
        return demoId;
    }

    public void setDemoId(int demoId) {
        this.demoId = demoId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment=segment;
    }
}
