package com.dms.ptp.dto;

public class DemoAverage {

    private int id;
    private double averageImpact;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public double getAverageImpact() {
        return averageImpact;
    }
    public void setAverageImpact(double averageImpact) {
        this.averageImpact = averageImpact;
    }

    public DemoAverage(int id, double averageImpact) {
        super();
        this.id = id;
        this.averageImpact = averageImpact;
    }
    public DemoAverage() {
        super();
    }

}
