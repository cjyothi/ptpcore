package com.dms.ptp.dto;

import java.util.List;

import com.dms.ptp.response.DeliveryCurrencyInput;
import com.dms.ptp.response.PricingChannel;

public class PricingUpdateRequest {
	private int id;
	private int baselineId;
	private String status;
	private int demo;
	private int baseRate;
	private String delivery;
	private int soHigh;
	private int soLow;
	private int rateIncr;
	private int rateDecr;
	private String qoqStart;
	private String qoqEnd;
	private String yoyStart;
	private String yoyEnd;
	private String weekPart;
	private List<DeliveryCurrencyInput> deliveryInputs;
	private List<PricingChannel> channels;

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

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

	public int getSoHigh() {
		return soHigh;
	}

	public void setSoHigh(int soHigh) {
		this.soHigh = soHigh;
	}

	public int getSoLow() {
		return soLow;
	}

	public void setSoLow(int soLow) {
		this.soLow = soLow;
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

	public String getWeekPart() {
		return weekPart;
	}

	public void setWeekPart(String weekPart) {
		this.weekPart = weekPart;
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
		return "PricingUpdateRequest [id=" + id + ", baselineId=" + baselineId + ", status=" + status + ", demo=" + demo
				+ ", baseRate=" + baseRate + ", delivery=" + delivery + ", soHigh=" + soHigh + ", soLow=" + soLow
				+ ", rateIncr=" + rateIncr + ", rateDecr=" + rateDecr + ", qoqStart=" + qoqStart + ", qoqEnd=" + qoqEnd
				+ ", yoyStart=" + yoyStart + ", yoyEnd=" + yoyEnd + ", weekPart=" + weekPart + ", deliveryInputs="
				+ deliveryInputs + ", channels=" + channels + "]";
	}

}
