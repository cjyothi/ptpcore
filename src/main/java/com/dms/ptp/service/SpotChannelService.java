package com.dms.ptp.service;

import com.dms.ptp.dto.SpotChannelRequest;
import com.dms.ptp.exception.InvalidParamException;
import com.dms.ptp.response.SpotChannelResponse;

public interface SpotChannelService {
    
    SpotChannelResponse getSpotChannel(SpotChannelRequest spotChannelRequest) throws InvalidParamException;

}
