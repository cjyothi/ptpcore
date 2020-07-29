package com.dms.ptp.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PriceDemo {
    @Id
    private int id;
    private String name;
    private Double universe_avg1;
    private Double universe_avg2;
    private Double universe_avg3;
    private Double universe_avg4;
    private Double universe_avg5;
    private Double universe_avg6;
    private Double universe_avg7;

    /*
     * private String tier; private String network; private String distribution;
     * private String genre;
     * 
     */ public int getId() {
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

    public Double getUniverse_avg1() {
        return universe_avg1;
    }

    public void setUniverse_avg1(Double universe_avg1) {
        this.universe_avg1 = universe_avg1 == null ? 0.0 : universe_avg1;
    }

    public Double getUniverse_avg2() {
        return universe_avg2;
    }

    public void setUniverse_avg2(Double universe_avg2) {
        this.universe_avg2 = universe_avg2 == null ? 0.0 : universe_avg2;
    }

    public Double getUniverse_avg3() {
        return universe_avg3;
    }

    public void setUniverse_avg3(Double universe_avg3) {
        this.universe_avg3 = universe_avg3 == null ? 0.0 : universe_avg3;
    }

    public Double getUniverse_avg4() {
        return universe_avg4;
    }

    public void setUniverse_avg4(Double universe_avg4) {
        this.universe_avg4 = universe_avg4 == null ? 0.0 : universe_avg4;
    }

    public Double getUniverse_avg5() {
        return universe_avg5;
    }

    public void setUniverse_avg5(Double universe_avg5) {
        this.universe_avg5 = universe_avg5 == null ? 0.0 : universe_avg5;
    }

    public Double getUniverse_avg6() {
        return universe_avg6;
    }

    public void setUniverse_avg6(Double universe_avg6) {
        this.universe_avg6 = universe_avg6 == null ? 0.0 : universe_avg6;
    }

    public Double getUniverse_avg7() {
        return universe_avg7;
    }

    public void setUniverse_avg7(Double universe_avg7) {
        this.universe_avg7 = universe_avg7 == null ? 0.0 : universe_avg7;
    }

    /*
     * public String getTier() { return tier; }
     * 
     * public void setTier(String tier) { this.tier = tier; }
     * 
     * public String getNetwork() { return network; }
     * 
     * public void setNetwork(String network) { this.network = network == null ?
     * "NO_VALUE" : network; }
     * 
     * public String getSegment() { return segment; }
     * 
     * public void setSegment(String segment) { this.segment = segment == null ?
     * "NO_VALUE" : segment; }
     * 
     * public String getNetgain() { return netgain; }
     * 
     * public void setNetgain(String netgain) { this.netgain = netgain == null ?
     * "NO_VALUE" : netgain; }
     * 
     * public String getGenre() { return genre; }
     * 
     * public void setGenre(String genre) { this.genre = genre == null ? "NO_VALUE"
     * : genre; }
     * 
     * @Override public String toString() { return "ResponseDemo [id=" + id +
     * ", name=" + name + ", universe_avg1=" + universe_avg1 + ", universe_avg2=" +
     * universe_avg2 + ", universe_avg3=" + universe_avg3 + ", universe_avg4=" +
     * universe_avg4 + ", universe_avg5=" + universe_avg5 + ", universe_avg6=" +
     * universe_avg6 + ", universe_avg7=" + universe_avg7 + ", tier=" + tier +
     * ", network=" + network + ", segment=" + segment + ", netgain=" + netgain +
     * ", genre=" + genre + "]"; }
     * 
     */}
