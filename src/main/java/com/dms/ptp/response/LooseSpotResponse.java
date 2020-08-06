package com.dms.ptp.response;

public class LooseSpotResponse {
	
	private int baselineId;
	private int pricingId;
	private String message;
	private String status;
	public int getBaselineId() {
		return baselineId;
	}
	public void setBaselineId(int baselineId) {
		this.baselineId = baselineId;
	}
	public int getPricingId() {
		return pricingId;
	}
	public void setPricingId(int pricingId) {
		this.pricingId = pricingId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
