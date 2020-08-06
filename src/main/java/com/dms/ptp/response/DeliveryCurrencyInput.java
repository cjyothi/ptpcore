package com.dms.ptp.response;

public class DeliveryCurrencyInput {
	private String tier;
	private int demoRate;
	private int tmsRate;

	public String getTier() {
		return tier;
	}

	public void setTier(String tier) {
		this.tier = tier;
	}

	public int getDemoRate() {
		return demoRate;
	}

	public void setDemoRate(int demoRate) {
		this.demoRate = demoRate;
	}

	public int getTmsRate() {
		return tmsRate;
	}

	public void setTmsRate(int tmsRate) {
		this.tmsRate = tmsRate;
	}

	public DeliveryCurrencyInput(String tier, int demoRate, int tmsRate) {
		super();
		this.tier = tier;
		this.demoRate = demoRate;
		this.tmsRate = tmsRate;
	}

	public DeliveryCurrencyInput() {
		super();
	}

	@Override
	public String toString() {
		return "DeliveryCurrencyInput [tier=" + tier + ", demoRate=" + demoRate + ", tmsRate=" + tmsRate + "]";
	}

}
