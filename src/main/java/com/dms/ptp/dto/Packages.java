package com.dms.ptp.dto;

import java.util.List;

public class Packages {

    private String type;
    private int approvalKeyId;
    private String optimization;
    private int catalog_id;
    private int target_market;
    private int business_type;
    private String start;
    private String end;
    private int spot_length;
    private int weeks;
    private int spot_rate;
    private int spots;
    private int views;
    private double rate;
    private int cpt;
    private int tvr;
    private int cpp;
    private int price;
    private int rating;

    private List<DayPartModel> daypart;
    private List<SalesAreaOnCampaign> SalesAreaOnCampaign;
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public int getApprovalKeyId() {
        return approvalKeyId;
    }
    public void setApprovalKeyId(int approvalKeyId) {
        this.approvalKeyId = approvalKeyId;
    }
    public String getOptimization() {
        return optimization;
    }
    public void setOptimization(String optimization) {
        this.optimization = optimization;
    }
    public int getCatalog_id() {
        return catalog_id;
    }
    public void setCatalog_id(int catalog_id) {
        this.catalog_id = catalog_id;
    }
    public int getTarget_market() {
        return target_market;
    }
    public void setTarget_market(int target_market) {
        this.target_market = target_market;
    }
    public int getBusiness_type() {
        return business_type;
    }
    public void setBusiness_type(int business_type) {
        this.business_type = business_type;
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
    public int getSpot_length() {
        return spot_length;
    }
    public void setSpot_length(int spot_length) {
        this.spot_length = spot_length;
    }
    public int getWeeks() {
        return weeks;
    }
    public void setWeeks(int weeks) {
        this.weeks = weeks;
    }
    public int getSpot_rate() {
        return spot_rate;
    }
    public void setSpot_rate(int spot_rate) {
        this.spot_rate = spot_rate;
    }
    public int getSpots() {
        return spots;
    }
    public void setSpots(int spots) {
        this.spots = spots;
    }
    public int getViews() {
        return views;
    }
    public void setViews(int views) {
        this.views = views;
    }
    public double getRate() {
        return rate;
    }
    public void setRate(double rate) {
        this.rate = rate;
    }
    public int getCpt() {
        return cpt;
    }
    public void setCpt(int cpt) {
        this.cpt = cpt;
    }
    public int getTvr() {
        return tvr;
    }
    public void setTvr(int tvr) {
        this.tvr = tvr;
    }
    public int getCpp() {
        return cpp;
    }
    public void setCpp(int cpp) {
        this.cpp = cpp;
    }
    public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}
	public List<DayPartModel> getDaypart() {
        return daypart;
    }
    public void setDaypart(List<DayPartModel> daypart) {
        this.daypart = daypart;
    }
    public List<SalesAreaOnCampaign> getSalesAreaOnCampaign() {
        return SalesAreaOnCampaign;
    }
    public void setSalesAreaOnCampaign(List<SalesAreaOnCampaign> salesAreaOnCampaign) {
        SalesAreaOnCampaign = salesAreaOnCampaign;
    }

}
