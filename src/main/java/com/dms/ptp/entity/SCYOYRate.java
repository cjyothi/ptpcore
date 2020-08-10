package com.dms.ptp.entity;

import javax.persistence.*;

@Entity
@Table(name="sc_yoy_rate")
public class SCYOYRate {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "weekPart")
    private String weekPart;
    
    @Column(name = "dayPart")
    private String dayPart;
    
    @Column(name = "rate")
    private double rate;

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

    public String getDayPart() {
        return dayPart;
    }

    public void setDayPart(String dayPart) {
        this.dayPart = dayPart;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "SCYOYRate [id=" + id + ", weekPart=" + weekPart + ", dayPart=" + dayPart + ", rate=" + rate + "]";
    }
    
    
}
