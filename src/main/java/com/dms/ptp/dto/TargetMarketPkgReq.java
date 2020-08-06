package com.dms.ptp.dto;

import java.util.List;

public class TargetMarketPkgReq {

	private int[] targetMarkets;
	private List<TargetMarketPackages> packages;
	
	
	public int[] getTargetMarkets() {
		return targetMarkets;
	}
	public void setTargetMarkets(int[] targetMarkets) {
		this.targetMarkets = targetMarkets;
	}
	public List<TargetMarketPackages> getPackages() {
		return packages;
	}
	public void setPackages(List<TargetMarketPackages> packages) {
		this.packages = packages;
	}

}
