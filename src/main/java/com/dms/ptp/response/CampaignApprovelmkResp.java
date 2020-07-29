package com.dms.ptp.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CampaignApprovelmkResp {
    private Integer campaignId;
     private String campaignStatus;
    public Integer getCampaignId() {
        return campaignId;
    }
    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }
    public String getCampaignStatus() {
        return campaignStatus;
    }
    public void setCampaignStatus(String campaignStatus) {
        this.campaignStatus = campaignStatus;
    }
     
     
}
