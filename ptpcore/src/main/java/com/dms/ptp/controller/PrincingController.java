package com.dms.ptp.controller;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.dms.ptp.dto.RateCardInput;
import com.dms.ptp.entity.RateCards;
import com.dms.ptp.exception.InvalidParamException;
import com.dms.ptp.response.LengthFactorResponse;
import com.dms.ptp.response.PriceingRatesResponse;
import com.dms.ptp.response.PriceingSelloutResponse;
import com.dms.ptp.response.PricingDataResponse;
import com.dms.ptp.response.UniverseDemoResponse;
import com.dms.ptp.service.PriceDemoService;
import com.dms.ptp.service.PricingService;
import com.dms.ptp.util.PageDecorator;

import io.github.perplexhub.rsql.RSQLJPASupport;

/**
 * This is the controller class which contains all endpoints to get list of
 * offering,territory,rates,universe,sellout,panel etc.
 * 
 * @author chidananda.p
 *
 */
@RestController
@RequestMapping("/pricing")
@CrossOrigin()
public class PrincingController {

    @Autowired
    PricingService pricingService;

    @Autowired
    PriceDemoService priceDemoService;
    UniverseDemoResponse response;

    /**
     * Returns list of offering
     * 
     * @return
     */
    @GetMapping("/offering")
    public ResponseEntity<PricingDataResponse> getAllOfferings() {
        PricingDataResponse offeringList;
        try {
            offeringList = pricingService.getOffering();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(offeringList);
    }

    /**
     * Return list of territory
     * 
     * @return
     */
    @GetMapping("/territory")
    public ResponseEntity<PricingDataResponse> getAllTerritory() {
        PricingDataResponse territoriesList;
        try {
            territoriesList = pricingService.getTerritory();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(territoriesList);
    }

    /**
     * Returns list of platform
     * 
     * @return
     */
    @GetMapping("/platform")
    public ResponseEntity<PricingDataResponse> getAllPlatform() {
        PricingDataResponse platformList;
        try {
            platformList = pricingService.getPlatform();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(platformList);
    }

    /**
     * Returns list of daypart
     * 
     * @return
     */
    @GetMapping("/daypart")
    public ResponseEntity<PricingDataResponse> getAllDayPart() {
        PricingDataResponse dayPartList;
        try {
            dayPartList = pricingService.getDayPart();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(dayPartList);
    }

    /**
     * Returns the list of Panels
     * 
     * @return
     */
    @GetMapping("/panel")
    public ResponseEntity<PricingDataResponse> getAllPanel() {
        PricingDataResponse panelList;
        try {
            panelList = pricingService.getPanel();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(panelList);
    }

    /**
     * Returns list of universe based on request param
     * 
     * @param demo
     * @param panelId
     * @param startDate
     * @param endDate
     * @return
     * @throws InvalidParamException
     * @throws ParseException
     */
    @RequestMapping(value = "/universe", method = RequestMethod.GET)
    public ResponseEntity<UniverseDemoResponse> getDemo(@RequestParam String demo, @RequestParam int panelId,
            @RequestParam String startDate, @RequestParam String endDate) throws InvalidParamException, ParseException {
        validateRequestParam(demo, panelId, startDate, endDate);

        String[] items = demo.split(",");
        try {
            switch (items.length) {
            case 2:
                response = priceDemoService.getDemoInsightsTwoParam(items[0], items[1], startDate, endDate, panelId);
                break;
            case 3:
                response = priceDemoService.getDemoInsightsThreeParam(items[0], items[1], items[2], startDate, endDate,
                        panelId);
                break;
            case 4:
                response = priceDemoService.getDemoInsightsFourParam(items[0], items[1], items[2], items[3], startDate,
                        endDate, panelId);
                break;
            case 5:
                response = priceDemoService.getDemoInsightsFiveParam(items[0], items[1], items[2], items[3], items[4],
                        startDate, endDate, panelId);
                break;
            case 6:
                response = priceDemoService.getDemoInsightsSixParam(items[0], items[1], items[2], items[3], items[4],
                        items[5], startDate, endDate, panelId);
                break;
            case 7:
                response = priceDemoService.getDemoInsightsSevenParam(items[0], items[1], items[2], items[3], items[4],
                        items[5], items[6], startDate, endDate, panelId);
                break;
            }
        } catch (Exception e) {
            return new ResponseEntity<UniverseDemoResponse>(HttpStatus.EXPECTATION_FAILED);
        }

        return new ResponseEntity<UniverseDemoResponse>(response, HttpStatus.OK);
    }

    /**
     * validate the param
     * 
     * @param demo
     * @param panelId
     * @param startDate
     * @param endDate
     * @throws InvalidParamException
     * @throws ParseException
     */
    private void validateRequestParam(String demo, int panelId, String startDate, String endDate)
            throws InvalidParamException, ParseException {
        Date sdate = new SimpleDateFormat("yyyy-mm-dd").parse(startDate);
        Date edate = new SimpleDateFormat("yyyy-mm-dd").parse(endDate);
        if (panelId == 0 || demo.split(",").length == 1 || edate.before(sdate)) {
            throw new InvalidParamException("Please validate the input param");
        }
    }

    /**
     * Returns rates info based on request param
     * 
     * @param startDate
     * @param endDate
     * @return
     * @throws ParseException
     * @throws InvalidParamException
     */
    @RequestMapping(value = "/rates", method = RequestMethod.GET)
    public ResponseEntity<PriceingRatesResponse> getRates(@RequestParam String startDate, @RequestParam String endDate)
            throws ParseException, InvalidParamException {
        Date sdate = new SimpleDateFormat("yyyy-mm-dd").parse(startDate);
        Date edate = new SimpleDateFormat("yyyy-mm-dd").parse(endDate);
        if (edate.before(sdate)) {
            throw new InvalidParamException("Please validate the input param");
        }
        PriceingRatesResponse response = pricingService.getRateList(startDate, endDate);
        return new ResponseEntity<PriceingRatesResponse>(response, HttpStatus.OK);

    }

    /**
     * Returns sellout info based on request param
     * 
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "/sellout", method = RequestMethod.GET)
    public ResponseEntity<PriceingSelloutResponse> getSellout(@RequestParam String startDate,
            @RequestParam String endDate) {
        PriceingSelloutResponse response = pricingService.getSelloutList(startDate, endDate);
        return new ResponseEntity<PriceingSelloutResponse>(response, HttpStatus.OK);

    }
    
    /**
     * Returns PageDecorator
     * 
     * @return
     */
    @GetMapping("/lf/search")
    public PageDecorator<LengthFactorResponse> getLengthFactor(@RequestParam(name="filter", required=true) String filter, 
			Pageable pageable, HttpServletResponse response) {

		Page<LengthFactorResponse> page = pricingService.getLengthFactor(RSQLJPASupport.toSpecification(filter),
               PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()));
		return new PageDecorator<>(page);
	}
    
    
    /**
     * Method to create rate cards.
     * 
     * @return ResponseEntity<Object>
     */
    @PostMapping("/ratecards")
    public ResponseEntity<Object> addRateCard(@RequestBody RateCardInput rateCardInput) {

        int demoId = pricingService.addRateCard(rateCardInput);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/ratecards").buildAndExpand(demoId)
                .toUri();
        return ResponseEntity.created(location).build();
    }
    
    
    /**
     * Method to get all the rate cards.
     * 
     * @return ResponseEntity<List<RateCards>>
     */
    @GetMapping("/ratecards")
    public ResponseEntity<List<RateCards>> findAllRateCards() {

        List<RateCards> rateCardsList = new ArrayList<RateCards>();
        try {
            rateCardsList = pricingService.findAllRateCard();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(rateCardsList);
    }
    
    
    /**
     * Method to get rate cards by their status.
     * 
     * @return ResponseEntity<List<RateCards>>
     */
    @GetMapping("/ratecard")
    public ResponseEntity<List<RateCards>> findRateCardByStatus(@RequestParam String status) {

        List<RateCards> rateCardsList = new ArrayList<RateCards>();
        try {
            rateCardsList = pricingService.findRateCardByStatus(status);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(rateCardsList);

    }

}
