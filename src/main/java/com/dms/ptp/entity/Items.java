package com.dms.ptp.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="items")
public class Items {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "campaign_revision", referencedColumnName = "id") 
    private CampaignRevision campaign_revision;

    /*
     * //@ManyToOne
     * 
     * @JoinColumn(name = "campaign_id", referencedColumnName = "portal_id") private
     * Campaign campaign_id;
     */

    /*
     * @OneToOne
     * 
     * @JoinColumn(name = "package_id", referencedColumnName = "id") private
     * PackageEntity package_id;
     */

    @Column(name = "type")
    private String type;

    @Column(name = "approval_key")
    private int approvalKeyID;

    @Column(name = "optimization")
    private String optimization;

    @Column(name = "catalog_id")
    private int catalog_id;

    @Column(name = "target_market")
    private int target_market;

    @Column(name = "start")
    private String start;

    @Column(name = "end")
    private String end;

    @Column(name = "spot_length")
    private int spot_length;

    @Column(name = "weeks")
    private int weeks;

    @Column(name = "spot_rate")
    private int spot_rate;

    @Column(name = "spots")
    private int spots;

    @Column(name = "rate")
    private double rate;

    @Column(name = "views")
    private int views;

    @Column(name = "cpt")
    private int cpt;

    @Column(name = "cpp")
    private int cpp;

    @Column(name = "tvr")
    private int tvr;
    
    @Column(name = "price")
    private double price;

    @Column(name = "business_type")
    private int business_type;
    
    @Column(name = "campaign_code")
    private Integer campaign_code;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "message")
    private String message;
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private List<Spots> spotList;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private List<CampaignDaypart> daypart;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id", referencedColumnName = "id") 
    private List<CampaignSalesArea> salesAreaOnCampaign;
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private List<PackageAudienceForecast> demos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CampaignRevision getCampaign_revision() {
        return campaign_revision;
    }

    public void setCampaign_revision(CampaignRevision campaign_revision) {
        this.campaign_revision = campaign_revision;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getApprovalKeyID() {
        return approvalKeyID;
    }

    public void setApprovalKeyID(int approvalKeyID) {
        this.approvalKeyID = approvalKeyID;
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

    public int getCpp() {
        return cpp;
    }

    public void setCpp(int cpp) {
        this.cpp = cpp;
    }

    public int getTvr() {
        return tvr;
    }

    public void setTvr(int tvr) {
        this.tvr = tvr;
    }

    public int getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(int business_type) {
        this.business_type = business_type;
    }

    public Integer getCampaign_code() {
        return campaign_code;
    }

    public void setCampaign_code(Integer campaign_code) {
        this.campaign_code = campaign_code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Spots> getSpotList() {
        return spotList;
    }

    public void setSpotList(List<Spots> spotList) {
        this.spotList = spotList;
    }

    public List<CampaignDaypart> getDaypart() {
        return daypart;
    }

    public void setDaypart(List<CampaignDaypart> daypart) {
        this.daypart = daypart;
    }

    public List<CampaignSalesArea> getSalesAreaOnCampaign() {
        return salesAreaOnCampaign;
    }

    public void setSalesAreaOnCampaign(List<CampaignSalesArea> salesAreaOnCampaign) {
        this.salesAreaOnCampaign = salesAreaOnCampaign;
    }
    
    
}
