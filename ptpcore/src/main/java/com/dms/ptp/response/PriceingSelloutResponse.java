package com.dms.ptp.response;

import java.util.List;

import com.dms.ptp.dto.SellOut;

public class PriceingSelloutResponse {
    private int total;
    private List<SellOut> items;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<SellOut> getItems() {
        return items;
    }

    public void setItems(List<SellOut> items) {
        this.items = items;
    }

}
