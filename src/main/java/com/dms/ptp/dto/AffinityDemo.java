package com.dms.ptp.dto;

import java.util.List;

public class AffinityDemo {
    private int id;
    private List<DaypartData> daypart;
    private double impact;
    private double rating;
    private double affinityIndex;
    private double reachIndex;
    private double affinityReachIndex;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<DaypartData> getDaypart() {
        return daypart;
    }

    public void setDaypart(List<DaypartData> daypart) {
        this.daypart = daypart;
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

    public double getAffinityIndex() {
        return affinityIndex;
    }

    public void setAffinityIndex(double affinityIndex) {
        this.affinityIndex = affinityIndex;
    }

    public double getReachIndex() {
        return reachIndex;
    }

    public void setReachIndex(double reachIndex) {
        this.reachIndex = reachIndex;
    }

    public double getAffinityReachIndex() {
        return affinityReachIndex;
    }

    public void setAffinityReachIndex(double affinityReachIndex) {
        this.affinityReachIndex = affinityReachIndex;
    }

    public AffinityDemo(int id, List<DaypartData> daypart, double impact, double rating, double affinityIndex,
            double reachIndex, double affinityReachIndex) {
        super();
        this.id = id;
        this.daypart = daypart;
        this.impact = impact;
        this.rating = rating;
        this.affinityIndex = affinityIndex;
        this.reachIndex = reachIndex;
        this.affinityReachIndex = affinityReachIndex;
    }

    public AffinityDemo() {
        super();
    }
    
    
}
