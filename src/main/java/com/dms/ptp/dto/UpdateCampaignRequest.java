package com.dms.ptp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCampaignRequest {
    private  int portalId;
    private CampaignRequest updatePayload;
    
    public int getPortalId() {
        return portalId;
    }
    public void setPortalId(int portalId) {
        this.portalId = portalId;
    }
    public CampaignRequest getUpdatePayload() {
        return updatePayload;
    }
    public void setUpdatePayload(CampaignRequest updatePayload) {
        this.updatePayload = updatePayload;
    }
    
    
}
