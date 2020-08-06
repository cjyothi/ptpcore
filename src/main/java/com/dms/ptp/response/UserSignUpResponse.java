/**
 * 
 */
package com.dms.ptp.response;

import java.util.Date;


public class UserSignUpResponse {
	
	private String username;
	private Date userCreatedDate;
	private Date lastModifiedDate;
	private boolean enabled;
	private String userStatus;
	private String password;
	private String email;
	private String designation;
	private String region;
	private String status;
	private int userRegion;
	private String userid;
	private String agencyCode;
	private String agencyName;
	private String role;

	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}



	public Date getUserCreatedDate() {
		return userCreatedDate;
	}



	public void setUserCreatedDate(Date userCreatedDate) {
		this.userCreatedDate = userCreatedDate;
	}



	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}



	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}



	public boolean isEnabled() {
		return enabled;
	}



	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}



	public String getUserStatus() {
		return userStatus;
	}



	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getDesignation() {
		return designation;
	}



	public void setDesignation(String designation) {
		this.designation = designation;
	}



	public String getRegion() {
		return region;
	}



	public void setRegion(String region) {
		this.region = region;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public int getUserRegion() {
		return userRegion;
	}



	public void setUserRegion(int userRegion) {
		this.userRegion = userRegion;
	}



	public String getUserid() {
		return userid;
	}



	public void setUserid(String userid) {
		this.userid = userid;
	}



	public String getAgencyCode() {
		return agencyCode;
	}



	public void setAgencyCode(String agencyCode) {
		this.agencyCode = agencyCode;
	}



	public String getAgencyName() {
		return agencyName;
	}



	public void setAgencyName(String agencyName) {
		this.agencyName = agencyName;
	}



	public String getRole() {
		return role;
	}



	public void setRole(String role) {
		this.role = role;
	}



	@Override
	public String toString() {
		return "UserSignUpResponse [username=" + username + ", userCreatedDate=" + userCreatedDate
				+ ", lastModifiedDate=" + lastModifiedDate + ", enabled=" + enabled + ", userStatus=" + userStatus
				+ ", email=" + email + ", designation=" + designation + ", region=" + region + ", status=" + status + ", "
						+ "agencyCode=" + agencyCode + ", agencyName=" + agencyName + "]";
	}

}
