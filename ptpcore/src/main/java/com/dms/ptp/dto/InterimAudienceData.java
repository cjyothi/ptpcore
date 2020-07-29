package com.dms.ptp.dto;

public class InterimAudienceData {

    int channelNo;
    int demoNo;
    double avgImpact;

    public int getChannelNo() {
        return channelNo;
    }
    public void setChannelNo(int channelNo) {
        this.channelNo = channelNo;
    }
    public int getDemoNo() {
        return demoNo;
    }
    public void setDemoNo(int demoNo) {
        this.demoNo = demoNo;
    }
    public double getAvgImpact() {
        return avgImpact;
    }
    public void setAvgImpact(double avgImpact) {
        this.avgImpact = avgImpact;
    }
    @Override
    public String toString() {
        return "InterimAudienceData [channelNo=" + channelNo + ", demoNo=" + demoNo + ", avgImpact=" + avgImpact + "]";
    }
    public InterimAudienceData() {
        super();
    }
    public InterimAudienceData(int channelNo, int demoNo, double avgImpact) {
        super();
        this.channelNo = channelNo;
        this.demoNo = demoNo;
        this.avgImpact = avgImpact;
    }



}
