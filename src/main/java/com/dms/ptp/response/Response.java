package com.dms.ptp.response;

public class Response {
	private PricingResponse pricingResponse;
	private String message;

	public PricingResponse getPricingResponse() {
		return pricingResponse;
	}

	public void setPricingResponse(PricingResponse pricingResponse) {
		this.pricingResponse = pricingResponse;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
