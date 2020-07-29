package com.dms.ptp.response;

import java.util.List;

import org.springframework.http.HttpStatus;

public class CampaignResponse {
	
    private String type;
	private HttpStatus status;
	private String message;
	private String messageSeverity;
	private int campaignCode;
	private String salesAreaName;
	private String batchBookingResponse;

	List<UploadCampaignResult> uploadCampaignResult;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<UploadCampaignResult> getUploadCampaignResult() {
        return uploadCampaignResult;
    }

    public void setUploadCampaignResult(List<UploadCampaignResult> uploadCampaignResult) {
        this.uploadCampaignResult = uploadCampaignResult;
    }

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

	public String getMessageSeverity() {
        return messageSeverity;
    }

    public void setMessageSeverity(String messageSeverity) {
        this.messageSeverity = messageSeverity;
    }

    public int getCampaignCode() {
		return campaignCode;
	}

	public void setCampaignCode(int campaignCode) {
		this.campaignCode = campaignCode;
	}

	public String getSalesAreaName() {
		return salesAreaName;
	}

	public void setSalesAreaName(String salesAreaName) {
		this.salesAreaName = salesAreaName;
	}

	public String getBatchBookingResponse() {
		return batchBookingResponse;
	}

	public void setBatchBookingResponse(String batchBookingResponse) {
		this.batchBookingResponse = batchBookingResponse;
	}

}
