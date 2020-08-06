package com.dms.ptp.response;

public class RateYOY {
	private String id;
	private double rate;

	public RateYOY() {
	}

	public RateYOY(String id, double rate) {
		this.id=id;
		this.rate=rate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	@Override
	public String toString() {
		return "RateYOY [id=" + id + ", rate=" + rate + "]";
	}

}
