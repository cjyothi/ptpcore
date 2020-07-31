package com.ptp.campaign.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class UpdateCampaignResponse {
    private int portalId ;
    private HttpStatus status;
    private String message;
    private String messageSeverity;
    private String roleType;
    private String campaignStatus;

}
