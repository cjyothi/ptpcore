package com.dms.ptp.response;

import java.util.List;

import com.dms.ptp.dto.Rates;

public class PriceingRatesResponse {
    private int total;
    private List<Rates> items;

    public PriceingRatesResponse() {
        super();
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Rates> getItems() {
        return items;
    }

    public void setItems(List<Rates> items) {
        this.items = items;
    }

    public PriceingRatesResponse(int total, List<Rates> items) {
        super();
        this.total = total;
        this.items = items;
    }

}
