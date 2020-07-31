package com.ptp.campaign.model;

public class TargetMarketPackages {

	private int catalogId;
	private String startDate;
	private String endDate;
	private int[] channels;
	private int spotPrice;
	
	public int getCatalogId() {
		return catalogId;
	}
	public void setCatalogId(int catalogId) {
		this.catalogId = catalogId;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public int[] getChannels() {
		return channels;
	}
	public void setChannels(int[] channels) {
		this.channels = channels;
	}
	public int getSpotPrice() {
		return spotPrice;
	}
	public void setSpotPrice(int spotPrice) {
		this.spotPrice = spotPrice;
	}
	


}
