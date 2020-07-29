package com.dms.ptp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CampaignApproveReject {
    private int portalId;
    private String userName;
    private String reason;
    
    public int getPortalId() {
        return portalId;
    }
    public void setPortalId(int portalId) {
        this.portalId = portalId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getReason() {
        return reason;
    }
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    
}
