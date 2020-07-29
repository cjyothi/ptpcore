package com.dms.ptp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvailLMKRequest {

    private String startDate;
    private String endDate;
    private int salesArea;
    private int[] targetMarkets;
    
    
    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public int getSalesArea() {
        return salesArea;
    }
    public void setSalesArea(int salesArea) {
        this.salesArea = salesArea;
    }
    public int[] getTargetMarkets() {
        return targetMarkets;
    }
    public void setTargetMarkets(int[] targetMarkets) {
        this.targetMarkets = targetMarkets;
    }
    
    

}
