/**
 * 
 */
package com.dms.ptp.response;

public class PasswordResponse {
	
	private String message;
	private String username;
	private String destination;
	private String deliveryMedium;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getDeliveryMedium() {
		return deliveryMedium;
	}
	public void setDeliveryMedium(String deliveryMedium) {
		this.deliveryMedium = deliveryMedium;
	}
	
	
}
