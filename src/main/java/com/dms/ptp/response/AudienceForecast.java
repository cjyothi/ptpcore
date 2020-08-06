package com.dms.ptp.response;

import java.util.List;

public class AudienceForecast {

	private List<AudienceDaypart> audienceDaypart;
	private VarianceYOY varianceYOY;

	
	public AudienceForecast() {
		super();
	}

	public AudienceForecast(List<AudienceDaypart> audienceDaypart, VarianceYOY varianceYOY) {
		super();
		this.audienceDaypart = audienceDaypart;
		this.varianceYOY = varianceYOY;
	}

	public List<AudienceDaypart> getAudienceDaypart() {
		return audienceDaypart;
	}

	public void setAudienceDaypart(List<AudienceDaypart> audienceDaypart) {
		this.audienceDaypart = audienceDaypart;
	}

	public VarianceYOY getVarianceYOY() {
		return varianceYOY;
	}

	public void setVarianceYOY(VarianceYOY varianceYOY) {
		this.varianceYOY = varianceYOY;
	}

}
