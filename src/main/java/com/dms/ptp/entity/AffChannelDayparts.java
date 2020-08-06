package com.dms.ptp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="aff_channel_dayparts")
public class AffChannelDayparts {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "daypart_id")
    private String daypartId;
    
    @Column(name = "impact")
    private double impact;
    
    @Column(name = "rating")
    private double rating;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDaypartId() {
        return daypartId;
    }

    public void setDaypartId(String daypartId) {
        this.daypartId = daypartId;
    }

    public double getImpact() {
        return impact;
    }

    public void setImpact(double impact) {
        this.impact = impact;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "AffChannelDayparts [id=" + id + ",  daypartId="
                + daypartId + ", impact=" + impact + ", rating=" + rating + "]";
    }

    public AffChannelDayparts() {
        super();
    }
    
    

}
