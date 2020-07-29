package com.dms.ptp.dto;

import java.util.List;

public class InterimChannel {
    private int id;
    private String name;
    private String tier;
    private String network;
    private List<String> genre;
    private int lmkRefNo;

    public InterimChannel() {
    }

    public InterimChannel(int id, String name, String tier, String network, List<String> genre,int lmkRefNo) {
        this.id = id;
        this.name = name;
        this.tier = tier;
        this.network = network;
        this.genre = genre;
        this.lmkRefNo=lmkRefNo;
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

    public int getLmkRefNo() {
        return lmkRefNo;
    }

    public void setLmkRefNo(int lmkRefNo) {
        this.lmkRefNo = lmkRefNo;
    }
}
