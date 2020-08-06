package com.dms.ptp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="aff_universe")
public class AffUniverse {
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "demoId")
    private int demoId;
    
    @Column(name = "universe")
    private int universe;
    
    @Column(name = "avgImpact")
    private double avgImpact;

    

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

    public int getUniverse() {
        return universe;
    }

    public void setUniverse(int universe) {
        this.universe = universe;
    }

    public double getAvgImpact() {
        return avgImpact;
    }

    public void setAvgImpact(double avgImpact) {
        this.avgImpact = avgImpact;
    }


    @Override
    public String toString() {
        return "AffUniverse [id=" + id + ", demoId=" + demoId + ", universe=" + universe + ", avgImpact=" + avgImpact
                + "]";
    }

    public AffUniverse() {
        super();
    }
    
    

}
