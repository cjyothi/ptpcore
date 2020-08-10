package com.dms.ptp.controller;

import com.dms.ptp.response.SCResponse;
import com.dms.ptp.response.SaveResponse;
import com.dms.ptp.response.SpotChannelData;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dms.ptp.dto.AffCreateRequest;
import com.dms.ptp.dto.AffUpdateRequest;
import com.dms.ptp.dto.SCCreateRequest;
import com.dms.ptp.dto.SCUpdateRequest;
import com.dms.ptp.dto.SpotChannelRequest;
import com.dms.ptp.exception.InvalidParamException;
import com.dms.ptp.response.SpotChannelResponse;
import com.dms.ptp.service.SpotChannelService;

@RestController
@RequestMapping("/pricing/")
@CrossOrigin()
public class SpotChannelCalculator {

    @Autowired
    SpotChannelService spotChannelService;

    static Logger logger = LoggerFactory.getLogger(SpotChannelCalculator.class);

    @PostMapping("sc/calc")
    public ResponseEntity<SCResponse> getSpotChannelCalculation(@RequestBody SpotChannelRequest spotChannelRequest) {
        SCResponse scResponse = new SCResponse();
        SpotChannelResponse spotChannelResponse;
        try {
            spotChannelResponse = spotChannelService.getSpotChannel(spotChannelRequest);
            scResponse.setSpotChannelResponse(spotChannelResponse);
            scResponse.setMessage("Spot Channel calculation bare data retrieved");
        } catch (Exception e) {
            logger.error("Error calling SpotChannel Calculator:{}",e.getMessage());
            scResponse.setMessage("Error calling SpotChannel Calculator : "+ e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(scResponse);
        }
        return ResponseEntity.ok(scResponse);
    }

    @PostMapping("sc")
    public ResponseEntity saveSpotChanne(@RequestBody SCCreateRequest sCCreateRequest) {
        SaveResponse spotChannelResponse;
        try {
            spotChannelResponse =  spotChannelService.saveSpotChannel(sCCreateRequest);
        } catch (Exception e) {
            logger.error("Error while saving Spot Channel :{}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(spotChannelResponse);
    }

    @PutMapping("sc")
    public ResponseEntity updateSpotChannel(@RequestBody SCUpdateRequest scUpdateRequest) {
        SaveResponse spotChannelResponse;
        try {
            spotChannelResponse =  spotChannelService.updateSpotChannel(scUpdateRequest);
        } catch (Exception e) {
            logger.error("Error while updating Spot channel :{}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(spotChannelResponse);
    }

    @DeleteMapping("sc")
    public ResponseEntity<SaveResponse> deleteSpotChannel(@RequestParam String component, @RequestParam int rateCardId)
            throws InvalidParamException {
        if (!(component.equalsIgnoreCase("Sport") || component.equalsIgnoreCase("Other"))) {
            throw new InvalidParamException("Invalid component");
        }
        return ResponseEntity.ok(spotChannelService.deleteSpotChannelinRateCard(component, rateCardId));
    }

    @GetMapping("sc")
    public ResponseEntity<SpotChannelData> getSpotChannel(@RequestParam String type, @RequestParam int week,
                                                          @RequestParam String component, @RequestParam int rateCardId) throws InvalidParamException {
        if (!(component.equalsIgnoreCase("Sport") || component.equalsIgnoreCase("Other"))) {
            throw new InvalidParamException("Invalid component");
        } else {
            if (!(type.equalsIgnoreCase("alcohol") || type.equalsIgnoreCase("standard"))) {
                throw new InvalidParamException("Invalid type");
            }
            List<Integer> weekList = Arrays.asList(1, 2, 4);
            if (!weekList.contains(week)) {
                throw new InvalidParamException("Invalid value for week");
            }
        }
        return ResponseEntity.ok(spotChannelService.getSpotChannelRateCard(type, week, component, rateCardId));
    }

}
