package com.ptp.campaign.model;

import java.util.List;

public class SalesAreaLMKResponse {
    
    private int total;
    List<SalesAreaLMK> items;
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<SalesAreaLMK> getItems() {
		return items;
	}
	public void setItems(List<SalesAreaLMK> items) {
		this.items = items;
	}
    
}
