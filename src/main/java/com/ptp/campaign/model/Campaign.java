package com.ptp.campaign.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "campaign")
public class Campaign{


    @Id
    @Column(name = "portal_id")
    private int portal_id;

    @Column(name = "title")
    private String title;

    @Column(name = "deal_number")
    private int deal_number;

    @Column(name = "product_code")
    private int product_code;

    @Column(name = "advertiser_code")
    private String advertiser_code;

    @Column(name = "delivery_currency_code")
    private int delivery_currency_code;

    @Column(name = "objective")
    private String objective;

    @Column(name = "panel")
    private String panel;

    @Column(name = "budget")
    private double budget;

    @Column(name = "length")
    private int length;

    @Column(name = "rating")
    private double rating;

    @Column(name = "start")
    private String start;

    @Column(name = "end")
    private String end;

    @Column(name = "source_start")
    private String source_start;

    @Column(name = "source_end")
    private String source_end;

    @Column(name = "currency")
    private String currency;

    @Column(name = "status")
    private int status;
    
    @Column(name = "po")
    private String po;
    
    @Column(name = "user_id")
    private Integer userId;
    
    @Column(name = "folder_name")
    private String folderName;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id", referencedColumnName = "portal_id") 
    private List<Items> items;
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "portal_id", referencedColumnName = "portal_id")
    private List<FlightCode> flightCode;
    
    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "portal_id", referencedColumnName = "portal_id")
    private List<CampaignRevision> revision;
     
    
    @ElementCollection
    private List<Integer> target_markets;
    
    
    @ElementCollection
    private List<Integer> secondaryTargetMarkets;

    /*
     * @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
     * 
     * @JoinColumn(name = "campaign_id", referencedColumnName = "portal_id") private
     * List<CampaignDemo> target_markets;
     */
    
	/*
	 * @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "campaign_id", referencedColumnName = "portal_id") private
	 * List<CampaignSalesArea> sales_area;
	 */
    
    @Column(name = "agency_code")
    private String agencyCode;
    
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    
    @Column(name = "modified_on")
    private LocalDateTime modifiedOn;

    @Column(name = "submitted_date")
    private LocalDateTime submittedDate;
}
