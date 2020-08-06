package com.dms.ptp.response;

public class SCEstAudience {
    
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
    public void setValue(int value) {
        this.value = value;
    }
    public SCEstAudience() {
        super();
    }
    
    public SCEstAudience(int demoId, double value) {
        super();
        this.demoId = demoId;
        this.value = value;
    }
    @Override
    public String toString() {
        return "SCEstAudience [demoId=" + demoId + ", value=" + value + "]";
    }
    

}
