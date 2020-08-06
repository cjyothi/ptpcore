package com.dms.ptp.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CatalogPricingDTO {
    private double rate;
    private int views;
    private int cpt;
    private int tvr;
    private int cpp;
    private String disclaimer;
    private int spots;
    private double spotRate;
    private int spotLength ;
    private String validityStart;
    private String validityEnd;
    private int rateCard;
    private int isActive;
    private List<String> shows;

    private List<SalesAreaDTO> salesAreaList;

    private List<DaypartDTO> daypart;


}
