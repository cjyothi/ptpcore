package com.ptp.campaign.controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
import com.ptp.campaign.service.CampaignService;

import io.swagger.annotations.ApiOperation;

@RestController
@CrossOrigin
@RequestMapping("/campaign")
public class CampaignController {

	private static final Logger log = LoggerFactory.getLogger(CampaignController.class);

	@Autowired
	CampaignService campaignService;

	@CrossOrigin
	@ApiOperation(value = "REST API for seed")
	@RequestMapping(value = "/seed", method = RequestMethod.GET)
	public ResponseEntity<Integer> seedGenerator() {
		log.info("inside CampaignController: seed");
		try {
			return ResponseEntity.status(200).body(campaignService.seedGenerator());
		} catch (Exception ex) {
			log.error("Error creating seed", ex.getMessage());
		}
		return ResponseEntity.notFound().build();
	}

	@CrossOrigin
	@ApiOperation(value = "REST API to get product list for selected Advertiser and Agency from Landmark system")
	@RequestMapping(value = "{agencyCode}/{advertiserCode}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
	public ResponseEntity<String> getProductsForAdvertisers(@PathVariable String agencyCode,
			@PathVariable String advertiserCode) {
		log.info("Inside CampaignController: getProductsForAdvertisers ");
		String response = null;
		try {
			response = campaignService.getProductsForAdvertisers(agencyCode, advertiserCode);
		} catch (Exception ex) {
			log.error("Error creating campaign: " + ex.getMessage());
		}
		if (response == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			return ResponseEntity.ok(response);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "REST API to create, save or submit Campaign")
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<CampaignResponseList> createCampaign(@RequestBody CampaignRequest campaignRequest,
			@RequestParam("action") String action, @RequestHeader(name = "Authorization") String token) {
		log.info("inside CampaignController: createCampaign");
		CampaignResponseList response = null;
		try {
			response = campaignService.createCampaign(campaignRequest, action, token);
		} catch (Exception ex) {
			log.error("Error creating campaign: " + ex.getMessage());
		}
		if (response == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			return ResponseEntity.status(response.getStatus()).body(response);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "REST API for fetching Campaign list")
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<CampaignList> getCampaignList(
			@RequestParam(required = false, defaultValue = "desc") String sortdir,
			@RequestHeader(name = "Authorization") String token, @PageableDefault() Pageable pageable) {

		log.info("inside CampaignController: getCampaignList");
		CampaignList responseList = null;
		if (sortdir.equals("asc") || sortdir.equals("desc")) {
			try {
				responseList = campaignService.getCampaignList(sortdir, pageable, token);
				return ResponseEntity.status(200).body(responseList);
			} catch (Exception ex) {
				log.error("Error fetching campaign list: " + ex.getMessage());
			}
			return ResponseEntity.status(400).body(responseList);
		}
		return ResponseEntity.status(400).body(responseList);
	}

	@CrossOrigin
	@ApiOperation(value = "REST API to fetch fetching Campaign list by status")
	@RequestMapping(value = "/status/{status}", method = RequestMethod.GET)
	public ResponseEntity<CampaignList> getCampaignInfo(@PathVariable int status,
			@RequestParam(required = false, defaultValue = "desc") String sortdir,
			@RequestHeader(name = "Authorization") String token, @PageableDefault() Pageable pageable) {

		log.info("inside CampaignController: getCampaignInfo");
		CampaignList responseList = null;

		if (sortdir.equals("asc") || sortdir.equals("desc")) {
			try {
				responseList = campaignService.getCampaignInfo(status, sortdir, pageable, token);
				return ResponseEntity.status(200).body(responseList);
			} catch (Exception ex) {
				log.error("Error fetching campaign info: " + ex.getMessage());
			}
			return ResponseEntity.status(400).body(responseList);
		}
		return ResponseEntity.status(400).body(responseList);
	}

	@CrossOrigin
	@ApiOperation(value = "REST API for fetching Campaign list")
	@RequestMapping(value = "/{portalId}", method = RequestMethod.DELETE)
	public Map<String, Object> removeCampaignDetails(@PathVariable int portalId) {
		log.info("inside CampaignController: removeCampaignDetails");
		Map<String, Object> response = null;
		response = campaignService.deleteCampaign(portalId);
		return response;
	}

	@CrossOrigin
	@ApiOperation(value = "REST API for Updating Campaign in Drafts")
	@PutMapping
	public ResponseEntity<UpdateCampaignResponse> updateCampaign(
			@RequestBody UpdateCampaignRequest updateCampaignRequest) {
		log.info("inside CampaignController: updateCampaign");
		UpdateCampaignResponse response = null;
		try {
			response = campaignService.updateCampaign(updateCampaignRequest);
		} catch (Exception e) {
			log.error("Error creating campaign: " + e.getMessage());
		}
		if (response == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			return ResponseEntity.ok(response);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "REST API for resubmit Campaign in Drafts")
	@PutMapping(value = "/resubmit")
	public ResponseEntity<UpdateCampaignResponse> resubmitCampaign(
			@RequestBody UpdateCampaignRequest updateCampaignRequest,
			@RequestHeader(name = "Authorization") String token) {
		log.info("inside CampaignController: resubmitCampaign");
		UpdateCampaignResponse response = null;
		try {
			response = campaignService.resubmitCampaign(updateCampaignRequest, token);
		} catch (Exception e) {
			log.error("Error creating campaign: " + e.getMessage());
		}
		if (response == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			return ResponseEntity.ok(response);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "REST API for fetching Campaign list")
	@RequestMapping(value = "/{portalId}", method = RequestMethod.GET)
	public Campaign getById(@PathVariable(required = true) int portalId) {

		return campaignService.getCampaignById(portalId);
	}

	@CrossOrigin
	@ApiOperation(value = "Rest API for getting available Cherrypicks")
	@RequestMapping(value = "/avail", method = RequestMethod.POST)
	public ResponseEntity<AvailLMKResponse> getAvailResponse(@RequestBody AvailLMKRequest availLMKRequest)
			throws ParseException, InvalidParamException {

		log.info("Inside CampaignController: getAvailResponse ");

		Date sdate = new SimpleDateFormat("yyyy-MM-dd").parse(availLMKRequest.getStartDate());
		Date edate = new SimpleDateFormat("yyyy-MM-dd").parse(availLMKRequest.getEndDate());

		if (edate.before(sdate)) {
			throw new InvalidParamException("Please validate the input param");
		}
		AvailLMKResponse response = null;
		try {
			response = campaignService.getAvailableCherrypicks(availLMKRequest);
		} catch (Exception ex) {
			log.error("Error creating campaign: {} ", ex.getMessage());
		}
		if (response == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			return ResponseEntity.ok(response);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "REST API for Campaign approval by Contractor")
	@RequestMapping(value = "/approve", method = RequestMethod.PUT)
	public ResponseEntity<CampaignApproveRejectResp> campaignApproval(
			@RequestBody CampaignApproveReject campaignApproval) {
		log.info("inside CampaignController: campaignApproval");
		CampaignApproveRejectResp respose = campaignService.campaignApproval(campaignApproval);
		if (respose == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respose);
		} else {
			return ResponseEntity.status(respose.getStatus()).body(respose);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "REST API for Campaign reject by Contractor")
	@RequestMapping(value = "/reject", method = RequestMethod.PUT)
	public ResponseEntity<CampaignApproveRejectResp> campaignReject(@RequestBody CampaignApproveReject campaignReject) {
		log.info("inside CampaignController: campaignApproval");
		CampaignApproveRejectResp respose = campaignService.campaignReject(campaignReject);
		if (respose == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respose);
		} else {
			return ResponseEntity.status(respose.getStatus()).body(respose);
		}
	}

	@CrossOrigin
	@ApiOperation(value = "Rest API for getting available Cherrypicks")
	@RequestMapping(value = "/packageForecast", method = RequestMethod.POST)
	public ResponseEntity<TargetMarketLMKResp> getTargetMarketDetailsPackage(
			@RequestBody TargetMarketPkgReq multipleTargetMarketReq) throws ParseException, InvalidParamException {

		log.info("Inside CampaignController: getTargetMarketDetailsPackage ");

		TargetMarketLMKResp response = null;
		try {
			response = campaignService.getTargetMarketDetailsPackage(multipleTargetMarketReq);
		} catch (Exception ex) {
			log.error("Error getting details: {} ", ex.getMessage());
			throw new InvalidParamException("target market list cannot be null or empty");
		}
		// return ResponseEntity.ok(response);

		if (response == null) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
		} else {
			return ResponseEntity.ok(response);
		}

	}

	/**
	 * This API get the document basfrom LMK
	 * 
	 * @param campaignCode
	 * @param type
	 * @param httpServletResponse
	 * @return
	 * @throws IOException
	 */
	@CrossOrigin
	@ApiOperation(value = "REST API to download order confirmation Campaign")
	@RequestMapping(value = "/order", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getOrderConfirmationFormLMK(@RequestParam("campaignCode") Integer campaignCode,
			@RequestParam("type") String type, HttpServletResponse httpServletResponse) throws IOException {
		log.info("inside CampaignController: getOrderConfirmationFormLMK");

		ResponseEntity<byte[]> response = campaignService.getOrderConfirmation(campaignCode, type);
		File file = new File(campaignCode + ".pdf");
		return ResponseEntity.ok().contentLength(response.getBody().length)
				.header("Content-type", "application/octet-stream")
				.header("Content-disposition", "attachment; filename=\"" + file.getName() + "\"")
				.body(response.getBody());

	}

}
