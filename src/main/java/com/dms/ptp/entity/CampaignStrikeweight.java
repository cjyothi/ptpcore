package com.dms.ptp.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="campaign_strikeweight")
public class CampaignStrikeweight {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "campaign_salesarea_id")
	private int salesAreaId;
	
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id") 
    private CampaignSalesArea campaign_salesarea_id;

	@Column(name = "start")
	private Date start;
	
	@Column(name = "end")
	private Date end;
	
	@Column(name = "rating_percentage")
	private float ratingPercentage;
	
	@Column(name = "spot_percentage")
	private float spotPercentage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSalesAreaId() {
        return salesAreaId;
    }

    public void setSalesAreaId(int salesAreaId) {
        this.salesAreaId = salesAreaId;
    }

    public CampaignSalesArea getCampaign_salesarea_id() {
        return campaign_salesarea_id;
    }

    public void setCampaign_salesarea_id(CampaignSalesArea campaign_salesarea_id) {
        this.campaign_salesarea_id = campaign_salesarea_id;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public float getRatingPercentage() {
        return ratingPercentage;
    }

    public void setRatingPercentage(float ratingPercentage) {
        this.ratingPercentage = ratingPercentage;
    }

    public float getSpotPercentage() {
        return spotPercentage;
    }

    public void setSpotPercentage(float spotPercentage) {
        this.spotPercentage = spotPercentage;
    }

	
}
