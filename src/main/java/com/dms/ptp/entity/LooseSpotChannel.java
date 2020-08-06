package com.dms.ptp.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "lspot_channel")
public class LooseSpotChannel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "channel_id")
	private int channelId;

	@Column(name = "sec_demo")
	private int secDemo;

	@Column(name = "s1")
	private double rateS1;

	@Column(name = "s2")
	private double rateS2;

	@Column(name = "s3")
	private double rateS3;

	@Column(name = "rate_infl_yoy")
	private double rateInflationYOY;

	@Column(name = "rate_infl_qoq")
	private double rateInflationQOQ;

	@OneToMany(targetEntity = LooseSpotRate.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "lspot_channel_id", referencedColumnName = "id", nullable = false)
	private List<LooseSpotRate> looseSpotRateList;

	@OneToMany(targetEntity = LooseSpotRateYOY.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "lspot_channel_id", referencedColumnName = "id", nullable = false)
	private List<LooseSpotRateYOY> looseSpotRateYOYList;

	@OneToMany(targetEntity = LooseSpotRateQOQ.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "lspot_channel_id", referencedColumnName = "id", nullable = false)
	private List<LooseSpotRateQOQ> looseSpotRateQOQList;

	@OneToMany(targetEntity = LooseSpotAudienceForecast.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "lspot_channel_id", referencedColumnName = "id", nullable = false)
	private List<LooseSpotAudienceForecast> looseSpotAudienceForecastList;

	@OneToMany(targetEntity = LooseSpotSellout.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "lspot_channel_id", referencedColumnName = "id", nullable = false)
	private List<LooseSpotSellout> looseSpotSelloutList;

	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(referencedColumnName = "id")
	private LooseSpotAudienceYOY looseSpotAudienceYOY;

	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(referencedColumnName = "id")
	private LooseSpotAudienceForecastVariance looseSpotAudienceForecastVariance;
	
	@OneToMany(targetEntity = LooseSpotSource.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "lspot_channel_id", referencedColumnName = "id", nullable = false)
	private List<LooseSpotSource> looseSpotSourceList;

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

	public double getRateS1() {
		return rateS1;
	}

	public void setRateS1(double rateS1) {
		this.rateS1 = rateS1;
	}

	public double getRateS2() {
		return rateS2;
	}

	public void setRateS2(double rateS2) {
		this.rateS2 = rateS2;
	}

	public double getRateS3() {
		return rateS3;
	}

	public void setRateS3(double rateS3) {
		this.rateS3 = rateS3;
	}

	public double getRateInflationYOY() {
		return rateInflationYOY;
	}

	public void setRateInflationYOY(double rateInflationYOY) {
		this.rateInflationYOY = rateInflationYOY;
	}

	public double getRateInflationQOQ() {
		return rateInflationQOQ;
	}

	public void setRateInflationQOQ(double rateInflationQOQ) {
		this.rateInflationQOQ = rateInflationQOQ;
	}

	public int getSecDemo() {
		return secDemo;
	}

	public void setSecDemo(int secDemo) {
		this.secDemo = secDemo;
	}

	public List<LooseSpotRate> getLooseSpotRateList() {
		return looseSpotRateList;
	}

	public void setLooseSpotRateList(List<LooseSpotRate> looseSpotRateList) {
		this.looseSpotRateList = looseSpotRateList;
	}

	public List<LooseSpotRateYOY> getLooseSpotRateYOYList() {
		return looseSpotRateYOYList;
	}

	public void setLooseSpotRateYOYList(List<LooseSpotRateYOY> looseSpotRateYOYList) {
		this.looseSpotRateYOYList = looseSpotRateYOYList;
	}

	public List<LooseSpotRateQOQ> getLooseSpotRateQOQList() {
		return looseSpotRateQOQList;
	}

	public void setLooseSpotRateQOQList(List<LooseSpotRateQOQ> looseSpotRateQOQList) {
		this.looseSpotRateQOQList = looseSpotRateQOQList;
	}

	public List<LooseSpotAudienceForecast> getLooseSpotAudienceForecastList() {
		return looseSpotAudienceForecastList;
	}

	public void setLooseSpotAudienceForecastList(List<LooseSpotAudienceForecast> looseSpotAudienceForecastList) {
		this.looseSpotAudienceForecastList = looseSpotAudienceForecastList;
	}

	public List<LooseSpotSellout> getLooseSpotSelloutList() {
		return looseSpotSelloutList;
	}

	public void setLooseSpotSelloutList(List<LooseSpotSellout> looseSpotSelloutList) {
		this.looseSpotSelloutList = looseSpotSelloutList;
	}

	public LooseSpotAudienceYOY getLooseSpotAudienceYOY() {
		return looseSpotAudienceYOY;
	}

	public void setLooseSpotAudienceYOY(LooseSpotAudienceYOY looseSpotAudienceYOY) {
		this.looseSpotAudienceYOY = looseSpotAudienceYOY;
	}

	public LooseSpotAudienceForecastVariance getLooseSpotAudienceForecastVariance() {
		return looseSpotAudienceForecastVariance;
	}

	public void setLooseSpotAudienceForecastVariance(
			LooseSpotAudienceForecastVariance looseSpotAudienceForecastVariance) {
		this.looseSpotAudienceForecastVariance = looseSpotAudienceForecastVariance;
	}
	

	public List<LooseSpotSource> getLooseSpotSourceList() {
		return looseSpotSourceList;
	}

	public void setLooseSpotSourceList(List<LooseSpotSource> looseSpotSourceList) {
		this.looseSpotSourceList = looseSpotSourceList;
	}

	@Override
	public String toString() {
		return "LooseSpotChannel [id=" + id + ", channelId=" + channelId + ", secDemo=" + secDemo + ", rateS1=" + rateS1
				+ ", rateS2=" + rateS2 + ", rateS3=" + rateS3 + ", rateInflationYOY=" + rateInflationYOY
				+ ", rateInflationQOQ=" + rateInflationQOQ + ", looseSpotRateList=" + looseSpotRateList
				+ ", looseSpotRateYOYList=" + looseSpotRateYOYList + ", looseSpotRateQOQList=" + looseSpotRateQOQList
				+ ", looseSpotAudienceForecastList=" + looseSpotAudienceForecastList + ", looseSpotSelloutList="
				+ looseSpotSelloutList + ", looseSpotAudienceYOY=" + looseSpotAudienceYOY
				+ ", looseSpotAudienceForecastVariance=" + looseSpotAudienceForecastVariance + "]";
	}

}
