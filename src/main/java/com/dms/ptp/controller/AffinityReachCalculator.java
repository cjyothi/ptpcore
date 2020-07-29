package com.dms.ptp.controller;

import com.dms.ptp.dto.AffCreateRequest;
import com.dms.ptp.dto.AffUpdateRequest;
import com.dms.ptp.dto.AffinityRequest;
import com.dms.ptp.dto.AffinityResponse;
import com.dms.ptp.response.BaselineListResponse;
import com.dms.ptp.response.BaselineResponse;
import com.dms.ptp.response.SaveResponse;
import com.dms.ptp.service.AffinityReachService;

import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/pricing/")
@CrossOrigin()
public class AffinityReachCalculator {

    @Autowired
    AffinityReachService affinityReachService;
    
    static Logger logger = LoggerFactory.getLogger(AffinityReachCalculator.class);

    @PostMapping("ar/calc")
    public ResponseEntity<AffinityResponse> getAffinity(@RequestBody AffinityRequest affinityRequest) {
        AffinityResponse affinityReachCalculators;
        try {
            affinityReachCalculators = affinityReachService.getAffinityReachCalculation(affinityRequest);
        } catch (Exception e) {
            logger.error("Error calling AffinityReach Calculator:{}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(affinityReachCalculators);
    }
    
    @PostMapping("ar")
    public ResponseEntity saveAffinity(@RequestBody AffCreateRequest affinityCreateRequest) {
        List<String> weekPartList =Arrays.asList("WEEKDAY","WEEKEND","ALLDAY");
        if(!weekPartList.contains(affinityCreateRequest.getWeekPart()) && affinityCreateRequest.getWeekPart()!=null){
            logger.error("Invalid weekpart");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid weekpart");
        }
        SaveResponse baselineResponse;
        try {
            baselineResponse =  affinityReachService.saveAffinity(affinityCreateRequest);
        } catch (Exception e) {
            logger.error("Error calling AffinityReach Calculator:{}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(baselineResponse);
    }
    
    @PutMapping("ar")
    public ResponseEntity updateAffinity(@RequestBody AffUpdateRequest affinityUpdateRequest) {
        List<String> weekPartList =Arrays.asList("WEEKDAY","WEEKEND","ALLDAY");
        if(!weekPartList.contains(affinityUpdateRequest.getWeekPart()) && affinityUpdateRequest.getWeekPart()!=null){
            logger.error("Invalid weekpart");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid weekpart");
        }
        SaveResponse baselineId;
        try {
            baselineId =  affinityReachService.updateAffinity(affinityUpdateRequest);
        } catch (Exception e) {
            logger.error("Error calling AffinityReach Calculator:{}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(baselineId);
    }
    
    @GetMapping("ar/{baseLineId}")
    public BaselineResponse getBaseline(@PathVariable int baseLineId ){
        return affinityReachService.getBaselineById(baseLineId);
    }
    @DeleteMapping("ar/{id}")
    public SaveResponse delete(@PathVariable int id ){
        return affinityReachService.deleteBaseline(id);
    }


    @GetMapping("ar")
    public ResponseEntity<List<BaselineListResponse>> getBaselineList(){
        List<BaselineListResponse> baselineListResponseList;
        try{
            baselineListResponseList= affinityReachService.getBaselineList();
        }catch (Exception e){
            logger.error("Error in retrieving Baseline list: {}",e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(baselineListResponseList);
    }
}
