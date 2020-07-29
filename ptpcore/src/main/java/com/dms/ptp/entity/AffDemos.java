package com.dms.ptp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="aff_demos")
public class AffDemos {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "demoId")
    private int demoId;
    
    @Column(name = "isPrimary")
    private boolean isPrimary;


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

    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean isPrimary) {
        this.isPrimary = isPrimary;
    }

    

    @Override
    public String toString() {
        return "AffDemos [id=" + id + ", demoId=" + demoId + ", isPrimary=" + isPrimary + "]";
    }

    public AffDemos() {
        super();
    }

    public AffDemos(int id, int demoId, boolean isPrimary) {
        super();
        this.id = id;
        this.demoId = demoId;
        this.isPrimary = isPrimary;
    }
    

}
