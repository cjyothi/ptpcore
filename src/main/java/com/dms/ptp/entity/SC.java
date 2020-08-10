package com.dms.ptp.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="sc")
public class SC extends AffinityAuditable<String>{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "component")
    private String component;
    
    @Column(name = "baseline1")
    private int baseline1;
    
    @Column(name = "baseline2")
    private int baseline2;
    
    @Column(name = "type")
    private String type;
    
    @Column(name = "qoqRateCard")
    private int qoqRateCard;
    
    @Column(name = "yoyRateCard")
    private int yoyRateCard;
    
    @Column(name = "qoqBaseline1")
    private int qoqBaseline1;
    
    @Column(name = "qoqBaseline2")
    private int qoqBaseline2;
    
    @Column(name = "yoyBaseline1")
    private int yoyBaseline1;
    
    @Column(name = "yoyBaseline2")
    private int yoyBaseline2;
    
    @Column(name = "week")
    private int week;
    
    @Column(name = "spotLength")
    private int spotLength;
    
    @Column(name = "rateCardId")
    private int rateCardId;
    
    @Column(name = "createdBy")
    private int createdBy;
    
    @Column(name = "lastModifiedBy")
    private int lastModifiedBy;
    
    @OneToMany(targetEntity=SCChannels.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "scId", referencedColumnName = "id", nullable=false)
    private List<SCChannels> scChannels;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public int getBaseline1() {
        return baseline1;
    }

    public void setBaseline1(int baseline1) {
        this.baseline1 = baseline1;
    }

    public int getBaseline2() {
        return baseline2;
    }

    public void setBaseline2(int baseline2) {
        this.baseline2 = baseline2;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQoqRateCard() {
        return qoqRateCard;
    }

    public void setQoqRateCard(int qoqRateCard) {
        this.qoqRateCard = qoqRateCard;
    }

    public int getYoyRateCard() {
        return yoyRateCard;
    }

    public void setYoyRateCard(int yoyRateCard) {
        this.yoyRateCard = yoyRateCard;
    }

    public int getQoqBaseline1() {
        return qoqBaseline1;
    }

    public void setQoqBaseline1(int qoqBaseline1) {
        this.qoqBaseline1 = qoqBaseline1;
    }

    public int getQoqBaseline2() {
        return qoqBaseline2;
    }

    public void setQoqBaseline2(int qoqBaseline2) {
        this.qoqBaseline2 = qoqBaseline2;
    }

    public int getYoyBaseline1() {
        return yoyBaseline1;
    }

    public void setYoyBaseline1(int yoyBaseline1) {
        this.yoyBaseline1 = yoyBaseline1;
    }

    public int getYoyBaseline2() {
        return yoyBaseline2;
    }

    public void setYoyBaseline2(int yoyBaseline2) {
        this.yoyBaseline2 = yoyBaseline2;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getSpotLength() {
        return spotLength;
    }

    public void setSpotLength(int spotLength) {
        this.spotLength = spotLength;
    }

    public int getRateCardId() {
        return rateCardId;
    }

    public void setRateCardId(int rateCardId) {
        this.rateCardId = rateCardId;
    }

    public List<SCChannels> getScChannels() {
        return scChannels;
    }

    public void setScChannels(List<SCChannels> scChannels) {
        this.scChannels = scChannels;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public int getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(int lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    @Override
    public String toString() {
        return "SC [id=" + id + ", component=" + component + ", baseline1=" + baseline1 + ", baseline2=" + baseline2
                + ", type=" + type + ", qoqRateCard=" + qoqRateCard + ", yoyRateCard=" + yoyRateCard + ", qoqBaseline1="
                + qoqBaseline1 + ", qoqBaseline2=" + qoqBaseline2 + ", yoyBaseline1=" + yoyBaseline1 + ", yoyBaseline2="
                + yoyBaseline2 + ", week=" + week + ", spotLength=" + spotLength + ", rateCardId=" + rateCardId
                + ", createdBy=" + createdBy + ", lastModifiedBy=" + lastModifiedBy + ", scChannels=" + scChannels
                + "]";
    }

    
    
}
