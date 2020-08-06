package com.dms.ptp.response;

public class SCEstRates {
    
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
    public SCEstRates() {
        super();
    }
    
    public SCEstRates(int demoId, double value) {
        super();
        this.demoId = demoId;
        this.value = value;
    }
    @Override
    public String toString() {
        return "SCEstRates [demoId=" + demoId + ", value=" + value + "]";
    }
    

}
