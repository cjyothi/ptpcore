package com.dms.ptp.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.dms.ptp.dto.RateCardInput;
import com.dms.ptp.entity.RateCards;
import com.dms.ptp.response.LengthFactorResponse;
import com.dms.ptp.response.PriceingRatesResponse;
import com.dms.ptp.response.PriceingSelloutResponse;
import com.dms.ptp.response.PricingDataResponse;

public interface PricingService {

    PricingDataResponse getOffering();

    PricingDataResponse getTerritory();

    PricingDataResponse getPlatform();

    PricingDataResponse getDayPart();

    PricingDataResponse getPanel();

    public PriceingRatesResponse getRateList(String startDate, String endDate);

    public PriceingSelloutResponse getSelloutList(String startDate, String endDate);
    
    public Page<LengthFactorResponse> getLengthFactor(Specification t, Pageable p);
    
    public int addRateCard(RateCardInput rateCardInput);

    public List<RateCards> findAllRateCard();

    public List<RateCards> findRateCardByStatus(String status);

}
