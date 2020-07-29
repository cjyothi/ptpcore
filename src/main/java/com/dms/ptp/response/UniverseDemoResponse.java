package com.dms.ptp.response;

import java.util.List;

import com.dms.ptp.dto.UnivrseDemoDto;
import com.dms.ptp.entity.PriceDemo;

public class UniverseDemoResponse {
    private int total;
    private List<UnivrseDemoDto> items;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<UnivrseDemoDto> getItems() {
        return items;
    }

    public void setItems(List<UnivrseDemoDto> items) {
        this.items = items;
    }

}
