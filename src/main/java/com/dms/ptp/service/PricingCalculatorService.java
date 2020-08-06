package com.dms.ptp.service;

import com.dms.ptp.dto.PricingCreateRequest;
import com.dms.ptp.dto.PricingRequest;
import com.dms.ptp.dto.PricingUpdateRequest;
import com.dms.ptp.response.LooseSpotResponse;
import com.dms.ptp.response.PricingBaseline;
import com.dms.ptp.response.PricingResponse;

public interface PricingCalculatorService {
	public PricingResponse getPricingCalculation(PricingRequest pricingRequest);

	LooseSpotResponse savePricing(PricingCreateRequest request);

	LooseSpotResponse updatePricing(PricingUpdateRequest updateRequest);

	PricingBaseline getLooseSpot(int baseLineId);

}
