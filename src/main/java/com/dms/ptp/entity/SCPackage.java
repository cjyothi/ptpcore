package com.dms.ptp.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="sc_package")
public class SCPackage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "cost")
    private double cost;
    
    @Column(name = "freq")
    private int freq;
    
    @Column(name = "totalSpots")
    private int totalSpots;
    
    @Column(name = "spotRate")
    private double spotRate;
    
    @Column(name = "estValue")
    private double estValue;
    
    @Column(name = "discount")
    private int discount;
    
    @OneToMany(targetEntity=SCSpotsByWeekpart.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "scPackageId", referencedColumnName = "id", nullable=false)
    private List<SCSpotsByWeekpart> scSpotsByWeekPart;
    
    @OneToMany(targetEntity=SCSplitByWeekPart.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "scPackageId", referencedColumnName = "id", nullable=false)
    private List<SCSplitByWeekPart> scSplitByWeekPart;
    
    @OneToMany(targetEntity=SCSplitByDaypart.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "scPackageId", referencedColumnName = "id", nullable=false)
    private List<SCSplitByDaypart> scSplitByDayPart;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    public int getTotalSpots() {
        return totalSpots;
    }

    public void setTotalSpots(int totalSpots) {
        this.totalSpots = totalSpots;
    }

    public double getSpotRate() {
        return spotRate;
    }

    public void setSpotRate(double spotRate) {
        this.spotRate = spotRate;
    }

    public double getEstValue() {
        return estValue;
    }

    public void setEstValue(double estValue) {
        this.estValue = estValue;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public List<SCSpotsByWeekpart> getScSpotsByWeekPart() {
        return scSpotsByWeekPart;
    }

    public void setScSpotsByWeekPart(List<SCSpotsByWeekpart> scSpotsByWeekPart) {
        this.scSpotsByWeekPart = scSpotsByWeekPart;
    }

    public List<SCSplitByWeekPart> getScSplitByWeekPart() {
        return scSplitByWeekPart;
    }

    public void setScSplitByWeekPart(List<SCSplitByWeekPart> scSplitByWeekPart) {
        this.scSplitByWeekPart = scSplitByWeekPart;
    }

    public List<SCSplitByDaypart> getScSplitByDayPart() {
        return scSplitByDayPart;
    }

    public void setScSplitByDayPart(List<SCSplitByDaypart> scSplitByDayPart) {
        this.scSplitByDayPart = scSplitByDayPart;
    }

    @Override
    public String toString() {
        return "SCPackage [id=" + id + ", cost=" + cost + ", freq=" + freq + ", totalSpots=" + totalSpots
                + ", spotRate=" + spotRate + ", estValue=" + estValue + ", discount=" + discount
                + ", scSpotsByWeekPart=" + scSpotsByWeekPart + ", scSplitByWeekPart=" + scSplitByWeekPart
                + ", scSplitByDayPart=" + scSplitByDayPart + "]";
    }

    
}
