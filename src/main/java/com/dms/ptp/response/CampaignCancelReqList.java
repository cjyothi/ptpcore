package com.dms.ptp.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.dms.ptp.dto.CampaignCancelReq;

@Getter
@Setter
public class CampaignCancelReqList {
    private List<CampaignCancelReq> Req;

    public List<CampaignCancelReq> getReq() {
        return Req;
    }

    public void setReq(List<CampaignCancelReq> req) {
        Req = req;
    }
    
    
}
