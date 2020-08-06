package com.dms.ptp.dto;

public class RateAudienceData {

	private int channelId;
	private int salesAreaNo;
	private double rating;
	private double audience;
	private double cpp;
	private double cpt;
	private String dayPart;
	private int demoId;

	public int getDemoId() {
		return demoId;
	}

	public void setDemoId(int demoId) {
		this.demoId = demoId;
	}

	public String getDayPart() {
		return dayPart;
	}

	public void setDayPart(String dayPart) {
		this.dayPart = dayPart;
	}

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public int getSalesAreaNo() {
		return salesAreaNo;
	}

	public void setSalesAreaNo(int salesAreaNo) {
		this.salesAreaNo = salesAreaNo;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public double getAudience() {
		return audience;
	}

	public void setAudience(double audience) {
		this.audience = audience;
	}

	public double getCpp() {
		return cpp;
	}

	public void setCpp(double cpp) {
		this.cpp = cpp;
	}

	public double getCpt() {
		return cpt;
	}

	public void setCpt(double cpt) {
		this.cpt = cpt;
	}

	@Override
	public String toString() {
		return "RateAudienceData [channelId=" + channelId + ", salesAreaNo=" + salesAreaNo + ", rating=" + rating
				+ ", audience=" + audience + ", cpp=" + cpp + ", cpt=" + cpt + "]";
	}

}
