package com.dms.ptp.response;

import java.util.List;

public class SpotChannels {
    
    private int channelId;
    private String affinity;
    private String bouquet;
    private String name;
    
    private List<SCLooseSpotRate> looseSpotYOY;
    private List<SCLooseSpotRate> looseSpotQOQ;
    private List<SCLooseSpotRate> looseSpotNew;
    
    private List<SCDemoDetail> demoDetail;
    private SCPackageDetail packageDetail;
    
    private List<SCEstRating> estRating;
    private List<SCEstAudience> estAudience;
    private List<SCEstRates> estCPP;
    private List<SCEstRates> estCPT;
    
    
    public int getChannelId() {
        return channelId;
    }
    public void setChannelId(int channelId) {
        this.channelId = channelId;
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
    public List<SCLooseSpotRate> getLooseSpotYOY() {
        return looseSpotYOY;
    }
    public void setLooseSpotYOY(List<SCLooseSpotRate> looseSpotYOY) {
        this.looseSpotYOY = looseSpotYOY;
    }
    public List<SCLooseSpotRate> getLooseSpotQOQ() {
        return looseSpotQOQ;
    }
    public void setLooseSpotQOQ(List<SCLooseSpotRate> looseSpotQOQ) {
        this.looseSpotQOQ = looseSpotQOQ;
    }
    public List<SCLooseSpotRate> getLooseSpotNew() {
        return looseSpotNew;
    }
    public void setLooseSpotNew(List<SCLooseSpotRate> looseSpotNew) {
        this.looseSpotNew = looseSpotNew;
    }
    public List<SCDemoDetail> getDemoDetail() {
        return demoDetail;
    }
    public void setDemoDetail(List<SCDemoDetail> demoDetail) {
        this.demoDetail = demoDetail;
    }
    public SCPackageDetail getPackageDetail() {
        return packageDetail;
    }
    public void setPackageDetail(SCPackageDetail packageDetail) {
        this.packageDetail = packageDetail;
    }
    public List<SCEstRating> getEstRating() {
        return estRating;
    }
    public void setEstRating(List<SCEstRating> estRating) {
        this.estRating = estRating;
    }
    public List<SCEstAudience> getEstAudience() {
        return estAudience;
    }
    public void setEstAudience(List<SCEstAudience> estAudience) {
        this.estAudience = estAudience;
    }
    public List<SCEstRates> getEstCPP() {
        return estCPP;
    }
    public void setEstCPP(List<SCEstRates> estCPP) {
        this.estCPP = estCPP;
    }
    public List<SCEstRates> getEstCPT() {
        return estCPT;
    }
    public void setEstCPT(List<SCEstRates> estCPT) {
        this.estCPT = estCPT;
    }
    public SpotChannels() {
        super();
    }
    @Override
    public String toString() {
        return "SpotChannels [channelId=" + channelId + ", affinity=" + affinity + ", bouquet=" + bouquet + ", name="
                + name + ", looseSpotYOY=" + looseSpotYOY + ", looseSpotQOQ=" + looseSpotQOQ + ", looseSpotNew="
                + looseSpotNew + ", demoDetail=" + demoDetail + ", packageDetail=" + packageDetail + ", estRating="
                + estRating + ", estAudience=" + estAudience + ", estCPP=" + estCPP + ", estCPT=" + estCPT + "]";
    }
    
    
}
