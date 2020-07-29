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
@Table(name="aff")
public class Aff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "territoryId")
    private int territoryId;
    
    @Column(name = "platformId")
    private int platformId;
    
    @Column(name = "sourceStart")
    private String sourceStart;
    
    @Column(name = "sourceEnd")
    private String sourceEnd;
    
    @Column(name = "weekpart")
    private String weekpart;
    
    @Column(name = "panel")
    private int panel;
    
    @OneToMany(targetEntity=AffDemos.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "affinityId", referencedColumnName = "id", nullable=false)
    private List<AffDemos> affDemos;
    
    @OneToMany(targetEntity=AffUniverse.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "affinityId", referencedColumnName = "id", nullable=false)
    private List<AffUniverse> affUniverse;
    
    @OneToMany(targetEntity=AffChannels.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "affinityId", referencedColumnName = "id", nullable=false)
    private List<AffChannels> affChannels;
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getTerritoryId() {
        return territoryId;
    }

    public void setTerritoryId(int territoryId) {
        this.territoryId = territoryId;
    }

    public int getPlatformId() {
        return platformId;
    }

    public void setPlatformId(int platformId) {
        this.platformId = platformId;
    }

    public String getSourceStart() {
        return sourceStart;
    }

    public void setSourceStart(String sourceStart) {
        this.sourceStart = sourceStart;
    }

    public String getSourceEnd() {
        return sourceEnd;
    }

    public void setSourceEnd(String sourceEnd) {
        this.sourceEnd = sourceEnd;
    }
    
    public List<AffDemos> getAffDemos() {
        return affDemos;
    }

    public void setAffDemos(List<AffDemos> affDemos) {
        this.affDemos = affDemos;
    }

    public List<AffUniverse> getAffUniverse() {
        return affUniverse;
    }

    public void setAffUniverse(List<AffUniverse> affUniverse) {
        this.affUniverse = affUniverse;
    }

    public List<AffChannels> getAffChannels() {
        return affChannels;
    }

    public void setAffChannels(List<AffChannels> affChannels) {
        this.affChannels = affChannels;
    }

    public String getWeekpart() {
        return weekpart;
    }

    public void setWeekpart(String weekpart) {
        this.weekpart = weekpart;
    }

    public int getPanel() {
        return panel;
    }

    public void setPanel(int panel) {
        this.panel = panel;
    }
    

    @Override
    public String toString() {
        return "Aff [id=" + id + ", territoryId=" + territoryId + ", platformId=" + platformId + ", sourceStart="
                + sourceStart + ", sourceEnd=" + sourceEnd + ", weekpart=" + weekpart + ", panel=" + panel
                + ", affDemos=" + affDemos + ", affUniverse=" + affUniverse + ", affChannels=" + affChannels + "]";
    }

    public Aff() {
        super();
    }
    
    
    
    
}
