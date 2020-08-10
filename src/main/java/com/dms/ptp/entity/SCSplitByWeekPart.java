package com.dms.ptp.entity;

import javax.persistence.*;

@Entity
@Table(name="sc_split_by_weekpart")
public class SCSplitByWeekPart {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "weekPart")
    private String weekPart;
    
    @Column(name = "percentage")
    private int percentage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWeekPart() {
        return weekPart;
    }

    public void setWeekPart(String weekPart) {
        this.weekPart = weekPart;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return "SCSplitByWeekPart [id=" + id + ", weekPart=" + weekPart + ", percentage=" + percentage + "]";
    }
    
    
}
