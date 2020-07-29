package com.dms.ptp.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CampaignApprovelmkReq {
   private List<Integer> campaignIds;

public List<Integer> getCampaignIds() {
    return campaignIds;
}

public void setCampaignIds(List<Integer> campaignIds) {
    this.campaignIds = campaignIds;
}
   
   
}
