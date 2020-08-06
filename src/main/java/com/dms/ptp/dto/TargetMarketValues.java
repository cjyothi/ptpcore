package com.dms.ptp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TargetMarketValues {
    
    private int id;
    private int rating;
    private int impact;
    private int cpp;
    private int cpt;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getRating() {
        return rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    public int getImpact() {
        return impact;
    }
    public void setImpact(int impact) {
        this.impact = impact;
    }
    public int getCpp() {
        return cpp;
    }
    public void setCpp(int cpp) {
        this.cpp = cpp;
    }
    public int getCpt() {
        return cpt;
    }
    public void setCpt(int cpt) {
        this.cpt = cpt;
    }
    

}
