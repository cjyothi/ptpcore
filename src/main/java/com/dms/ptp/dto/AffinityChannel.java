package com.dms.ptp.dto;

import java.util.List;

public class AffinityChannel {
    private int id;
    private String name;
    private String tier;
    private String network;
    private List<String> genre;
    private List<AffinityDemo> demos;
    private double secDemoSum;
    private ResultDemo resultDemo;

    public AffinityChannel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public List<String> getGenre() {
        return genre;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    public List<AffinityDemo> getDemos() {
        return demos;
    }

    public void setDemos(List<AffinityDemo> demos) {
        this.demos = demos;
    }

    public double getSecDemoSum() {
        return secDemoSum;
    }

    public void setSecDemoSum(double secDemoSum) {
        this.secDemoSum = secDemoSum;
    }

    public ResultDemo getResultDemo() {
        return resultDemo;
    }

    public void setResultDemo(ResultDemo resultDemo) {
        this.resultDemo = resultDemo;
    }
}
