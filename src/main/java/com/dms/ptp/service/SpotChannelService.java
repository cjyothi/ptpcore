package com.dms.ptp.service;

import com.dms.ptp.dto.SCCreateRequest;
import com.dms.ptp.dto.SCUpdateRequest;
import com.dms.ptp.dto.SpotChannelRequest;
import com.dms.ptp.exception.InvalidParamException;
import com.dms.ptp.response.SaveResponse;
import com.dms.ptp.response.SpotChannelData;
import com.dms.ptp.response.SpotChannelResponse;

public interface SpotChannelService {
    
    SpotChannelResponse getSpotChannel(SpotChannelRequest spotChannelRequest) throws InvalidParamException;
    
    SaveResponse saveSpotChannel(SCCreateRequest sCCreateRequest);
    
    SaveResponse updateSpotChannel(SCUpdateRequest scUpdateRequest);
    
    void deleteSCChannels(int scId);
    
    SaveResponse deleteSpotChannelinRateCard(String component,int rateCardId);

    SpotChannelData getSpotChannelRateCard(String type, int week, String component, int rateCardId);

}
