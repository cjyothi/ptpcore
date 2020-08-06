package com.dms.ptp.response;

public class RateQOQ {
	private String id;
	private double rate;

	public RateQOQ() {
	}

	public RateQOQ(String id, double rate) {
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
		return "RateQOQ [id=" + id + ", rate=" + rate + "]";
	}

}
