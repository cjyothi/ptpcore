package com.dms.ptp.response;

import org.springframework.http.HttpStatus;

public class UserApprovalResponse {
	
	private HttpStatus status;
	private String message;
	private boolean respFlag;
	
	public HttpStatus getStatus() {
		return status;
	}
	public void setStatus(HttpStatus status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isRespFlag() {
		return respFlag;
	}
	public void setRespFlag(boolean respFlag) {
		this.respFlag = respFlag;
	}

}
