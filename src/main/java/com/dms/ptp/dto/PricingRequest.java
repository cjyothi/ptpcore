package com.dms.ptp.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.dms.ptp.response.DeliveryCurrencyInput;

public class PricingRequest {
	@NotNull
	private Integer demo;
	@NotNull
	private Integer baseRate;
	@NotNull
	@NotEmpty
	private String delivery;
	@NotNull
	private Double soHigh;
	@NotNull
	private Double soLow;
	@NotNull
	private Double rateIncr;
	@NotNull
	private Double rateDecr;
	@NotNull
	@NotEmpty
	private String qoqStart;
	@NotNull
	@NotEmpty
	private String qoqEnd;
	@NotNull
	@NotEmpty
	private String yoyStart;
	@NotNull
	@NotEmpty
	private String yoyEnd;
	@NotNull
	@NotEmpty
	private String weekPart;
	@NotNull
	@NotEmpty
	private List<DeliveryCurrencyInput> deliveryInputs;
	@NotNull
	private AffinityResponse arResult;

	public int getDemo() {
		return demo;
	}

	public void setDemo(int demo) {
		this.demo = demo;
	}

	public String getWeekPart() {
		return weekPart;
	}

	public void setWeekPart(String weekPart) {
		this.weekPart = weekPart;
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

	public AffinityResponse getArResult() {
		return arResult;
	}

	public void setArResult(AffinityResponse arResult) {
		this.arResult = arResult;
	}

	@Override
	public String toString() {
		return "PricingRequest [demo=" + demo + ", baseRate=" + baseRate + ", delivery=" + delivery + ", soHigh="
				+ soHigh + ", soLow=" + soLow + ", rateIncr=" + rateIncr + ", rateDecr=" + rateDecr + ", qoqStart="
				+ qoqStart + ", qoqEndD=" + qoqEnd + ", yoyStart=" + yoyStart + ", yoyEnd=" + yoyEnd
				+ ", deliveryInputs=" + deliveryInputs + ", arResult=" + arResult + "]";
	}

}
