package com.dms.ptp.response;

public class SellOutData {
    private String id;
    private double sellout;

    public SellOutData() {
    }

    public SellOutData(String id, double sellout) {
        this.id=id;
        this.sellout=sellout;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id=id;
    }

    public double getSellout() {
        return sellout;
    }

    public void setSellout(double sellout) {
        this.sellout=sellout;
    }
}
