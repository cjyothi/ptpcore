package com.dms.ptp.entity;

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

    @Column(name = "agency_code")
    private String agencyCode;
    
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    
    @Column(name = "modified_on")
    private LocalDateTime modifiedOn;

    @Column(name = "submitted_date")
    private LocalDateTime submittedDate;

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
    

    public int getPortal_id() {
        return portal_id;
    }

    public void setPortal_id(int portal_id) {
        this.portal_id = portal_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDeal_number() {
        return deal_number;
    }

    public void setDeal_number(int deal_number) {
        this.deal_number = deal_number;
    }

    public int getProduct_code() {
        return product_code;
    }

    public void setProduct_code(int product_code) {
        this.product_code = product_code;
    }

    public String getAdvertiser_code() {
        return advertiser_code;
    }

    public void setAdvertiser_code(String advertiser_code) {
        this.advertiser_code = advertiser_code;
    }

    public int getDelivery_currency_code() {
        return delivery_currency_code;
    }

    public void setDelivery_currency_code(int delivery_currency_code) {
        this.delivery_currency_code = delivery_currency_code;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getPanel() {
        return panel;
    }

    public void setPanel(String panel) {
        this.panel = panel;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getSource_start() {
        return source_start;
    }

    public void setSource_start(String source_start) {
        this.source_start = source_start;
    }

    public String getSource_end() {
        return source_end;
    }

    public void setSource_end(String source_end) {
        this.source_end = source_end;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPo() {
        return po;
    }

    public void setPo(String po) {
        this.po = po;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }

    public List<FlightCode> getFlightCode() {
        return flightCode;
    }

    public void setFlightCode(List<FlightCode> flightCode) {
        this.flightCode = flightCode;
    }

    public List<CampaignRevision> getRevision() {
        return revision;
    }

    public void setRevision(List<CampaignRevision> revision) {
        this.revision = revision;
    }

    public List<Integer> getTarget_markets() {
        return target_markets;
    }

    public void setTarget_markets(List<Integer> target_markets) {
        this.target_markets = target_markets;
    }

    public List<Integer> getSecondaryTargetMarkets() {
        return secondaryTargetMarkets;
    }

    public void setSecondaryTargetMarkets(List<Integer> secondaryTargetMarkets) {
        this.secondaryTargetMarkets = secondaryTargetMarkets;
    }

    public String getAgencyCode() {
        return agencyCode;
    }

    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }
    
    
    

}
