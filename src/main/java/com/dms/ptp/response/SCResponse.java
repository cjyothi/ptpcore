package com.dms.ptp.response;

public class SCResponse {

    private SpotChannelResponse spotChannelResponse;
    private String message;


    public SpotChannelResponse getSpotChannelResponse() {
        return spotChannelResponse;
    }

    public void setSpotChannelResponse(SpotChannelResponse spotChannelResponse) {
        this.spotChannelResponse=spotChannelResponse;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message=message;
    }
}
