package com.dms.ptp.service;
import java.io.IOException;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.dms.ptp.dto.CampaignList;
import com.dms.ptp.entity.Campaign;
import com.dms.ptp.exception.InvalidParamException;
import com.dms.ptp.dto.*;
import com.dms.ptp.response.*;

import freemarker.template.TemplateException;

public interface CampaignService {

    Integer seedGenerator();
    CampaignResponseList createCampaign(CampaignRequest campaignRequest, String action, String token) throws MessagingException, IOException, TemplateException;
    CampaignList getCampaignList(String sortDir,Pageable pageable, String token);
    String getProductsForAdvertisers(String agencyCode, String advertiserCode);
    Map<String, Object> deleteCampaign(int portalId);
    UpdateCampaignResponse updateCampaign(UpdateCampaignRequest updateCampaignRequest);
    UpdateCampaignResponse resubmitCampaign(UpdateCampaignRequest updateCampaignRequest, String token);
    Campaign getCampaignById(int portalId);
    AvailLMKResponse getAvailableCherrypicks(AvailLMKRequest availLMKRequest);
    CampaignApproveRejectResp campaignReject(CampaignApproveReject campaignReject);
    CampaignApproveRejectResp campaignApproval(CampaignApproveReject campaignApproval);
	CampaignList getCampaignInfo(int status, String sortdir, Pageable pageable, String token);
	TargetMarketLMKResp getTargetMarketDetailsPackage(TargetMarketPkgReq multipleTargetMarketReq)
			throws InvalidParamException;
	ResponseEntity<byte[]> getOrderConfirmation(Integer campaignCode, String type);
}
