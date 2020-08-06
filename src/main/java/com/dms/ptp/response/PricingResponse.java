package com.dms.ptp.response;

import java.util.List;

public class PricingResponse {

	private int demo;
	private int baseRate;
	private String delivery;
	private double soHigh;
	private double soLow;
	private double rateIncr;
	private double rateDecr;
	private String qoqStart;
	private String qoqEnd;
	private String yoyStart;
	private String yoyEnd;
	private List<DeliveryCurrencyInput> deliveryInputs;
	private List<PricingChannel> channels;

	public int getDemo() {
		return demo;
	}

	public void setDemo(int demo) {
		this.demo = demo;
	}

	public int getBaseRate() {
		return baseRate;
	}

	public void setBaseRate(int baseRate) {
		this.baseRate = baseRate;
	}

	public String getDelivery() {
		return delivery;
	}

	public void setDelivery(String delivery) {
		this.delivery = delivery;
	}

	public double getSoHigh() {
		return soHigh;
	}

	public void setSoHigh(double soHigh) {
		this.soHigh = soHigh;
	}

	public double getSoLow() {
		return soLow;
	}

	public void setSoLow(double soLow) {
		this.soLow = soLow;
	}

	public double getRateIncr() {
		return rateIncr;
	}

	public void setRateIncr(double rateIncr) {
		this.rateIncr = rateIncr;
	}

	public double getRateDecr() {
		return rateDecr;
	}

	public void setRateDecr(double rateDecr) {
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

	public List<DeliveryCurrencyInput> getDeliveryInputs() {
		return deliveryInputs;
	}

	public void setDeliveryInputs(List<DeliveryCurrencyInput> deliveryInputs) {
		this.deliveryInputs = deliveryInputs;
	}

	public List<PricingChannel> getChannels() {
		return channels;
	}

	public void setChannels(List<PricingChannel> channels) {
		this.channels = channels;
	}

	@Override
	public String toString() {
		return "PricingResponse [demo=" + demo + ", baseRate=" + baseRate + ", delivery=" + delivery + ", soHigh="
				+ soHigh + ", soLow=" + soLow + ", rateIncr=" + rateIncr + ", rateDecr=" + rateDecr + ", qoqStart="
				+ qoqStart + ", qoqEnd=" + qoqEnd + ", yoyStart=" + yoyStart + ", yoyEnd=" + yoyEnd
				+ ", deliveryInputs=" + deliveryInputs + ", channels=" + channels + "]";
	}

}
