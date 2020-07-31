package com.ptp.campaign.model;

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

	
    
      
     
}
