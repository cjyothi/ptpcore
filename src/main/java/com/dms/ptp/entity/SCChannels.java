package com.dms.ptp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="sc_channels")
public class SCChannels {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "channelId")
    private int channelId;
    
    @Column(name = "catalogId")
    private int catalogId;
    
    @Column(name = "affinity")
    private String affinity;
    
    @Column(name = "bouquet")
    private String bouquet;
    
    @Column(name = "name")
    private String name;
    
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "scPackageId", referencedColumnName = "id")
    private SCPackage scPackage;
    
    @OneToMany(targetEntity=SCYOYRate.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "scChannelId", referencedColumnName = "id", nullable=false)
    private List<SCYOYRate> scYOYRates;
    
    @OneToMany(targetEntity=SCQOQRate.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "scChannelId", referencedColumnName = "id", nullable=false)
    private List<SCQOQRate> scQOQRates;
    
    @OneToMany(targetEntity=SCNewRate.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "scChannelId", referencedColumnName = "id", nullable=false)
    private List<SCNewRate> scNewRates;
    
    @OneToMany(targetEntity=SCDemo.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "scChannelId", referencedColumnName = "id", nullable=false)
    private List<SCDemo> scDemos;
    
    @OneToMany(targetEntity=SCEstAudData.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "scChannelId", referencedColumnName = "id", nullable=false)
    private List<SCEstAudData> scEstAudData;

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

    public int getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(int catalogId) {
        this.catalogId = catalogId;
    }

    public String getAffinity() {
        return affinity;
    }

    public void setAffinity(String affinity) {
        this.affinity = affinity;
    }

    public String getBouquet() {
        return bouquet;
    }

    public void setBouquet(String bouquet) {
        this.bouquet = bouquet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SCPackage getScPackage() {
        return scPackage;
    }

    public void setScPackage(SCPackage scPackage) {
        this.scPackage = scPackage;
    }

    public List<SCYOYRate> getScYOYRates() {
        return scYOYRates;
    }

    public void setScYOYRates(List<SCYOYRate> scYOYRates) {
        this.scYOYRates = scYOYRates;
    }

    public List<SCQOQRate> getScQOQRates() {
        return scQOQRates;
    }

    public void setScQOQRates(List<SCQOQRate> scQOQRates) {
        this.scQOQRates = scQOQRates;
    }

    public List<SCNewRate> getScNewRates() {
        return scNewRates;
    }

    public void setScNewRates(List<SCNewRate> scNewRates) {
        this.scNewRates = scNewRates;
    }

    public List<SCDemo> getScDemos() {
        return scDemos;
    }

    public void setScDemos(List<SCDemo> scDemos) {
        this.scDemos = scDemos;
    }

    public List<SCEstAudData> getScEstAudData() {
        return scEstAudData;
    }

    public void setScEstAudData(List<SCEstAudData> scEstAudData) {
        this.scEstAudData = scEstAudData;
    }

    @Override
    public String toString() {
        return "SCChannels [id=" + id + ", channelId=" + channelId + ", catalogId=" + catalogId + ", affinity="
                + affinity + ", bouquet=" + bouquet + ", name=" + name + ", scPackage=" + scPackage + ", scYOYRates="
                + scYOYRates + ", scQOQRates=" + scQOQRates + ", scNewRates=" + scNewRates + ", scDemos=" + scDemos
                + ", scEstAudData=" + scEstAudData + "]";
    }
    

}
