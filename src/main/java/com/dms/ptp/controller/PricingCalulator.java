package com.dms.ptp.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dms.ptp.dto.PricingCreateRequest;
import com.dms.ptp.dto.PricingRequest;
import com.dms.ptp.dto.PricingUpdateRequest;
import com.dms.ptp.exception.BaselineNotFoundException;
import com.dms.ptp.response.LooseSpotResponse;
import com.dms.ptp.response.PricingBaseline;
import com.dms.ptp.response.PricingResponse;
import com.dms.ptp.response.Response;
import com.dms.ptp.service.PricingCalculatorService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/pricing/")
@CrossOrigin()
public class PricingCalulator {

	@Autowired
	PricingCalculatorService priceCalService;

	static Logger logger = LoggerFactory.getLogger(PricingCalulator.class);

	@PostMapping("ls/calc")
	public ResponseEntity<Response> getPricingCalculation(@Valid @RequestBody PricingRequest pricingRequest,
			BindingResult bindingResult) {
		Response response = new Response();
		PricingResponse calculationResponse = null;
		if (bindingResult.hasErrors()) {
			logger.info(
					"PricingCalulator : getPricingCalculation (Payload contains either mismatch field or null value)"
							+ bindingResult.getFieldErrors());
			response.setMessage(bindingResult.getFieldError().toString());
			response.setPricingResponse(calculationResponse);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		try {
			calculationResponse = priceCalService.getPricingCalculation(pricingRequest);
			response.setMessage("Calculation completed");
			response.setPricingResponse(calculationResponse);
			logger.info("Response:" + calculationResponse);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return ResponseEntity.ok(response);
	}

	@PostMapping("ls")
	public ResponseEntity<LooseSpotResponse> saveAffinity(@RequestBody PricingCreateRequest pricingRequest) {

		LooseSpotResponse response = new LooseSpotResponse();
		try {
			response = priceCalService.savePricing(pricingRequest);
		} catch (Exception e) {
			response.setMessage("Failed to Persist:-> " + e.getMessage());
			response.setStatus("FAILED");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		if (response == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@PutMapping("ls")
	public ResponseEntity<LooseSpotResponse> updateAffinity(@RequestBody PricingUpdateRequest affinityUpdateRequest) {
		LooseSpotResponse response = new LooseSpotResponse();
		try {
			response = priceCalService.updatePricing(affinityUpdateRequest);
		} catch (Exception e) {
			response.setMessage("Failed to Update in DB:-> " + e.getMessage());
			response.setStatus("FAILED");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		}
		if (response == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}

		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

	@GetMapping("ls/{baselineId}")
	public ResponseEntity<PricingBaseline> getLooseSpot(@PathVariable int baselineId) {
		PricingBaseline pricingBaseline;
		try {
			pricingBaseline = priceCalService.getLooseSpot(baselineId);
		} catch (BaselineNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		return ResponseEntity.ok(pricingBaseline);
	}

}
