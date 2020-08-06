package com.dms.ptp.dto;

import java.util.List;

import com.dms.ptp.entity.CampaignRevision;
import com.dms.ptp.entity.FlightCode;
import com.dms.ptp.entity.Items;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CampaignRequest {

    private String title;
    private int[] target_markets;
    private int[] secondaryTargetMarkets;
    private int deal_number;
    private int product_code;
    private String advertiser_code;
    private String objective;
    private String panel;
    private int budget;
    private int length;
    private double rating;
    private String start;
    private String end;
    private String source_start;
    private String source_end;
    private List<Items> items;
    private String folderName;
    private String po;
    private List<CampaignRevision> revision;
    private List<FlightCode> flightCode;
    private int portalId;
    private String agencyCode;
    
    
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int[] getTarget_markets() {
        return target_markets;
    }
    public void setTarget_markets(int[] target_markets) {
        this.target_markets = target_markets;
    }
    public int[] getSecondaryTargetMarkets() {
        return secondaryTargetMarkets;
    }
    public void setSecondaryTargetMarkets(int[] secondaryTargetMarkets) {
        this.secondaryTargetMarkets = secondaryTargetMarkets;
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
    public int getBudget() {
        return budget;
    }
    public void setBudget(int budget) {
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
    public List<Items> getItems() {
        return items;
    }
    public void setItems(List<Items> items) {
        this.items = items;
    }
    public String getFolderName() {
        return folderName;
    }
    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
    public String getPo() {
        return po;
    }
    public void setPo(String po) {
        this.po = po;
    }
    public List<CampaignRevision> getRevision() {
        return revision;
    }
    public void setRevision(List<CampaignRevision> revision) {
        this.revision = revision;
    }
    public List<FlightCode> getFlightCode() {
        return flightCode;
    }
    public void setFlightCode(List<FlightCode> flightCode) {
        this.flightCode = flightCode;
    }
    public int getPortalId() {
        return portalId;
    }
    public void setPortalId(int portalId) {
        this.portalId = portalId;
    }
    public String getAgencyCode() {
        return agencyCode;
    }
    public void setAgencyCode(String agencyCode) {
        this.agencyCode = agencyCode;
    }
    
    
    

}