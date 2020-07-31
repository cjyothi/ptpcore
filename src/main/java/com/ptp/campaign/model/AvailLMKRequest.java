package com.ptp.campaign.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvailLMKRequest {

    private String startDate;
    private String endDate;
    private int salesArea;
    private int[] targetMarkets;

}
