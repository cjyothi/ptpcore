package com.dms.ptp.dto;

import java.util.List;

public class LMKRateRequest {
	private List<Integer> salesAreaNo;
	private String startDate;
	private String endDate;

	public List<Integer> getSalesAreaNo() {
		return salesAreaNo;
	}

	public void setSalesAreaNo(List<Integer> salesAreaNo) {
		this.salesAreaNo = salesAreaNo;
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

	@Override
	public String toString() {
		return "LMKRateRequest [salesAreaNo=" + salesAreaNo + ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}

}
