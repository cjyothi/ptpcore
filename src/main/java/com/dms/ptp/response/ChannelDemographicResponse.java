package com.dms.ptp.response;

import java.util.List;

/**
 * Response for List of Channel amd Demographic
 */
public class ChannelDemographicResponse {

    private int total;
    private List items;

    public ChannelDemographicResponse(int total, List items) {
        this.total = total;
        this.items = items;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List getItems() {
        return items;
    }

    public void setItems(List items) {
        this.items = items;
    }
}
