package com.dms.ptp.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CampaignApprovelmkRespList {
    List<CampaignApprovelmkResp> campaignStatusList;

    public List<CampaignApprovelmkResp> getCampaignStatusList() {
        return campaignStatusList;
    }

    public void setCampaignStatusList(List<CampaignApprovelmkResp> campaignStatusList) {
        this.campaignStatusList = campaignStatusList;
    }
    
    
}
