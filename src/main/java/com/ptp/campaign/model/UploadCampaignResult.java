package com.ptp.campaign.model;

public class UploadCampaignResult {
	
    private String type;
    private String salesAreaName;
	private int approvalKeyID;
    private int campaignCode;
    private String campaignStatus;
    private String campaignIntendedAction;
    ResultMessageList resultMessages;
	
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getSalesAreaName() {
		return salesAreaName;
	}
	public void setSalesAreaName(String salesAreaName) {
		this.salesAreaName = salesAreaName;
	}
	public int getApprovalKeyID() {
		return approvalKeyID;
	}
	public void setApprovalKeyID(int approvalKeyID) {
		this.approvalKeyID = approvalKeyID;
	}
	public int getCampaignCode() {
		return campaignCode;
	}
	public void setCampaignCode(int campaignCode) {
		this.campaignCode = campaignCode;
	}
	public String getCampaignStatus() {
		return campaignStatus;
	}
	public void setCampaignStatus(String campaignStatus) {
		this.campaignStatus = campaignStatus;
	}
	public String getCampaignIntendedAction() {
		return campaignIntendedAction;
	}
	public void setCampaignIntendedAction(String campaignIntendedAction) {
		this.campaignIntendedAction = campaignIntendedAction;
	}
	public ResultMessageList getResultMessages() {
		return resultMessages;
	}
	public void setResultMessages(ResultMessageList resultMessages) {
		this.resultMessages = resultMessages;
	}
	
}
