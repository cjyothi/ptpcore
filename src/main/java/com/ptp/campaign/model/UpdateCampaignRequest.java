package com.ptp.campaign.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCampaignRequest {
    private  int portalId;
    private CampaignRequest updatePayload;
}
