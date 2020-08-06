package com.dms.ptp.dto;

public class LmkRateResponse {
	private int salesAreaNo;
	private String daypart;
	private double duration;
	private double available;
	private double sold;
	private double breakPrice;
	private double sellOut;

	public int getSalesAreaNo() {
		return salesAreaNo;
	}

	public void setSalesAreaNo(int salesAreaNo) {
		this.salesAreaNo = salesAreaNo;
	}

	public String getDaypart() {
		return daypart;
	}

	public void setDaypart(String daypart) {
		this.daypart = daypart;
	}

	public double getDuration() {
		return duration;
	}

	public void setDuration(double duration) {
		this.duration = duration;
	}

	public double getAvailable() {
		return available;
	}

	public void setAvailable(double available) {
		this.available = available;
	}

	public double getSold() {
		return sold;
	}

	public void setSold(double sold) {
		this.sold = sold;
	}

	public double getBreakPrice() {
		return breakPrice;
	}

	public void setBreakPrice(double breakPrice) {
		this.breakPrice = breakPrice;
	}

	public double getSellOut() {
		return sellOut;
	}

	public void setSellOut(double sellOut) {
		this.sellOut = sellOut;
	}

	@Override
	public String toString() {
		return "LmkRateResponse [salesAreaNo=" + salesAreaNo + ", daypart=" + daypart + ", duration=" + duration
				+ ", available=" + available + ", sold=" + sold + ", breakPrice=" + breakPrice + ", sellOut=" + sellOut
				+ "]";
	}

}
