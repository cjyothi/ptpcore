package com.dms.ptp.dto;

public class EmptyAudienceData {
    private int channelNo;
    private int demoNo;

    public EmptyAudienceData(int channelNo, int demoNo) {
        this.channelNo=channelNo;
        this.demoNo=demoNo;
    }

    public int getChannelNo() {
        return channelNo;
    }

    public void setChannelNo(int channelNo) {
        this.channelNo=channelNo;
    }

    public int getDemoNo() {
        return demoNo;
    }

    public void setDemoNo(int demoNo) {
        this.demoNo=demoNo;
    }
}
