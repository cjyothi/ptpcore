package com.dms.ptp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="campaign_salesarea")
public class CampaignSalesArea {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "channel_id")
    private int channelId;

    @Column(name = "sales_area")
    private int sales_area;

    @Column(name = "percentage_split")
    private int percentageSplit;

    @Column(name = "spot_percentage")
    private float spotPercentage;

    @Column(name = "rating")
    private float rating;

    @Column(name = "spot_length")
    private int spot_length;

    @Column(name = "spots")
    private int spots;

    @Column(name = "name")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getSales_area() {
        return sales_area;
    }

    public void setSales_area(int sales_area) {
        this.sales_area = sales_area;
    }

    public int getPercentageSplit() {
        return percentageSplit;
    }

    public void setPercentageSplit(int percentageSplit) {
        this.percentageSplit = percentageSplit;
    }

    public float getSpotPercentage() {
        return spotPercentage;
    }

    public void setSpotPercentage(float spotPercentage) {
        this.spotPercentage = spotPercentage;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getSpot_length() {
        return spot_length;
    }

    public void setSpot_length(int spot_length) {
        this.spot_length = spot_length;
    }

    public int getSpots() {
        return spots;
    }

    public void setSpots(int spots) {
        this.spots = spots;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    

}
