package com.dms.ptp.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="aff_channel_demos")
public class AffChannelDemos {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "demo_id")
    private int demoId;
    
    @Column(name = "impact")
    private double impact;
    
    @Column(name = "rating")
    private double rating;
    
    @Column(name = "affinity_index")
    private double affinityIndex;
    
    @Column(name = "reach_index")
    private double reachIndex;
    
    @Column(name = "affinity_reach_index")
    private double affinityReachIndex;
    
    @OneToMany(targetEntity=AffChannelDayparts.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "affinity_channel_demo_id", referencedColumnName = "id", nullable=false)
    private List<AffChannelDayparts> affChannelDayparts;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getDemoId() {
        return demoId;
    }

    public void setDemoId(int demoId) {
        this.demoId = demoId;
    }

    public double getImpact() {
        return impact;
    }

    public void setImpact(double impact) {
        this.impact = impact;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getAffinityIndex() {
        return affinityIndex;
    }

    public void setAffinityIndex(double affinityIndex) {
        this.affinityIndex = affinityIndex;
    }

    public double getReachIndex() {
        return reachIndex;
    }

    public void setReachIndex(double reachIndex) {
        this.reachIndex = reachIndex;
    }

    public double getAffinityReachIndex() {
        return affinityReachIndex;
    }

    public void setAffinityReachIndex(double affinityReachIndex) {
        this.affinityReachIndex = affinityReachIndex;
    }

    public List<AffChannelDayparts> getAffChannelDayparts() {
        return affChannelDayparts;
    }

    public void setAffChannelDayparts(List<AffChannelDayparts> affChannelDayparts) {
        this.affChannelDayparts = affChannelDayparts;
    }

    
    @Override
    public String toString() {
        return "AffChannelDemos [id=" + id + ",  demoId=" + demoId + ", impact="
                + impact + ", rating=" + rating + ", affinityIndex=" + affinityIndex + ", reachIndex=" + reachIndex
                + ", affinityReachIndex=" + affinityReachIndex + ", affChannelDayparts=" + affChannelDayparts + "]";
    }

    public AffChannelDemos() {
        super();
    }

    public AffChannelDemos(int id, int demoId, double impact, double rating, double affinityIndex, double reachIndex,
            double affinityReachIndex, List<AffChannelDayparts> affChannelDayparts) {
        super();
        this.id = id;
        this.demoId = demoId;
        this.impact = impact;
        this.rating = rating;
        this.affinityIndex = affinityIndex;
        this.reachIndex = reachIndex;
        this.affinityReachIndex = affinityReachIndex;
        this.affChannelDayparts = affChannelDayparts;
    }
    
    
    
    
}
