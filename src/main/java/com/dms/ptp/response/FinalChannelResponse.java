package com.dms.ptp.response;

import java.util.List;

/**
 * List of ChannelResponse
 */
public class FinalChannelResponse {

    private int listSize;
    private List<ChannelResponse> channelResponseList;

    public FinalChannelResponse() {
        super();
    }

    public FinalChannelResponse(int listSize, List<ChannelResponse> channelResponseList) {
        super();
        this.listSize = listSize;
        this.channelResponseList = channelResponseList;
    }

    public int getListSize() {
        return listSize;
    }

    public void setListSize(int listSize) {
        this.listSize = listSize;
    }

    public List<ChannelResponse> getChannelResponseList() {
        return channelResponseList;
    }

    public void setChannelResponseList(List<ChannelResponse> channelResponseList) {
        this.channelResponseList = channelResponseList;
    }

    @Override
    public String toString() {
        return "FinalChannelResponse [listSize=" + listSize + ", channelResponseList=" + channelResponseList + "]";
    }


}
