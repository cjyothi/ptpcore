package com.dms.ptp.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CatalogRequest {

	private int id;
	private String title1;
	private String title2;
	private String shortDescr;
	private int businessType;
	
	private String type;
	private String graphic;
	private String logo;
	private List<DaypartDTO> daypart;
	private String exclude;
	private List<PlansDTO> plans;
	private List<String> shows;
	private double rate;
	private int views;
	private int cpt;
	private int tvr;
	private int cpp;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitle1() {
        return title1;
    }
    public void setTitle1(String title1) {
        this.title1 = title1;
    }
    public String getTitle2() {
        return title2;
    }
    public void setTitle2(String title2) {
        this.title2 = title2;
    }
    public String getShortDescr() {
        return shortDescr;
    }
    public void setShortDescr(String shortDescr) {
        this.shortDescr = shortDescr;
    }
    public int getBusinessType() {
        return businessType;
    }
    public void setBusinessType(int businessType) {
        this.businessType = businessType;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getGraphic() {
        return graphic;
    }
    public void setGraphic(String graphic) {
        this.graphic = graphic;
    }
    public String getLogo() {
        return logo;
    }
    public void setLogo(String logo) {
        this.logo = logo;
    }
    public List<DaypartDTO> getDaypart() {
        return daypart;
    }
    public void setDaypart(List<DaypartDTO> daypart) {
        this.daypart = daypart;
    }
    public String getExclude() {
        return exclude;
    }
    public void setExclude(String exclude) {
        this.exclude = exclude;
    }
    public List<PlansDTO> getPlans() {
        return plans;
    }
    public void setPlans(List<PlansDTO> plans) {
        this.plans = plans;
    }
    public List<String> getShows() {
        return shows;
    }
    public void setShows(List<String> shows) {
        this.shows = shows;
    }
    public double getRate() {
        return rate;
    }
    public void setRate(double rate) {
        this.rate = rate;
    }
    public int getViews() {
        return views;
    }
    public void setViews(int views) {
        this.views = views;
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
	
	
}
