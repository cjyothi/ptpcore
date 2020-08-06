package com.dms.ptp.util;

public class Constant {
	private Constant() {
	}

	public static final String CATALOG_NOT_FOUND = "Catalog Not Found ";
	public static final String DELETED_SUCCESSFULLY ="Deleted successfully";
	public static final String DB_EXCEPTION_MSG="Exception while performing DB operations";
	
	// User Management
	
	public static final String USERNAME = "USERNAME";
    public static final String PASSWORD ="PASSWORD";
    public static final String NEW_PASS_WORD_KEY = "newPassword";
    public static final String ACCESS_TOKEN_KEY = "accessToken";
    
    public static final String PENDING = "Pending";
    
    /**Response Messages**/
    public static final String USER_APPROVAL_ACCEPT= "User Approved Successfully";
    public static final String USER_APPROVAL_REJECT= "User rejected.";
    public static final String VALID_OTP = "OTP is valid.";
    public static final String INVALID_OTP = "Invalid OTP. Please try again.";
    public static final String VALID_LOGIN = "Login was successful";
    public static final String INVALID_LOGIN = "Login was unsuccessful";
    public static final String JWT_EXCEPTION = "No JWT token found in request headers";
    public static final String USER_DETAILS_MSG= "user details updated successfully";
    
    /**Exception Messages**/
    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_NAME_EXISTS = "An account with this Email address already exists";
    public static final String INVALID_PASSWORD = "The password must include: "
            + "a. Atleast one uppercase, one lowercase, one number and one special character" 
            + "b. Minimum of 8 characters and Maximum of 20 characters";
    public static final String INVALID_PARAMATER = "Please enter the number in the following format: +27 011 25975648";
    public static final String INVALID_LOGIN_CREDENTIALS = "Invalid Login credentials";
    public static final String USER_REQUEST_ALRDY_SUBMITTED = "user request is already submitted";
    
    public static final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,20})";
    public static final String PHONE_NUM_PATTERN = "^\\+(?:[0-9] ?){6,14}[0-9]$";
    
    public static final String PASSWORD_RESET = "Password reset is successful!";
    public static final String LOGOUT = "logged out successfully!";
    
    
    // Campaign
    
    public static final String CAMPAIGN_CREATED = "Campaign creation successful";
    public static final String CAMPAIGN_NOT_CREATED = "Campaign creation was unsuccessful";
    public static final String ACTION_DRAFT = "draft";
    public static final String CAMPAIGN_APPROVAL_ACCEPT = "Campaign Approved Successfully";
    public static final String CAMPAIGN_APPROVAL_REJECT = "Campaign Approval was Unsuccessful.";
    public static final String SALES_AREA_EXCEPTION = "Both Sales Area Number & Sales Area Name cannot be null. Please provide at least one input";

    public static final String FILES_UPLOADED = "file uploaded successfully";
    public static final String APPROVAL_COMPLETED = "Approval Completed";
    public static final String CAMPAIGN_NOT_IN_DRAFT = "Campaign not in Draft state";
    public static final String CAMPAIGN_NOT_UPDTAED = "Campaign not Updated";
    public static final String CAMPAIGN_SUBMITTED = "campaign submitted by Media Planner";
    public static final String CAMPAIGN_AMENDED = "campaign amended by Contractor";
    public static final String CAMPAIGN_DRAFT_UPDATE = "Campaign Draft updated successfully";
    public static final String CAMPAIGN_STATUS_SUBMITTED = "submitted";
    public static final String CAMPAIGN_STATUS_AMENDED = "amended";
    public static final String CAMPAIGN_STATUS_DRAFT = "draft";
    

    public static final String SUCCESS = "Success";
    public static final String WARNING = "WARNING";
    public static final String EXPECTATION_FAILED = "EXPECTATION_FAILED";
    public static final String ERROR = "ERROR";

    /** Package types **/
    public static final String PACKAGE_TYPE_GTT = "gtt";
    public static final String PACKAGE_TYPE_SPOT_BUNDLE = "spotbundle";
    public static final String PACKAGE_TYPE_SPOT_CHANNEL = "spotchannel";

    /** User Roles **/
	public static final String ROLE_ADMIN = "Admin";
	public static final String ROLE_AGENCY_ADMIN = "Agency Admin";
    public static final String ROLE_DMS_ADMIN = "DMS Admin";
    public static final String ROLE_AGENCY_MEDIA_PLANNER = "Agency Media Planner";
    public static final String ROLE_DMS_MEDIA_PLANNER = "DMS Media Planner";
    public static final String ROLE_CONTRACTOR = "Contractor";

    /** File types **/
    public static final String FILE_TYPE_TELMAR = "Telmar";
    public static final String FILE_TYPE_ARIANNA = "Arianna";
    public static final String FILE_TYPE_PROFILE = "profile";

    /**Approve and reject **/
    public static final String APP_REJ_FAIL_MSG = "Items in the plan approved partially due to limitations. Check and approve the remaining items in booking system manually.";
    public static final String REJ_FAILED_MSG = "Failed cancel campaign due to internal server error.";
    public static final String REJ_SUCCESS_MSG = "Campaign rejected successfully due to following reason - ";
    public static final String REQ_FAIL_MSG ="Request failed due to internal server error.";
}
