package com.dms.ptp.dto;

import com.dms.ptp.response.SCLooseSpotRate;

public class InterimSCLooseSpotRate {
    private int channelId;
    private SCLooseSpotRate scLooseSpotRate;

    public InterimSCLooseSpotRate() {
    }

    public InterimSCLooseSpotRate(int channelId, SCLooseSpotRate scLooseSpotRate) {
        this.channelId=channelId;
        this.scLooseSpotRate=scLooseSpotRate;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId=channelId;
    }

    public SCLooseSpotRate getScLooseSpotRate() {
        return scLooseSpotRate;
    }

    public void setScLooseSpotRate(SCLooseSpotRate scLooseSpotRate) {
        this.scLooseSpotRate=scLooseSpotRate;
    }
}
