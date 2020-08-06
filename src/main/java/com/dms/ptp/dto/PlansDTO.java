package com.dms.ptp.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlansDTO {

	private int weeks;
	private int businessType;
	private String extRefCode;
	private List<CatalogPricingDTO> priceInfo;
}
