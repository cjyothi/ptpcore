package com.dms.ptp.response;

public class SCEstRating {
    
    private int demoId;
    private double value;
    
    public int getDemoId() {
        return demoId;
    }
    public void setDemoId(int demoId) {
        this.demoId = demoId;
    }
    public double getValue() {
        return value;
    }
    public void setValue(double value) {
        this.value = value;
    }
    public SCEstRating() {
        super();
    }
    
    public SCEstRating(int demoId, double value) {
        super();
        this.demoId = demoId;
        this.value = value;
    }
    @Override
    public String toString() {
        return "SCEstRating [demoId=" + demoId + ", value=" + value + "]";
    }
    

}
