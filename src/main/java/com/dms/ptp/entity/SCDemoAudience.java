package com.dms.ptp.entity;

import javax.persistence.*;

@Entity
@Table(name="sc_demoaudience")
public class SCDemoAudience {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "weekPart")
    private String weekPart;
    
    @Column(name = "dayPart")
    private String dayPart;
    
    @Column(name = "rating")
    private double rating;
    
    @Column(name = "impact")
    private double impact;

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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getImpact() {
        return impact;
    }

    public void setImpact(double impact) {
        this.impact = impact;
    }

    @Override
    public String toString() {
        return "SCDemoAudience [id=" + id + ", weekPart=" + weekPart + ", dayPart=" + dayPart + ", rating=" + rating
                + ", impact=" + impact + "]";
    }
    

}
