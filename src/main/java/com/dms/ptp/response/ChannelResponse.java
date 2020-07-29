package com.dms.ptp.response;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Response for Channel
 */
@Entity
public class ChannelResponse {

    @Id
    private int id;
    private String channel;
    private String profile;
    private String ADH;

    public ChannelResponse(String channel, String profile, String aDH) {
        super();
        this.channel = channel;
        this.profile = profile;
        ADH = aDH;
    }

    public ChannelResponse() {
        super();
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getADH() {
        return ADH;
    }

    public void setADH(String aDH) {
        ADH = aDH;
    }

    @Override
    public String toString() {
        return "ChannelResponse [channel=" + channel + ", profile=" + profile + ",  ADH=" + ADH + "]";
    }

}
