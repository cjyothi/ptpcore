package com.dms.ptp.dto;

import java.util.List;

public class SellOut {
    private int id;
    private String name;
    private List<SellOutDaypart> rates;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SellOutDaypart> getRates() {
        return rates;
    }

    public void setRates(List<SellOutDaypart> rates) {
        this.rates = rates;
    }

}
