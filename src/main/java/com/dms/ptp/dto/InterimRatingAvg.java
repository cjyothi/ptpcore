package com.dms.ptp.dto;

public class InterimRatingAvg {

    int channelNo;
    int demoNo;
    double avgRatings;
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
    public double getAvgRatings() {
        return avgRatings;
    }
    public void setAvgRatings(double avgRatings) {
        this.avgRatings = avgRatings;
    }
    @Override
    public String toString() {
        return "InterimRatingAvg [channelNo=" + channelNo + ", demoNo=" + demoNo + ", avgRatings=" + avgRatings + "]";
    }
    public InterimRatingAvg(int channelNo, int demoNo, double avgRatings) {
        super();
        this.channelNo = channelNo;
        this.demoNo = demoNo;
        this.avgRatings = avgRatings;
    }



}
