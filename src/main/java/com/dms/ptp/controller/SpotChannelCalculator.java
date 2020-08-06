package com.dms.ptp.controller;

import com.dms.ptp.response.SCResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dms.ptp.dto.SpotChannelRequest;
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
    public ResponseEntity<SCResponse> getSpotChannel(@RequestBody SpotChannelRequest spotChannelRequest) {
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

}
