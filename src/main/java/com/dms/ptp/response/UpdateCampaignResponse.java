package com.dms.ptp.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class UpdateCampaignResponse {
    private int portalId ;
    private HttpStatus status;
    private String message;
    private String messageSeverity;
    private String roleType;
    private String campaignStatus;
    public int getPortalId() {
        return portalId;
    }
    public void setPortalId(int portalId) {
        this.portalId = portalId;
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
    public String getRoleType() {
        return roleType;
    }
    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }
    public String getCampaignStatus() {
        return campaignStatus;
    }
    public void setCampaignStatus(String campaignStatus) {
        this.campaignStatus = campaignStatus;
    }
    
    
}
