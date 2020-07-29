package com.dms.ptp.dto;

public class DaypartData {


    private String id;
    private double impact;
    private double rating;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public double getImpact() {
        return impact;
    }
    public void setImpact(double impact) {
        this.impact = impact;
    }
    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
    public DaypartData(String id, double impact, double rating) {
        super();
        this.id = id;
        this.impact = impact;
        this.rating = rating;
    }
    @Override
    public String toString() {
        return "DaypartData [id=" + id + ", impact=" + impact + ", rating=" + rating + "]";
    }
    public DaypartData() {
        super();
    }


}
