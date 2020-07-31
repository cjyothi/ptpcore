package com.ptp.campaign.model;

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

}
