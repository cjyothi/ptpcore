package com.dms.ptp.dto;

import java.util.List;

public class Rates {
    private int id;
    private String name;
    private List<RateDayPart> rates;

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

    public List<RateDayPart> getRates() {
        return rates;
    }

    public void setRates(List<RateDayPart> rates) {
        this.rates = rates;
    }

}
