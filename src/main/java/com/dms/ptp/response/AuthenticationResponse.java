package com.dms.ptp.response;

import com.dms.ptp.dto.SignUpRequestModel;
import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResponse {

	private String accessToken;
	private String sessionToken;
	private String expiresIn;
	private String actualDate;
	private String expirationDate;
	private SignUpRequestModel userData;
	private String username;
	private String message;

	
	public AuthenticationResponse() {
		super();
	}

	public AuthenticationResponse(String accessToken, String expiresIn, String sessionToken, SignUpRequestModel userData) {
		super();
		this.accessToken = accessToken;
		this.expiresIn = expiresIn;
		this.sessionToken = sessionToken;
		this.userData = userData;
	}
	

	public String getExpirationDate() {
		return expirationDate;
	}


	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}


	public String getActualDate() {
		return actualDate;
	}


	public void setActualDate(String actualDate) {
		this.actualDate = actualDate;
	}


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public SignUpRequestModel getUserData() {
		return userData;
	}


	public void setUserData(SignUpRequestModel userData) {
		this.userData = userData;
	}


	public String getAccessToken() {
		return accessToken;
	}

	
	public void setAccessToken(String token) {
		this.accessToken = token;
	}

	
	public String getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(String expiresIn) {
		this.expiresIn = expiresIn;
	}


	public String getSessionToken() {
		return sessionToken;
	}


	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	
	
}