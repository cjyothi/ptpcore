package com.dms.ptp.response;

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
     
    public String getTotal() {
        return total;
    }
    public void setTotal(String total) {
        this.total = total;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public int getBusinessType() {
        return businessType;
    }
    public void setBusinessType(int businessType) {
        this.businessType = businessType;
    }
    public List<AvailItemLMKResponse> getItems() {
        return items;
    }
    public void setItems(List<AvailItemLMKResponse> items) {
        this.items = items;
    }
     

}


