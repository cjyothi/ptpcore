package com.dms.ptp.entity;

import javax.persistence.*;

@Entity
@Table(name="sc_spots_by_weekpart")
public class SCSpotsByWeekpart {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "weekPart")
    private String weekPart;
    
    @Column(name = "dayPart")
    private String dayPart;
    
    @Column(name = "spots")
    private int spots;

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

    public int getSpots() {
        return spots;
    }

    public void setSpots(int spots) {
        this.spots = spots;
    }

    @Override
    public String toString() {
        return "SCSpotsByWeekpart [id=" + id + ", weekPart=" + weekPart + ", dayPart=" + dayPart + ", spots=" + spots
                + "]";
    }
    

}
