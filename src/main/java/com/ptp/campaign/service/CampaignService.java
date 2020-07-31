package com.ptp.campaign.service;

import java.io.IOException;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.ptp.campaign.exception.InvalidParamException;
import com.ptp.campaign.model.AvailLMKRequest;
import com.ptp.campaign.model.AvailLMKResponse;
import com.ptp.campaign.model.Campaign;
import com.ptp.campaign.model.CampaignApproveReject;
import com.ptp.campaign.model.CampaignApproveRejectResp;
import com.ptp.campaign.model.CampaignList;
import com.ptp.campaign.model.CampaignRequest;
import com.ptp.campaign.model.CampaignResponseList;
import com.ptp.campaign.model.TargetMarketLMKResp;
import com.ptp.campaign.model.TargetMarketPkgReq;
import com.ptp.campaign.model.UpdateCampaignRequest;
import com.ptp.campaign.model.UpdateCampaignResponse;

import freemarker.template.TemplateException;

public interface CampaignService {

	Integer seedGenerator();

	CampaignResponseList createCampaign(CampaignRequest campaignRequest, String action, String token)
			throws MessagingException, IOException, TemplateException;

	CampaignList getCampaignList(String sortDir, Pageable pageable, String token);

	String getProductsForAdvertisers(String agencyCode, String advertiserCode);

	Map<String, Object> deleteCampaign(int portalId);

	UpdateCampaignResponse updateCampaign(UpdateCampaignRequest updateCampaignRequest);

	UpdateCampaignResponse resubmitCampaign(UpdateCampaignRequest updateCampaignRequest, String token);

	Campaign getCampaignById(int portalId);

	AvailLMKResponse getAvailableCherrypicks(AvailLMKRequest availLMKRequest);

	CampaignApproveRejectResp campaignReject(CampaignApproveReject campaignReject);

	CampaignApproveRejectResp campaignApproval(CampaignApproveReject campaignApproval);

	TargetMarketLMKResp getTargetMarketDetailsPackage(TargetMarketPkgReq multipleTargetMarketReq)
			throws InvalidParamException;

	CampaignList getCampaignInfo(int status, String sortdir, Pageable pageable, String token);

	ResponseEntity<byte[]> getOrderConfirmation(Integer campaignCode, String type);
}
