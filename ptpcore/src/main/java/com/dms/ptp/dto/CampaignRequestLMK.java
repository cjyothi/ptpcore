package com.dms.ptp.dto;

import java.util.List;

public class CampaignRequestLMK {
    
    private String title;
    private int portal_id;
    private int[] target_markets;
    private int deal_number;
    private int product_code;
    private String advertiser_code;
    private String objective;
    private String panel;
    private int budget;
    private double rating;
    private String start;
    private String end;
    private String source_start;
    private String source_end;

    private List<Packages> items;
    
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getPortal_id() {
        return portal_id;
    }
    public void setPortal_id(int portal_id) {
        this.portal_id = portal_id;
    }
    public int[] getTarget_markets() {
        return target_markets;
    }
    public void setTarget_markets(int[] target_markets) {
        this.target_markets = target_markets;
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
    public double getBudget() {
        return budget;
    }
    public void setBudget(int budget) {
        this.budget = budget;
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
	public List<Packages> getItems() {
		return items;
	}
	public void setItems(List<Packages> items) {
		this.items = items;
	}
 
}
