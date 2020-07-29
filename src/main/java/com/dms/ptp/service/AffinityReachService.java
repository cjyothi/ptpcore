package com.dms.ptp.service;

import java.util.List;

import com.dms.ptp.dto.AffCreateRequest;
import com.dms.ptp.dto.AffUpdateRequest;
import com.dms.ptp.dto.AffinityRequest;
import com.dms.ptp.dto.AffinityResponse;
import com.dms.ptp.response.BaselineListResponse;
import com.dms.ptp.response.BaselineResponse;
import com.dms.ptp.response.SaveResponse;

public interface AffinityReachService {

    AffinityResponse getAffinityReachCalculation(AffinityRequest affinityRequest);

    SaveResponse saveAffinity(AffCreateRequest affinityCreateRequest);

    SaveResponse updateAffinity(AffUpdateRequest affinityUpdateRequest);
    
    void deleteAffinity(int baselineId);
    
    List<BaselineListResponse> getBaselineList();
    
    BaselineResponse getBaselineById(int baseLineId);

    SaveResponse deleteBaseline(int baseLineId);
}
