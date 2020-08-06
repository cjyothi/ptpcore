package com.dms.ptp.serviceimplementation;

import com.dms.ptp.dto.*;
import com.dms.ptp.entity.*;
import com.dms.ptp.repository.*;

import com.dms.ptp.response.*;
import com.dms.ptp.service.PricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class PricingServiceImplementation implements PricingService {

    @Autowired
    OfferingRepository offeringRepository;

    @Autowired
    TerritoryRepository territoryRepository;

    @Autowired
    PlatformRepository platformRepository;

    @Autowired
    DayPartRepository dayPartRepository;

    @Autowired
    PanelRepository panelRepository;
    @Autowired
    private RateRepository rateRepo;
    @Autowired
    private SelloutRepository sellOutRepo;
    
    @Autowired
    LengthFactorRepository lengthFactorRepository;
    
    @Autowired
    RateCardRepository rateCardRepository;

    @Override
    public PricingDataResponse getOffering() {
        List<Offering> offeringList = offeringRepository.findAll();
        return new PricingDataResponse(offeringList.size(), offeringList);
    }

    @Override
    public PricingDataResponse getTerritory() {
        List<Territory> territoryList = territoryRepository.findAll();
        List<TerritoryResponse> territoryResponseList =  new ArrayList<>();
        territoryList.forEach(territory ->
                territoryResponseList.add(new TerritoryResponse(territory.getId(), territory.getName())));
        return new PricingDataResponse(territoryResponseList.size(), territoryResponseList);
    }

    @Override
    public PricingDataResponse getPlatform() {
        List<Platform> platformList = platformRepository.findAll();
        return new PricingDataResponse(platformList.size(), platformList);
    }

    @Override
    public PricingDataResponse getDayPart() {
        List<DayPart> dayPartList = dayPartRepository.findAll();
        return new PricingDataResponse(dayPartList.size(), dayPartList);
    }

    @Override
    public PricingDataResponse getPanel() {
        List<Panel> panelList = panelRepository.findAll();
        List<PanelResponse> panelResponseList = new ArrayList<>();
        panelList.forEach(panel -> {
            panelResponseList.add(new PanelResponse(panel.getId(), panel.getName()));
        });
        return new PricingDataResponse(panelResponseList.size(), panelResponseList);
    }

    @Override
    public PriceingRatesResponse getRateList(String startDate, String endDate) {
        PriceingRatesResponse response = new PriceingRatesResponse();

        List<Rates> ratesList = new ArrayList<>();
        List<RateInfo> rateInfoList = rateRepo.getRatesInfo(startDate, endDate);
        for (int i = 0; i < rateInfoList.size(); i++) {
            Rates rates = new Rates();
            List<RateDayPart> rateDayPartList = new ArrayList<>();

            rates.setId(rateInfoList.get(i).getId());
            rates.setName(rateInfoList.get(i).getChannel_name());
            RateDayPart r = new RateDayPart();
            r.setDaypart(rateInfoList.get(i).getDay_part());
            r.setRate_actual(rateInfoList.get(i).getRate_actual());
            r.setRate_forecast(rateInfoList.get(i).getRate_forecast());
            rateDayPartList.add(r);
            rates.setRates(rateDayPartList);
            ratesList.add(rates);
        }
        response.setTotal(ratesList.size());
        response.setItems(ratesList);

        return response;
    }

    @Override
    public PriceingSelloutResponse getSelloutList(String startDate, String endDate) {
        PriceingSelloutResponse response = new PriceingSelloutResponse();
        List<SellOut> selloutList = new ArrayList<>();
        List<SellOutInfo> sellOutInfoList = sellOutRepo.getRatesInfo(startDate, endDate);
        sellOutInfoList.forEach(sellOutInfo -> {
            SellOut rates = new SellOut();
            List<SellOutDaypart> rateDayPartList = new ArrayList<>();
            rates.setId(sellOutInfo.getId());
            rates.setName(sellOutInfo.getChannel_name());
            SellOutDaypart s = new SellOutDaypart();
            s.setDaypart(sellOutInfo.getDaypart());
            s.setSo(sellOutInfo.getPercentage());
            rateDayPartList.add(s);
            rates.setRates(rateDayPartList);
            selloutList.add(rates);

        });
        response.setTotal(selloutList.size());
        response.setItems(selloutList);
        return response;
    }
    
    @Override
    public Page<LengthFactorResponse> getLengthFactor(Specification t, Pageable p) {

        Page<LengthFactor> lengthFactor = lengthFactorRepository.findAll(t, p);

        List<LengthFactor> lengthFactorList = lengthFactor.getContent();
        List<LengthFactorResponse> lfResponseList = new ArrayList<LengthFactorResponse>();
        for (int i = 0; i < lengthFactorList.size(); i++) {
            LengthFactorResponse lfResponse = new LengthFactorResponse();
            lfResponse.setPriceFactor(lengthFactorList.get(i).getPriceFactor());
            lfResponseList.add(lfResponse);
        }

        Page<LengthFactorResponse> page = new PageImpl(lfResponseList, p, 1L);
        return page;
    }
    
    /**
     * Method to add rate cards.
     * 
     * @return int
     */
    @Override
    public int addRateCard(RateCardInput rateCardInput) {
        
        RateCards rateCards = new RateCards();
        rateCards.setTitle(rateCardInput.getTitle());
        rateCards.setStartDate(Date.valueOf(rateCardInput.getStart_date()));
        rateCards.setStatus("Submitted");
        RateCards addedRateCards = rateCardRepository.save(rateCards);
        return addedRateCards.getId();
    }
    
    
    /**
     * Method to get all rate cards.
     * 
     * @return List<RateCards>
     */
    @Override
    public List<RateCards> findAllRateCard(){
        
        return rateCardRepository.findAll();
    }
    
    /**
     * Method to get rate cards by their status.
     * 
     * @return List<RateCards>
     */
    @Override
    public List<RateCards> findRateCardByStatus(String status) {
        
        return rateCardRepository.findByStatus(status);
    }

}
