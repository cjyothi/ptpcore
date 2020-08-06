package com.dms.ptp.response;

public class SourceData {
    private int demoId;
    private double rating;
    private double audience;
    private double cpt;
    private double cpp;

    public SourceData() {
    }

    public SourceData(int demoId, double rating, double audience, double cpt, double cpp) {
        this.demoId=demoId;
        this.rating=rating;
        this.audience=audience;
        this.cpt=cpt;
        this.cpp=cpp;
    }

    public int getDemoId() {
        return demoId;
    }

    public void setDemoId(int demoId) {
        this.demoId=demoId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating=rating;
    }

    public double getAudience() {
        return audience;
    }

    public void setAudience(double audience) {
        this.audience=audience;
    }

    public double getCpt() {
        return cpt;
    }

    public void setCpt(double cpt) {
        this.cpt=cpt;
    }

    public double getCpp() {
        return cpp;
    }

    public void setCpp(double cpp) {
        this.cpp=cpp;
    }
}
