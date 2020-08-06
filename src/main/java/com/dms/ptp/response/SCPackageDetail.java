package com.dms.ptp.response;

import java.util.List;

public class SCPackageDetail {
    
    private double cost;
    private int freq;
    private int totalSpots;
    private double spotRate;
    private double estValue;
    private int discount;
    
    private List<SCSpotsByWeekpart> spotsByWeekpart;
    private List<SCSplitByWeekpart> splitByWeekpart;
    private List<SCSplitByDaypart> splitByDaypart;
    
    
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
    public List<SCSpotsByWeekpart> getSpotsByWeekpart() {
        return spotsByWeekpart;
    }
    public void setSpotsByWeekpart(List<SCSpotsByWeekpart> spotsByWeekpart) {
        this.spotsByWeekpart = spotsByWeekpart;
    }
    public List<SCSplitByWeekpart> getSplitByWeekpart() {
        return splitByWeekpart;
    }
    public void setSplitByWeekpart(List<SCSplitByWeekpart> splitByWeekpart) {
        this.splitByWeekpart = splitByWeekpart;
    }
    public List<SCSplitByDaypart> getSplitByDaypart() {
        return splitByDaypart;
    }
    public void setSplitByDaypart(List<SCSplitByDaypart> splitByDaypart) {
        this.splitByDaypart = splitByDaypart;
    }
    public SCPackageDetail() {
        super();
    }
    @Override
    public String toString() {
        return "SCPackageDetail [cost=" + cost + ", freq=" + freq + ", totalSpots=" + totalSpots + ", spotRate="
                + spotRate + ", estValue=" + estValue + ", discount=" + discount + ", spotsByWeekpart="
                + spotsByWeekpart + ", splitByWeekpart=" + splitByWeekpart + ", splitByDaypart=" + splitByDaypart + "]";
    }
    
    
}
