package com.dms.ptp.response;

public class CalculatedRate {
	private String id;
	private double rate;

	public CalculatedRate() {
	}

	public CalculatedRate(String id, double rate) {
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
		return "CalculatedRate [id=" + id + ", rate=" + rate + "]";
	}

}
