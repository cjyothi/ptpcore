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
@Table(name="aff_channels")
public class AffChannels {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "channelId")
    private int channelId;
    
    @Column(name = "sec_demo_impact_total")
    private double secDemoImpactTotal;
    
    @Column(name = "res_demo_id")
    private int resultDemoId;
    
    @Column(name = "res_Segment")
    private String resultSegment;
    
    
    
    @OneToMany(targetEntity=AffChannelDemos.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "aff_channel_id", referencedColumnName = "id", nullable=false)
    private List<AffChannelDemos> affChannelDemos;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public double getSecDemoImpactTotal() {
        return secDemoImpactTotal;
    }

    public void setSecDemoImpactTotal(double secDemoImpactTotal) {
        this.secDemoImpactTotal = secDemoImpactTotal;
    }

    public int getResultDemoId() {
        return resultDemoId;
    }

    public void setResultDemoId(int resultDemoId) {
        this.resultDemoId = resultDemoId;
    }

    public String getResultSegment() {
        return resultSegment;
    }

    public void setResultSegment(String resultSegment) {
        this.resultSegment = resultSegment;
    }

   


    public List<AffChannelDemos> getAffChannelDemos() {
        return affChannelDemos;
    }

    public void setAffChannelDemos(List<AffChannelDemos> affChannelDemos) {
        this.affChannelDemos = affChannelDemos;
    }

    @Override
    public String toString() {
        return "AffChannels [id=" + id + ", channelId=" + channelId + ", secDemoImpactTotal=" + secDemoImpactTotal
                + ", resultDemoId=" + resultDemoId + ", resultSegment=" + resultSegment + ", affChannelDemos="
                + affChannelDemos + "]";
    }

    public AffChannels() {
        super();
    }
    

}
