package com.ptp.campaign.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class CampaignCancelResp {
    private List<UploadCampaignResult> uploadCampaignResult;
}
