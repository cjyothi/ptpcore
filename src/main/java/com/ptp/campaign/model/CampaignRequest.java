package com.ptp.campaign.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CampaignRequest {

    private String title;
    private int[] target_markets;
    private int[] secondaryTargetMarkets;
    private int deal_number;
    private int product_code;
    private String advertiser_code;
    private String objective;
    private String panel;
    private int budget;
    private int length;
    private double rating;
    private String start;
    private String end;
    private String source_start;
    private String source_end;
    private List<Items> items;
    private String folderName;
    private String po;
    private List<CampaignRevision> revision;
    private List<FlightCode> flightCode;
    private int portalId;
    private String agencyCode;

}