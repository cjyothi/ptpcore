package com.dms.ptp.response;

import java.util.List;

public class PricingChannel {
	private int id;
	private String name;
	private String tier;
	private String network;
	private List<String> genre;
	private List<RateYOY> ratesYOY;
	private List<RateQOQ> ratesQOQ;
	private Integer secDemoId;
	private double s1;
	private double s2;
	private double s3;
	private List<CalculatedRate> calculatedRate;
	private double rateInflationYOY;
	private double rateInflationQOQ;
	private AudienceYOY audienceYOY;
	private AudienceForecast audienceForecast;
	private List<SourceData> sourceData;
	private List<SellOutData> selloutData;
    
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

	public List<RateYOY> getRatesYOY() {
		return ratesYOY;
	}

	public void setRatesYOY(List<RateYOY> ratesYOY) {
		this.ratesYOY = ratesYOY;
	}

	public List<RateQOQ> getRatesQOQ() {
		return ratesQOQ;
	}

	public void setRatesQOQ(List<RateQOQ> ratesQOQ) {
		this.ratesQOQ = ratesQOQ;
	}

	public double getS1() {
		return s1;
	}

	public void setS1(double s1) {
		this.s1 = s1;
	}

	public double getS2() {
		return s2;
	}

	public void setS2(double s2) {
		this.s2 = s2;
	}

	public double getS3() {
		return s3;
	}

	public void setS3(double s3) {
		this.s3 = s3;
	}

	public List<CalculatedRate> getCalculatedRate() {
		return calculatedRate;
	}

	public void setCalculatedRate(List<CalculatedRate> calculatedRate) {
		this.calculatedRate = calculatedRate;
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

	public AudienceYOY getAudienceYOY() {
		return audienceYOY;
	}

	public void setAudienceYOY(AudienceYOY audienceYOY) {
		this.audienceYOY = audienceYOY;
	}

	public AudienceForecast getAudienceForecast() {
		return audienceForecast;
	}

	public void setAudienceForecast(AudienceForecast audienceForecast) {
		this.audienceForecast=audienceForecast;
	}

	public List<SourceData> getSourceData() {
		return sourceData;
	}

	public void setSourceData(List<SourceData> sourceData) {
		this.sourceData=sourceData;
	}

	public List<SellOutData> getSelloutData() {
		return selloutData;
	}

	public void setSelloutData(List<SellOutData> selloutData) {
		this.selloutData=selloutData;
	}

	public Integer getSecDemoId() {
		return secDemoId;
	}

	public void setSecDemoId(Integer secDemoId) {
		this.secDemoId = secDemoId;
	}

	@Override
	public String toString() {
		return "PricingChannel{" +
				"id=" + id +
				", name='" + name + '\'' +
				", tier='" + tier + '\'' +
				", network='" + network + '\'' +
				", genre=" + genre +
				", ratesYOY=" + ratesYOY +
				", ratesQOQ=" + ratesQOQ +
				", secDemoId=" + secDemoId +
				", s1=" + s1 +
				", s2=" + s2 +
				", s3=" + s3 +
				", calculatedRate=" + calculatedRate +
				", rateInflationYOY=" + rateInflationYOY +
				", rateInflationQOQ=" + rateInflationQOQ +
				", audienceYOY=" + audienceYOY +
				", audienceForecast=" + audienceForecast +
				", sourceDataList=" + sourceData +
				", selloutDataList=" + selloutData +
				'}';
	}
}
