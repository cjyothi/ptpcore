package com.dms.ptp.dto;

public class LmkAudienceResponse {

	private int channelNo;
	private int demoNo;
	private String weekPart;
	private String dayPart;
	private double avgRatings;
	private double avgImpacts;
	private double avgUniverse;

	public LmkAudienceResponse() {
		super();
	}

	public LmkAudienceResponse(int channelNo, int demoNo, String weekPart, String dayPart, double avgRatings,
			double avgImpacts, double avgUniverse) {
		super();
		this.channelNo = channelNo;
		this.demoNo = demoNo;
		this.weekPart = weekPart;
		this.dayPart = dayPart;
		this.avgRatings = avgRatings;
		this.avgImpacts = avgImpacts;
		this.avgUniverse = avgUniverse;
	}

	public int getChannelNo() {
		return channelNo;
	}

	public void setChannelNo(int channelNo) {
		this.channelNo = channelNo;
	}

	public int getDemoNo() {
		return demoNo;
	}

	public void setDemoNo(int demoNo) {
		this.demoNo = demoNo;
	}

	public String getWeekPart() {
		return weekPart;
	}

	public void setWeekPart(String weekPart) {
		this.weekPart = weekPart;
	}

	public String getDayPart() {
		return dayPart;
	}

	public void setDayPart(String dayPart) {
		this.dayPart = dayPart;
	}

	public double getAvgRatings() {
		return avgRatings;
	}

	public void setAvgRatings(double avgRatings) {
		this.avgRatings = avgRatings;
	}

	public double getAvgImpacts() {
		return avgImpacts;
	}

	public void setAvgImpacts(double avgImpacts) {
		this.avgImpacts = avgImpacts;
	}

	public double getAvgUniverse() {
		return avgUniverse;
	}

	public void setAvgUniverse(double avgUniverse) {
		this.avgUniverse = avgUniverse;
	}
}
