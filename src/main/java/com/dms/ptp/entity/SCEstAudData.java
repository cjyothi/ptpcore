package com.dms.ptp.entity;

import javax.persistence.*;

@Entity
@Table(name="sc_est_aud_data")
public class SCEstAudData {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "demoId")
    private int demoId;
    
    @Column(name = "rating")
    private double rating;
    
    @Column(name = "audience")
    private double audience;
    
    @Column(name = "cpp")
    private double cpp;
    
    @Column(name = "cpt")
    private double cpt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDemoId() {
        return demoId;
    }

    public void setDemoId(int demoId) {
        this.demoId = demoId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getAudience() {
        return audience;
    }

    public void setAudience(double audience) {
        this.audience = audience;
    }

    public double getCpp() {
        return cpp;
    }

    public void setCpp(double cpp) {
        this.cpp = cpp;
    }

    public double getCpt() {
        return cpt;
    }

    public void setCpt(double cpt) {
        this.cpt = cpt;
    }

    @Override
    public String toString() {
        return "SCEstAudData [id=" + id + ", demoId=" + demoId + ", rating=" + rating + ", audience=" + audience
                + ", cpp=" + cpp + ", cpt=" + cpt + "]";
    }
    
    

}
