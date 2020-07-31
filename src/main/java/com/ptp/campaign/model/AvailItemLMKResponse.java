package com.ptp.campaign.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvailItemLMKResponse {

    private int sales_area_no;
    private String sales_area_name;
  //  private int target_market;
    private int price;
    private int program_no;
    private String program_name;
    private int start_time;
    private int end_time;
    private long rating;
    private long impact;
    private long cpp;
    private long cpt;
    private String scheduled_on;
    private int scheduled_time;
    private String day;
    private int universe;
    private List<TargetMarketValues> demos;

}
