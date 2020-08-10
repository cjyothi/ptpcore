package com.dms.ptp.response;

import java.util.List;

public class SpotChannelData {

    private int id;
    private int rateCardId;
    private String component;
    private int baseline1;
    private int baseline2;
    private String type;
    private int qoqRateCard;
    private int yoyRateCard;
    private int qoqBaseline1;
    private int yoyBaseline1;
    private int qoqBaseline2;
    private int yoyBaseline2;
    private int week;
    private int spotLength;
    private List<SpotChannels> channels;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public int getRateCardId() {
        return rateCardId;
    }

    public void setRateCardId(int rateCardId) {
        this.rateCardId=rateCardId;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component=component;
    }

    public int getBaseline1() {
        return baseline1;
    }

    public void setBaseline1(int baseline1) {
        this.baseline1=baseline1;
    }

    public int getBaseline2() {
        return baseline2;
    }

    public void setBaseline2(int baseline2) {
        this.baseline2=baseline2;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type=type;
    }

    public int getQoqRateCard() {
        return qoqRateCard;
    }

    public void setQoqRateCard(int qoqRateCard) {
        this.qoqRateCard=qoqRateCard;
    }

    public int getYoyRateCard() {
        return yoyRateCard;
    }

    public void setYoyRateCard(int yoyRateCard) {
        this.yoyRateCard=yoyRateCard;
    }

    public int getQoqBaseline1() {
        return qoqBaseline1;
    }

    public void setQoqBaseline1(int qoqBaseline1) {
        this.qoqBaseline1=qoqBaseline1;
    }

    public int getYoyBaseline1() {
        return yoyBaseline1;
    }

    public void setYoyBaseline1(int yoyBaseline1) {
        this.yoyBaseline1=yoyBaseline1;
    }

    public int getQoqBaseline2() {
        return qoqBaseline2;
    }

    public void setQoqBaseline2(int qoqBaseline2) {
        this.qoqBaseline2=qoqBaseline2;
    }

    public int getYoyBaseline2() {
        return yoyBaseline2;
    }

    public void setYoyBaseline2(int yoyBaseline2) {
        this.yoyBaseline2=yoyBaseline2;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week=week;
    }

    public int getSpotLength() {
        return spotLength;
    }

    public void setSpotLength(int spotLength) {
        this.spotLength=spotLength;
    }

    public List<SpotChannels> getChannels() {
        return channels;
    }

    public void setChannels(List<SpotChannels> channels) {
        this.channels=channels;
    }
}
