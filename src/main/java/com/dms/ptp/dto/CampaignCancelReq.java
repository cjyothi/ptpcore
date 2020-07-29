package com.dms.ptp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CampaignCancelReq {
    private int appKeyId;
    private int campCode;
    
    public int getAppKeyId() {
        return appKeyId;
    }
    public void setAppKeyId(int appKeyId) {
        this.appKeyId = appKeyId;
    }
    public int getCampCode() {
        return campCode;
    }
    public void setCampCode(int campCode) {
        this.campCode = campCode;
    }
    
    

}
