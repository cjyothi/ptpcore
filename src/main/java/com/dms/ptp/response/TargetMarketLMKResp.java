package com.dms.ptp.response;

import java.util.List;

import com.dms.ptp.entity.PackageForecast;

public class TargetMarketLMKResp {

	private int total;
	private int[] targetMarkets;
	private List<PackageForecast> items;
	private String message;

	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int[] getTargetMarkets() {
		return targetMarkets;
	}
	public void setTargetMarkets(int[] targetMarkets) {
		this.targetMarkets = targetMarkets;
	}
	public List<PackageForecast> getItems() {
		return items;
	}
	public void setItems(List<PackageForecast> items) {
		this.items = items;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
