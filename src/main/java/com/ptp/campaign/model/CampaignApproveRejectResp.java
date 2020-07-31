package com.ptp.campaign.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
@Getter
@Setter
public class CampaignApproveRejectResp {
    private HttpStatus status;
    private String message;
}
