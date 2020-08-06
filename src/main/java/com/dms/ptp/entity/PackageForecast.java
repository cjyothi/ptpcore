package com.dms.ptp.entity;

import java.util.List;

import com.dms.ptp.dto.PackageForecastLMK;

public class PackageForecast {

	private int catalogId;
	private String startDate;
	private String endDate;
	private List<PackageForecastLMK> demos;

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
	public List<PackageForecastLMK> getDemos() {
		return demos;
	}
	public void setDemos(List<PackageForecastLMK> demos) {
		this.demos = demos;
	}



}
