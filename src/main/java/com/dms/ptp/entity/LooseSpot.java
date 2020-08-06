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
@Table(name = "lspot")
public class LooseSpot {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "baseline_id")
	private int baselineId;

	@Column(name = "delivery")
	private String delivery;

	@Column(name = "pri_demo")
	private int priDemo;

	@Column(name = "so_low")
	private int soLow;

	@Column(name = "so_high")
	private int soHigh;

	@Column(name = "base_rate")
	private int baseRate;

	@Column(name = "rate_incr")
	private int rateIncr;

	@Column(name = "rate_decr")
	private int rateDecr;

	@Column(name = "qoq_start")
	private String qoqStart;

	@Column(name = "qoq_end")
	private String qoqEnd;

	@Column(name = "yoy_start")
	private String yoyStart;

	@Column(name = "yoy_end")
	private String yoyEnd;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "lspot_id", referencedColumnName = "id", nullable = false)
	private List<LooseSpotDeliveryInputs> looseSpotDeliveryInputsList;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "lspot_id", referencedColumnName = "id", nullable = false)
	private List<LooseSpotChannel> looseSpotChannelList;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBaselineId() {
		return baselineId;
	}

	public void setBaselineId(int baselineId) {
		this.baselineId = baselineId;
	}

	public String getDelivery() {
		return delivery;
	}

	public void setDelivery(String delivery) {
		this.delivery = delivery;
	}

	public int getPriDemo() {
		return priDemo;
	}

	public void setPriDemo(int priDemo) {
		this.priDemo = priDemo;
	}

	public int getSoLow() {
		return soLow;
	}

	public void setSoLow(int soLow) {
		this.soLow = soLow;
	}

	public int getSoHigh() {
		return soHigh;
	}

	public void setSoHigh(int soHigh) {
		this.soHigh = soHigh;
	}

	public int getBaseRate() {
		return baseRate;
	}

	public void setBaseRate(int baseRate) {
		this.baseRate = baseRate;
	}

	public int getRateIncr() {
		return rateIncr;
	}

	public void setRateIncr(int rateIncr) {
		this.rateIncr = rateIncr;
	}

	public int getRateDecr() {
		return rateDecr;
	}

	public void setRateDecr(int rateDecr) {
		this.rateDecr = rateDecr;
	}

	public String getQoqStart() {
		return qoqStart;
	}

	public void setQoqStart(String qoqStart) {
		this.qoqStart = qoqStart;
	}

	public String getQoqEnd() {
		return qoqEnd;
	}

	public void setQoqEnd(String qoqEnd) {
		this.qoqEnd = qoqEnd;
	}

	public String getYoyStart() {
		return yoyStart;
	}

	public void setYoyStart(String yoyStart) {
		this.yoyStart = yoyStart;
	}

	public String getYoyEnd() {
		return yoyEnd;
	}

	public void setYoyEnd(String yoyEnd) {
		this.yoyEnd = yoyEnd;
	}

	public List<LooseSpotDeliveryInputs> getLooseSpotDeliveryInputsList() {
		return looseSpotDeliveryInputsList;
	}

	public void setLooseSpotDeliveryInputsList(List<LooseSpotDeliveryInputs> looseSpotDeliveryInputsList) {
		this.looseSpotDeliveryInputsList = looseSpotDeliveryInputsList;
	}

	public List<LooseSpotChannel> getLooseSpotChannelList() {
		return looseSpotChannelList;
	}

	public void setLooseSpotChannelList(List<LooseSpotChannel> looseSpotChannelList) {
		this.looseSpotChannelList = looseSpotChannelList;
	}

	@Override
	public String toString() {
		return "LooseSpot [id=" + id + ", baselineId=" + baselineId + ", delivery=" + delivery + ", priDemo=" + priDemo
				+ ", soLow=" + soLow + ", soHigh=" + soHigh + ", baseRate=" + baseRate + ", rateIncr=" + rateIncr
				+ ", rateDecr=" + rateDecr + ", qoqStart=" + qoqStart + ", qoqEnd=" + qoqEnd + ", yoyStart=" + yoyStart
				+ ", yoyEnd=" + yoyEnd + ", looseSpotDeliveryInputsList=" + looseSpotDeliveryInputsList
				+ ", looseSpotChannelList=" + looseSpotChannelList + "]";
	}
	
	
}
