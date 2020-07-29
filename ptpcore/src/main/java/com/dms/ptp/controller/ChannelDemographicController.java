package com.dms.ptp.controller;

import com.dms.ptp.entity.Channel;
import com.dms.ptp.response.DemoInput;
import com.dms.ptp.response.FinalChannelResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dms.ptp.response.ChannelDemographicResponse;
import com.dms.ptp.service.ChannelDemographicService;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

/**
 * Rest Controller for Channel and Demographic
 */
@RestController
@RequestMapping("/core")
@CrossOrigin()
public class ChannelDemographicController {
    
    private Logger logger = LoggerFactory.getLogger(ChannelDemographicController.class);

    @Autowired
    ChannelDemographicService channelDemographicService;
    
    /**
     * @param panel String
     * @return ResponseEntity
     */
    @GetMapping(value = "/demos")
    public ResponseEntity<ChannelDemographicResponse> getDemos(@RequestParam(required = false, defaultValue = "all") String panel) {
        ChannelDemographicResponse demographics;
        if(!panel.equals("1") && !panel.equals("2") && !panel.equals("3") && !panel.equals("all"))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        try {
            demographics = channelDemographicService.getDemographics(panel);
        } catch (Exception e) {
            logger.error ("Exception caught:{}",e.getMessage ());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok(demographics);
    }

    /**
     * @param platform int
     * @return ResponseEntity
     */
    @GetMapping("/channels")
    public ResponseEntity getChannelInfo(@RequestParam(required = false, defaultValue = "0") int platform) {
        ChannelDemographicResponse channelList;
        try {
            channelList = channelDemographicService.getChannels(platform);
        } catch (Exception e) {
            logger.error ("Exception caught:{}",e.getMessage ());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.ok(channelList);
    }

    /**
     * @param channel Channel
     * @return ResponseEntity
     */
    @RequestMapping(value = "/channel", method = RequestMethod.POST)
    public ResponseEntity<Object> channelRegistration(@RequestBody Channel channel) {

        int channelId = channelDemographicService.addChannel(channel);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/channel")
                .buildAndExpand(channelId).toUri();
        return ResponseEntity.created(location).build();

    }

    /**
     * @param demoInput DemoInput
     * @return ResponseEntity
     */
    @RequestMapping(value = "/demo", method = RequestMethod.POST)
    public ResponseEntity<Object> demoRegistration(@RequestBody DemoInput demoInput) {

        int demoId = channelDemographicService.addDemo(demoInput);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/demo")
                .buildAndExpand(demoId).toUri();
        return ResponseEntity.created(location).build();

    }

    
    /**
     * @param channel String
     * @param panelId String
     * @param startDate String
     * @param endDate String
     * @return ResponseEntity
     */
    @RequestMapping(value = "/insights/channels", method = RequestMethod.GET)
    public ResponseEntity<FinalChannelResponse> getChannelInfo(@RequestParam String channel,@RequestParam String panelId,
            @RequestParam String startDate, @RequestParam String endDate) {

        return ResponseEntity.ok(channelDemographicService.getChannelInsights(channel, startDate, endDate, Integer.parseInt(panelId)));
    }


}