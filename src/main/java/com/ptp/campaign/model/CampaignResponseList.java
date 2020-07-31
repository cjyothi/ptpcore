package com.ptp.campaign.model;

import java.util.List;

import org.springframework.http.HttpStatus;

public class CampaignResponseList {

    private int total;
    private HttpStatus status;
    List<CampaignResponse> items;

    public int getTotal() {
        return total;
    }
    public void setTotal(int total) {
        this.total = total;
    }
    public HttpStatus getStatus() {
		return status;
	}
	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	public List<CampaignResponse> getItems() {
        return items;
    }
    public void setItems(List<CampaignResponse> items) {
        this.items = items;
    }
}
