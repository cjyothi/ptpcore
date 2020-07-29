package com.dms.ptp.service;


import com.dms.ptp.entity.Channel;
import com.dms.ptp.response.ChannelDemographicResponse;
import com.dms.ptp.response.DemoInput;
import com.dms.ptp.response.FinalChannelResponse;

/**
 * ChannelDemographicServices
 */
public interface ChannelDemographicService {

    ChannelDemographicResponse getDemographics (String panel);
    ChannelDemographicResponse getChannels(int platform);
    int addChannel(Channel channel);
    int addDemo(DemoInput demoInput);
    FinalChannelResponse getChannelInsights (String channelList, String startDate, String endDate,
            int panelId);



}

