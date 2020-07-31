package com.ptp.campaign.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CampaignCancelReq {
    private int appKeyId;
    private int campCode;

}
