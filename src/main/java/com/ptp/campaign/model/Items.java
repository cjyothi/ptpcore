package com.ptp.campaign.model;

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
    private List<DayPart> daypart;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id", referencedColumnName = "id") 
    private List<CampaignSalesArea> salesAreaOnCampaign;
}
