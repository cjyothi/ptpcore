package com.ptp.campaign.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AvailLMKResponse {
     private String total;
     private String type;
     private int businessType;
   //  private int target_market;
     private List<AvailItemLMKResponse> items;

}


