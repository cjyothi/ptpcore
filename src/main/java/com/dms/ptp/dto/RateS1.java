package com.dms.ptp.dto;

public class RateS1 {

	private int channelId;
	private int lmkRefNo;
	private double result;

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public int getLmkRefNo() {
		return lmkRefNo;
	}

	public void setLmkRefNo(int lmkRefNo) {
		this.lmkRefNo = lmkRefNo;
	}

	
		public double getResult() {
		return result;
	}

	public void setResult(double result) {
		this.result = result;
	}

		@Override
	public String toString() {
		return "RateS1 [channelId=" + channelId + ", lmkRefNo=" + lmkRefNo + ", ruseltOfS*=" + result + "]";
	}

}
