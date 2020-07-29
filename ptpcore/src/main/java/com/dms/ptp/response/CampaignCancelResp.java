package com.dms.ptp.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class CampaignCancelResp {
    private List<UploadCampaignResult> uploadCampaignResult;

    public List<UploadCampaignResult> getUploadCampaignResult() {
        return uploadCampaignResult;
    }

    public void setUploadCampaignResult(List<UploadCampaignResult> uploadCampaignResult) {
        this.uploadCampaignResult = uploadCampaignResult;
    }
    
    
}
