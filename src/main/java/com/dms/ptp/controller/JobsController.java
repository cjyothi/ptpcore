package com.dms.ptp.controller;

import com.dms.ptp.response.JobsResponse;
import com.dms.ptp.service.JobsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Rest Controller for Jobs
 */
@RestController
@CrossOrigin()
@RequestMapping("/core")
public class JobsController {
    @Autowired
    JobsService jobsService;

    private Logger logger = LoggerFactory.getLogger (JobsController.class);

    /**
     * @param filenames List of file Names
     * @param type String
     * @return ResponseEntity
     */
    @CrossOrigin
    @GetMapping(value="/jobs")
    public ResponseEntity insertFileName(@RequestParam List<String> filenames, @RequestParam String type){
        if(!type.equals("panel")&&!type.equals("insight")){
            return ResponseEntity.badRequest().body("Please send valid input");
        }
        try{ jobsService.saveJobs(filenames,type);
            return ResponseEntity.ok(new JobsResponse(true));
        }catch (Exception e){
            logger.error ("Exception caught:{}",e.getMessage ());
            return ResponseEntity.status(503).body(new JobsResponse(false));
        }

    }


    @CrossOrigin
    @GetMapping(value="/jobs/{type}")
    public ResponseEntity getAllFileNameByType(@PathVariable String type){
        if(!type.equals("panel")&&!type.equals("insight")){
            return ResponseEntity.badRequest().body("Please send valid input");
        }
        try{
            return ResponseEntity.ok(jobsService.getAllJobsByType(type));
        }catch (Exception e){
            logger.error ("Exception caught:{}",e.getMessage ());
            return ResponseEntity.status(503).body("Exception");
        }
    }

    @CrossOrigin
    @GetMapping(value="/jobs/update")
    public ResponseEntity updateFileStatus(@RequestParam int id, @RequestParam int status){
        if(status!=0&&status!=1&&status!=2){
            return ResponseEntity.badRequest().body("Please send valid input");
        }
        try{
            return ResponseEntity.ok(jobsService.updateJobStatus(id,status));
        }catch (Exception e){
            logger.error ("Exception caught:{}",e.getMessage ());
            return ResponseEntity.status(503).body("Exception");
        }
    }




}