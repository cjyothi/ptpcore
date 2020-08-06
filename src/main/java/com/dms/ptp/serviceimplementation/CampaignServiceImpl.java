package com.dms.ptp.serviceimplementation;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.mail.MessagingException;
import javax.net.ssl.SSLException;
import javax.transaction.Transactional;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.dms.ptp.dto.AvailLMKRequest;
import com.dms.ptp.dto.CampaignApproveReject;
import com.dms.ptp.dto.CampaignApprovelmkReq;
import com.dms.ptp.dto.CampaignCancelReq;
import com.dms.ptp.dto.CampaignList;
import com.dms.ptp.dto.CampaignRequest;
import com.dms.ptp.dto.CampaignRequestLMK;
import com.dms.ptp.dto.DayPartModel;
import com.dms.ptp.dto.JWTExtract;
import com.dms.ptp.dto.PackageForecastLMK;
import com.dms.ptp.dto.Packages;
import com.dms.ptp.dto.PageDecorator;
import com.dms.ptp.dto.ResultMessageList;
import com.dms.ptp.dto.ResultMessages;
import com.dms.ptp.dto.SalesAreaLMK;
import com.dms.ptp.dto.SalesAreaOnCampaign;
import com.dms.ptp.dto.TargetMarketPkgReq;
import com.dms.ptp.dto.UpdateCampaignRequest;
import com.dms.ptp.entity.Campaign;
import com.dms.ptp.entity.CampaignDaypart;
import com.dms.ptp.entity.CampaignRevision;
import com.dms.ptp.entity.CampaignSalesArea;
import com.dms.ptp.entity.Items;
import com.dms.ptp.entity.PackageAudienceForecast;
import com.dms.ptp.entity.PackageForecast;
import com.dms.ptp.entity.Spots;
import com.dms.ptp.exception.CampaignNotFoundException;
import com.dms.ptp.exception.InvalidParamException;
import com.dms.ptp.exception.SalesAreaException;
import com.dms.ptp.repository.ApprovalKeyRepository;
import com.dms.ptp.repository.CampaignRepository;
import com.dms.ptp.repository.ItemsRepository;
import com.dms.ptp.repository.PackageAudienceRepository;
import com.dms.ptp.repository.PackageRepository;
import com.dms.ptp.repository.SeedRepository;
import com.dms.ptp.response.AvailLMKResponse;
import com.dms.ptp.response.CampaignApproveRejectResp;
import com.dms.ptp.response.CampaignApprovelmkResp;
import com.dms.ptp.response.CampaignApprovelmkRespList;
import com.dms.ptp.response.CampaignCancelResp;
import com.dms.ptp.response.CampaignResponse;
import com.dms.ptp.response.CampaignResponseList;
import com.dms.ptp.response.SalesAreaLMKResponse;
import com.dms.ptp.response.TargetMarketLMKResp;
import com.dms.ptp.response.UpdateCampaignResponse;
import com.dms.ptp.response.UploadCampaignResult;
import com.dms.ptp.service.CampaignService;
import com.dms.ptp.util.Constant;
import com.dms.ptp.util.EmailUtil;
import com.dms.ptp.util.JWTUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import freemarker.template.TemplateException;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import reactor.netty.http.client.HttpClient;

@Service
@Slf4j
public class CampaignServiceImpl implements CampaignService {

    // RestTemplate restTemplate = new RestTemplate();

    @Autowired
    SeedRepository seedRepo;

    @Autowired
    ApprovalKeyRepository approvalKeyRepo;

    @Autowired
    CampaignRepository campaignRepo;

    @Autowired
    PackageRepository packageRepo;

    @Autowired
    ItemsRepository itemsRepository;
    
    @Autowired
	PackageAudienceRepository packageAudienceRepo;

	/*
	 * @Autowired WebClient webClient;
	 */
    @Autowired
    Environment env;

    @Autowired
    EmailUtil emailUtil;

    @Autowired
    JWTUtil jwtUtil;

    @Autowired
    RestTemplate restTemplate;
    
    private static final Logger log = LoggerFactory.getLogger(CampaignServiceImpl.class);

    @Override
    public Integer seedGenerator() {
        Integer seedId = seedRepo.findByIdFromSeed();
        return seedId;
    }

    @Override
    public String getProductsForAdvertisers(String agencyCode, String advertiserCode) {
        /** LMK url is commented. keeping for reference. Plz do not remove **/
        /*
         * final String url = "http://" + env.getProperty("lmkserver.address") + ":" +
         * env.getProperty("lmkserver.port") + "/CariaServices/products/" + agencyCode +
         * "/" + advertiserCode;
         */

        log.info("Inside CampaignServiceImpl: getProductsForAdvertisers ");
        final String url = "https://dmsbookingportaluat.multichoice.co.za/CariaServices/products/" + agencyCode + "/"
                + advertiserCode;

        String response = restTemplate.getForObject(url, String.class);

        return response;
    }

    @Override
    public CampaignResponseList createCampaign(CampaignRequest campaignRequest, String action, String token)
            throws MessagingException, IOException, TemplateException {
        log.info("Inside CampaignServiceImpl: createCampaign");
        CampaignResponse draftResponse = new CampaignResponse();
        CampaignResponseList campaignResponse = new CampaignResponseList();
        List<CampaignResponse> responseList = new ArrayList<CampaignResponse>();
        Campaign campaign = new Campaign();
        Campaign campaignDetails = new Campaign();
        List<Items> itemsList = new ArrayList<Items>();
        CampaignRequestLMK requestLmk = new CampaignRequestLMK();

        JWTExtract jwtExtract = jwtUtil.getIdRoleFromToken(token);
        // TRP-1621 - changes starts
        int portal_id;
        try {
        	if (campaignRequest.getPortalId() != 0 && campaignRepo.existsById(campaignRequest.getPortalId())) {
        		portal_id = campaignRequest.getPortalId();
        	} else {
        		portal_id = seedRepo.findByIdFromSeed();
        	}
        	// TRP-1621 changes ends

            try {
                log.info("request from UI: " + new ObjectMapper().writeValueAsString(campaignRequest));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            /** status -> 0: active, 1: pending, 2: post, 3: draft, 4: cancelled **/

            if (action.equals(Constant.ACTION_DRAFT)) {
                campaign.setStatus(0);
                campaignDetails = campaignDetails(campaignRequest, campaign, portal_id, jwtExtract, action);

                log.info("saving campaignDetails to campaign table");
                campaignRepo.save(campaignDetails);

                draftResponse.setStatus(HttpStatus.OK);
                draftResponse.setMessage("Campaign saved as Draft");
                responseList.add(draftResponse);
                campaignResponse.setTotal(responseList.size());
                campaignResponse.setItems(responseList);
                campaignResponse.setStatus(HttpStatus.OK);
            } else {
                campaignDetails = campaignDetails(campaignRequest, campaign, portal_id, jwtExtract, action);

                List<UploadCampaignResult> lmkResultList = lmkRequestMapping(campaignRequest, requestLmk, portal_id);

                for (UploadCampaignResult uploadCampaignResult : lmkResultList) {
                    CampaignResponse response = new CampaignResponse();
                    ResultMessageList resultMessageList = uploadCampaignResult.getResultMessages();
                    response.setCampaignCode(uploadCampaignResult.getCampaignCode());
                    response.setType(uploadCampaignResult.getType());
                    response.setSalesAreaName(uploadCampaignResult.getSalesAreaName());

                    List<ResultMessages> resultMessages = resultMessageList.getResultMessage();
                    for (ResultMessages resultMessages2 : resultMessages) {
                        response.setMessage(resultMessages2.getMessageText());
                        response.setMessageSeverity(resultMessages2.getMessageSeverity());
                    }
                    /*
                     * responseList.add(response); campaignResponse.setTotal(responseList.size());
                     * campaignResponse.setItems(responseList);
                     */

                    if (response.getMessageSeverity().equalsIgnoreCase(Constant.SUCCESS)
                            || (response.getMessageSeverity().equalsIgnoreCase(Constant.WARNING))) {

                        log.info("saving campaignDetails to campaign table");
                        
                        campaign.setStatus(1);
                        //changes starts - setting approval key
                        for (Items item:campaignRequest.getItems() ){

							if(item.getType().equals(uploadCampaignResult.getType())
									&& response.getMessageSeverity().equalsIgnoreCase(Constant.SUCCESS)) {
								
								item.setApprovalKeyID(uploadCampaignResult.getApprovalKeyID());
	                            item.setCampaign_code(response.getCampaignCode());
	                            item.setStatus(response.getMessageSeverity());
	                            item.setMessage(response.getMessage());
	                            itemsList.add(item);	
							}
                        }
                        //changes ends - setting approval key
						campaignDetails.setItems(itemsList);
                       
                        // emailUtil.sendMailForSignUp(signUpRequest.getUsername());
                        // emailUtil.sendMailToDMSAdmin("arnabashutosh.d@tataelxsi.co.in");

                        response.setStatus(HttpStatus.OK);
                        campaignResponse.setStatus(HttpStatus.OK);
                        responseList.add(response);
                        campaignResponse.setTotal(responseList.size());
                        campaignResponse.setItems(responseList);

                        // batch booking
                        if ((response.getSalesAreaName() != null) && (response.getType().equals("cherrypick"))) {
                            String resp = batchBookingLMK(response.getCampaignCode(), response.getSalesAreaName(),
                                    campaignRequest.getItems());
                            response.setBatchBookingResponse(resp);
                        } else {
                            response.setBatchBookingResponse("");
                        }
                    } else {
                        response.setStatus(HttpStatus.EXPECTATION_FAILED);
                        // emailUtil.sendMailToDMSAdmin("arnabashutosh.d@tataelxsi.co.in");
                        responseList.add(response);
                        campaignResponse.setStatus(HttpStatus.EXPECTATION_FAILED);
                        campaignResponse.setTotal(responseList.size());
                        campaignResponse.setItems(responseList);
                        //changes starts - setting approval key
                        campaign.setStatus(1); //status submitted
						for (Items item:campaignRequest.getItems() ){
							
							if(item.getType().equals(uploadCampaignResult.getType())
									&& response.getMessageSeverity().equalsIgnoreCase(Constant.ERROR)) {
								item.setApprovalKeyID(uploadCampaignResult.getApprovalKeyID());
	                            item.setCampaign_code(response.getCampaignCode());
	                            item.setStatus(response.getMessageSeverity());
	                            item.setMessage(response.getMessage());
	                            itemsList.add(item);	
							}
						}
                        //changes ends - setting approval key

						campaignDetails.setItems(itemsList);
                    }
                    
                    campaignRepo.save(campaignDetails);
					itemsList.clear();
                }
            }

        } catch (Exception e) {
            log.error("Exception occurred: " + e.getMessage());
            emailUtil.sendMailToDMSAdmin("arnabashutosh.d@tataelxsi.co.in");
            draftResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            draftResponse.setMessage(e.getMessage());
            responseList.add(draftResponse);
            campaignResponse.setTotal(responseList.size());
            campaignResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            campaignResponse.setItems(responseList);
        }
        return campaignResponse;
    }

    private String batchBookingLMK(int campaignCode, String salesAreaName, List<Items> items) {
        log.info("inside CampaignServiceImpl batchBookingLMK: cherry pick package type");
        String response = null;
        JSONObject bookingJson = new JSONObject();
        for (Items it : items) {
            JSONArray array = new JSONArray();
            for (int i = 0; i < it.getSpotList().size(); i++) {

                JSONObject jsonList = new JSONObject();
                if (it.getSpotList().get(i).getSales_area_name().contentEquals(salesAreaName)) {
                    jsonList.put("salesArea", it.getSpotList().get(i).getSales_area_no());
                    jsonList.put("scheduledDate", it.getSpotList().get(i).getScheduled_on());
                    jsonList.put("campaignCode", campaignCode);
                    jsonList.put("startTime", it.getSpotList().get(i).getStart_time());
                    jsonList.put("spotLength", it.getSpot_length());
                    array.put(jsonList);
                }
                bookingJson.put("campaignBookList", array);
            }

            String st = bookingJson.toString();
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestBody = new HttpEntity<>(st, headers);
            final String url = "https://dmsbookingportaluat.multichoice.co.za/LMKServices/batchBooking";
            response = restTemplate.exchange(url, HttpMethod.POST, requestBody, String.class).getBody();
            log.info("resp:" + response);
        }
        return response;
    }

    private List<UploadCampaignResult> lmkRequestMapping(CampaignRequest campaignRequest, CampaignRequestLMK requestLmk,
            int portal_id) {
        log.info("inside CampaignServiceImpl lmkRequestMapping");
        List<UploadCampaignResult> responseList = new ArrayList<UploadCampaignResult>();

        requestLmk.setTitle(campaignRequest.getTitle());
        requestLmk.setPortal_id(portal_id);

        int[] targetMarkets = campaignRequest.getTarget_markets();
        requestLmk.setTarget_markets(targetMarkets);

        requestLmk.setDeal_number(campaignRequest.getDeal_number());
        requestLmk.setProduct_code(campaignRequest.getProduct_code());
        requestLmk.setAdvertiser_code(campaignRequest.getAdvertiser_code());
        requestLmk.setObjective(campaignRequest.getObjective());
        requestLmk.setPanel(campaignRequest.getPanel());
        requestLmk.setBudget(campaignRequest.getBudget());
        requestLmk.setRating(campaignRequest.getRating());
        requestLmk.setStart(campaignRequest.getStart());
        requestLmk.setEnd(campaignRequest.getEnd());
        requestLmk.setSource_start(campaignRequest.getSource_start());
        requestLmk.setSource_end(campaignRequest.getSource_end());

        Packages pkg = new Packages();

        List<DayPartModel> dayPartmodelList = null;
        List<SalesAreaOnCampaign> salesAreaList = null;
        List<Items> cpkg = campaignRequest.getItems();
        for (Items campaignPackage : cpkg) {
            if (!(campaignPackage.getType().equals("cherrypick"))) {
                log.info("processing for type: " + campaignPackage.getType());
                dayPartmodelList = new ArrayList<DayPartModel>();
                salesAreaList = new ArrayList<SalesAreaOnCampaign>();
                pkg.setType(campaignPackage.getType());
                //changes starts - setting approval key
                pkg.setApprovalKeyId(approvalKeyRepo.findByIdFromApprovalKey());
                //changes starts - setting approval key
                pkg.setOptimization(campaignPackage.getOptimization());
                pkg.setCatalog_id(campaignPackage.getCatalog_id());
                pkg.setTarget_market(campaignPackage.getTarget_market());
                pkg.setStart(campaignPackage.getStart());
                pkg.setEnd(campaignPackage.getEnd());
                pkg.setSpot_length(campaignPackage.getSpot_length());
                pkg.setWeeks(campaignPackage.getWeeks());
                pkg.setSpot_rate(campaignPackage.getSpot_rate());
                pkg.setSpots(campaignPackage.getSpots());
                pkg.setRate(campaignPackage.getRate());
                pkg.setViews(campaignPackage.getViews());
                pkg.setCpt(campaignPackage.getCpt());
                pkg.setTvr(campaignPackage.getTvr());
                pkg.setCpp(campaignPackage.getCpp());
                pkg.setBusiness_type(campaignPackage.getBusiness_type());

                List<CampaignDaypart> dayPartList = campaignPackage.getDaypart();
                if (dayPartList != null) {
                    for (CampaignDaypart dayPart : dayPartList) {
                        DayPartModel dayPartModel = new DayPartModel();
                        dayPartModel.setCode(dayPart.getCode());
                        dayPartModel.setTitle(dayPart.getTitle());
                        dayPartModel.setPercentage(dayPart.getPercentage());
                        dayPartModel.setIs_average(dayPart.is_average());
                        dayPartmodelList.add(dayPartModel);
                        pkg.setDaypart(dayPartmodelList);
                    }
                }

                List<CampaignSalesArea> cpSales = campaignPackage.getSalesAreaOnCampaign();
                if (cpSales != null) {
                    for (CampaignSalesArea campaignSalesArea : cpSales) {
                        SalesAreaOnCampaign salesArea = new SalesAreaOnCampaign();
                        salesArea.setSales_area(campaignSalesArea.getSales_area());
                        salesArea.setSpots(campaignSalesArea.getSpots());
                        salesArea.setName(campaignSalesArea.getName());
                        salesAreaList.add(salesArea);
                        pkg.setSalesAreaOnCampaign(salesAreaList);
                    }
                }
                List<Packages> packages = new ArrayList<Packages>();
                UploadCampaignResult response = new UploadCampaignResult();
                packages.add(pkg);
                requestLmk.setItems(packages);
                response = callToLMK(requestLmk, pkg.getType(), null);
                responseList.add(response);
            } else {
                log.info("processing for type: cherrypick");

                pkg.setType(campaignPackage.getType());
                pkg.setOptimization(campaignPackage.getOptimization());
                pkg.setTarget_market(campaignPackage.getTarget_market());
                pkg.setSpot_length(campaignPackage.getSpot_length());
                pkg.setBusiness_type(campaignPackage.getBusiness_type());
                //changes starts - setting approval key
                pkg.setApprovalKeyId(approvalKeyRepo.findByIdFromApprovalKey());
                //changes ends - setting approval key

                Set<String> slAreaNames = new HashSet<>();
                for (Spots s : campaignPackage.getSpotList()) {
                    slAreaNames.add(s.getSales_area_name());

                    if (s.getComment() != null) {
                        log.info("check for comment, if null then package else spot");
                        List<UploadCampaignResult> spotCherryPickPackage = packageDetails(pkg, s, requestLmk);
                        responseList = spotCherryPickPackage;
                        // return responseList;
                    }
                }
                log.info("slAreaNames: " + slAreaNames);

                Map<String, Double> avgCpp = campaignPackage.getSpotList().stream().collect(Collectors
                        .groupingBy(spot -> spot.getSales_area_name(), Collectors.averagingDouble(Spots::getCpp)));

                Map<String, Double> avgCpt = campaignPackage.getSpotList().stream().collect(Collectors
                        .groupingBy(spot -> spot.getSales_area_name(), Collectors.averagingDouble(Spots::getCpt)));

                Map<String, Double> avgPrice = campaignPackage.getSpotList().stream().collect(Collectors
                        .groupingBy(spot -> spot.getSales_area_name(), Collectors.averagingDouble(Spots::getPrice)));

                Map<String, Double> avgRating = campaignPackage.getSpotList().stream().collect(Collectors
                        .groupingBy(spot -> spot.getSales_area_name(), Collectors.averagingDouble(Spots::getRating)));

                Map<String, Optional<Spots>> minScheduledOn = minScheduledOn(campaignPackage.getSpotList());
                Map<String, Optional<Spots>> maxScheduledOn = maxScheduledOn(campaignPackage.getSpotList());

                Map<String, Long> countValue = campaignPackage.getSpotList().stream().collect(
                        Collectors.groupingBy(Spots::getSales_area_name, LinkedHashMap::new, Collectors.counting()));

                String salesAreaName = "";

                for (String s1 : slAreaNames) {
                    log.info("s1: " + s1);
                    salesAreaName = s1;

                    log.info("setting avg values");
                    //changes starts - setting approval key
                    pkg.setApprovalKeyId(approvalKeyRepo.findByIdFromApprovalKey());
                    //changes ends - setting approval key
                    pkg.setCpp(avgValues(avgCpp, salesAreaName).intValue());
                    pkg.setCpt(avgValues(avgCpt, salesAreaName).intValue());
                    pkg.setRate(avgValues(avgPrice, salesAreaName).intValue());

                    if (minScheduledOn != null) {
                        // String minSchedule = minValues(minScheduledOn, salesAreaName).toString();
                        String minScheduleOnFormatted = minValues(minScheduledOn, salesAreaName).toString();
                        pkg.setStart(minScheduleOnFormatted);
                    }
                    if (maxScheduledOn != null) {
                        // String maxSchedule = maxValues(maxScheduledOn, salesAreaName).toString();
                        String maxScheduleFormatted = maxValues(maxScheduledOn, salesAreaName).toString();
                        pkg.setEnd(maxScheduleFormatted);
                    }
                    pkg.setSpots(countValues(countValue, salesAreaName).intValue());

                    List<Spots> spList = campaignPackage.getSpotList();
                    String url = "";

                    if (spList != null) {
                        log.info("setting salesArea values");
                        SalesAreaOnCampaign salesArea = new SalesAreaOnCampaign();

                        List<SalesAreaOnCampaign> slList = new ArrayList<SalesAreaOnCampaign>();
                        for (Spots sp : spList) {
                            if (sp.getSales_area_name().equalsIgnoreCase(salesAreaName)) {
                                if ((sp.getSales_area_no() == 0) && (sp.getSales_area_name() != null)
                                        && (!sp.getSales_area_name().isEmpty())) {
                                    RestTemplate template = new RestTemplate();
                                    url = "https://dmsbookingportaluat.multichoice.co.za/LMKServices/salesArea/byName/"
                                            + salesAreaName;
                                    String resp = template.getForObject(url, String.class);
                                    log.info("sales area byName response: " + salesAreaName + resp);

                                    JSONObject jsonSales = new JSONObject(resp);
                                    int salesAreaNo = (int) jsonSales.getJSONArray("items").getJSONObject(0)
                                            .get("salesAreaNumber");

                                    sp.setSales_area_no(salesAreaNo);
                                } else if ((salesAreaName == null || salesAreaName.isEmpty())
                                        && (sp.getSales_area_no() != 0)) {
                                    RestTemplate template = new RestTemplate();
                                    url = "https://dmsbookingportaluat.multichoice.co.za/LMKServices/salesArea/byId/"
                                            + sp.getSales_area_no();
                                    String resp = template.getForObject(url, String.class);
                                    log.info("sales area byId response: " + salesAreaName + resp);

                                    JSONObject jsonSales = new JSONObject(resp);
                                    String name = (String) jsonSales.getJSONArray("items").getJSONObject(0).get("name");
                                    sp.setSales_area_name(name);

                                } else if ((sp.getSales_area_no() == 0)
                                        && (salesAreaName == null || salesAreaName.isEmpty())) {
                                    throw new SalesAreaException(Constant.SALES_AREA_EXCEPTION);
                                }
                            }
                        }

                        Map<String, Double> sAreaNum = campaignPackage.getSpotList().stream()
                                .collect(Collectors.groupingBy(spot -> spot.getSales_area_name(),
                                        Collectors.averagingInt(Spots::getSales_area_no)));
                        int sArea = avgValues(sAreaNum, salesAreaName).intValue();

                        salesArea.setSales_area(sArea);
                        salesArea.setName(salesAreaName);
                        slList.add(salesArea);
                        pkg.setSalesAreaOnCampaign(slList);

                        List<Packages> packages = new ArrayList<Packages>();
                        UploadCampaignResult response = new UploadCampaignResult();
                        packages.add(pkg);
                        requestLmk.setItems(packages);
                        response = callToLMK(requestLmk, pkg.getType(), salesAreaName);
                        responseList.add(response);
                    }
                }
            }
        }

        log.info("lmk responseList: " + responseList);
        return responseList;
    }

    private List<UploadCampaignResult> packageDetails(Packages pkg, Spots s, CampaignRequestLMK requestLmk) {
        log.info("inside packageDetails: cherry pick package type");
        List<UploadCampaignResult> responseList = new ArrayList<UploadCampaignResult>();

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);
        //changes starts - setting approval key - commented as app key alrady set in pkg
       // pkg.setApprovalKeyId(approvalKeyRepo.findByIdFromApprovalKey());
        //changes ends - setting approval key - commented as app key alrady set in pkg
        pkg.setCpp((int) s.getCpp());
        pkg.setCpt((int) s.getCpt());
        pkg.setPrice((int) s.getPrice());
        pkg.setSpots((int) s.getSpots());

        String scheduleOnFormatted = s.getScheduled_on();
        pkg.setStart(scheduleOnFormatted);
        pkg.setEnd(scheduleOnFormatted);

        String url = "";
        SalesAreaLMKResponse saResponse = new SalesAreaLMKResponse();
        List<SalesAreaOnCampaign> slAreaList = new ArrayList<SalesAreaOnCampaign>();
        SalesAreaOnCampaign slArea = new SalesAreaOnCampaign();
        if ((s.getSales_area_no() == 0) && (s.getSales_area_name() != null) && (!s.getSales_area_name().isEmpty())) {
            url = "https://dmsbookingportaluat.multichoice.co.za/LMKServices/salesArea/byName/"
                    + s.getSales_area_name();
            saResponse = restTemplate.getForObject(url, SalesAreaLMKResponse.class);
        } else if ((s.getSales_area_name() == null || s.getSales_area_name().isEmpty())
                && (s.getSales_area_no() != 0)) {

            url = "https://dmsbookingportaluat.multichoice.co.za/LMKServices/salesArea/byId/" + s.getSales_area_no();
            saResponse = restTemplate.getForObject(url, SalesAreaLMKResponse.class);
        } else if ((s.getSales_area_no() == 0)
                && (s.getSales_area_name() == null || s.getSales_area_name().isEmpty())) {
            throw new SalesAreaException(Constant.SALES_AREA_EXCEPTION);
        } else if(!(s.getSales_area_name() == null || s.getSales_area_name().isEmpty())
                && (s.getSales_area_no() != 0)) {
        	
        	slArea.setSales_area(s.getSales_area_no());
            slArea.setName(s.getSales_area_name());
            slAreaList.add(slArea);
            pkg.setSalesAreaOnCampaign(slAreaList);
        }

        
        if(saResponse.getItems() != null && saResponse.getTotal() !=0) {
        	for (SalesAreaLMK sa : saResponse.getItems()) {
                slArea.setSales_area(sa.getSalesAreaNumber());
                slArea.setName(sa.getName());
                slAreaList.add(slArea);
                pkg.setSalesAreaOnCampaign(slAreaList);
            }
        }

        
		/*
		 * for (SalesAreaLMK sa : saResponse.getItems()) {
		 * slArea.setSales_area(sa.getSalesAreaNumber()); slArea.setName(sa.getName());
		 * slAreaList.add(slArea); pkg.setSalesAreaOnCampaign(slAreaList); }
		 */

        List<Packages> packages = new ArrayList<Packages>();
        UploadCampaignResult response = new UploadCampaignResult();
        packages.add(pkg);
        requestLmk.setItems(packages);
        response = callToLMK(requestLmk, pkg.getType(), s.getSales_area_name());
        responseList.add(response);

        return responseList;
    }

    public static Double avgValues(Map<String, Double> map, String salesAreaName) {
        double avgValues = 0;
        for (Entry<String, Double> entry : map.entrySet()) {
            if (entry.getKey().equals(salesAreaName)) {
                avgValues = (double) entry.getValue();
            }
        }
        return avgValues;
    }

    public static String minValues(Map<String, Optional<Spots>> map, String salesAreaName) {
        Optional<Spots> avgValues = null;

        for (Entry<String, Optional<Spots>> entry : map.entrySet()) {
            if (entry.getKey().equals(salesAreaName)) {
                avgValues = entry.getValue();
            }
        }
        Spots s = avgValues.get();
        String scheduleOnMin = s.getScheduled_on();
        return scheduleOnMin;
    }

    public static String maxValues(Map<String, Optional<Spots>> map, String salesAreaName) {

        Optional<Spots> avgValues = null;
        for (Entry<String, Optional<Spots>> entry : map.entrySet()) {
            if (entry.getKey().equals(salesAreaName)) {
                avgValues = entry.getValue();
            }
        }
        Spots s = avgValues.get();
        String scheduleOnMax = s.getScheduled_on();
        return scheduleOnMax;
    }

    public static Long countValues(Map<String, Long> map, String salesAreaName) {
        long countValues = 0;
        /*
         * for (Long entry : map.values()) { countValues = entry + countValues; }
         */
        for (Entry<String, Long> entry : map.entrySet()) {
            if (entry.getKey().equals(salesAreaName)) {
                countValues = entry.getValue();
            }
        }
        return countValues;
    }

    public static String formatDate(String obj) {
        String formattedDate = "";
        try {
            Date date = new SimpleDateFormat("dd/mm/yyyy").parse(obj);
            formattedDate = new SimpleDateFormat("yyyy-mm-dd").format(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return formattedDate;
    }

    public static Map<String, Optional<Spots>> minScheduledOn(List<Spots> spots) {
        Comparator<Spots> byEndDate = Comparator.comparing(Spots::getScheduled_on);
        Map<String, Optional<Spots>> collect = spots.stream().collect(
                Collectors.groupingBy(Spots::getSales_area_name, Collectors.reducing(BinaryOperator.minBy(byEndDate))));
        return collect;
    }

    public static Map<String, Optional<Spots>> maxScheduledOn(List<Spots> spots) {
        Comparator<Spots> byEndDate = Comparator.comparing(Spots::getScheduled_on);
        Map<String, Optional<Spots>> collect = spots.stream().collect(
                Collectors.groupingBy(Spots::getSales_area_name, Collectors.reducing(BinaryOperator.maxBy(byEndDate))));
        return collect;
    }

    private UploadCampaignResult callToLMK(CampaignRequestLMK requestToLmk, String type, String salesAreaName) {
        log.info("Inside CampaignServiceImpl: callToLMK");

        /** LMK url is commented. keeping for reference. Plz do not remove **/
        /*
         * final String url = "http://" + env.getProperty("lmkserver.address") + ":" +
         * env.getProperty("lmkserver.port") + "/CariaServices/createCampaign";
         */

        final String url = "https://dmsbookingportaluat.multichoice.co.za/CariaServices/createCampaign";

        try {
            log.info("request to lmk: " + new ObjectMapper().writeValueAsString(requestToLmk));
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CampaignRequestLMK> requestBody = new HttpEntity<>(requestToLmk, headers);
        CampaignResponse responseList = restTemplate.postForObject(url, requestBody, CampaignResponse.class);

        try {
            log.info("response from lmk: " + new ObjectMapper().writeValueAsString(responseList));
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        UploadCampaignResult result = new UploadCampaignResult();
        List<UploadCampaignResult> uploadCampaignResult = responseList.getUploadCampaignResult();

        for (UploadCampaignResult campaignResult : uploadCampaignResult) {
            for (int i = 0; i < uploadCampaignResult.size(); i++) {
            	result.setApprovalKeyID(campaignResult.getApprovalKeyID());
                result.setCampaignCode(campaignResult.getCampaignCode());
                result.setType(type);
                result.setSalesAreaName(salesAreaName);
                result.setResultMessages(campaignResult.getResultMessages());
            }
        }
        return result;
    }

    private Campaign campaignDetails(CampaignRequest campaignRequest, Campaign campaign, int portal_id,
            JWTExtract jwtExtract, String action) {
        log.info("Inside CampaignServiceImpl: campaignDetails");
        List<Items> itemsList = new ArrayList<>();
        campaign.setTitle(campaignRequest.getTitle());
        campaign.setPortal_id(portal_id);
        campaign.setDeal_number(campaignRequest.getDeal_number());
        campaign.setProduct_code(campaignRequest.getProduct_code());
        campaign.setAdvertiser_code(campaignRequest.getAdvertiser_code());
        campaign.setObjective(campaignRequest.getObjective());
        campaign.setPanel(campaignRequest.getPanel());
        campaign.setBudget(campaignRequest.getBudget());
        campaign.setLength(campaignRequest.getLength());
        campaign.setRating(campaignRequest.getRating());
        campaign.setStart(campaignRequest.getStart());
        campaign.setEnd(campaignRequest.getEnd());
        campaign.setSource_start(campaignRequest.getSource_start());
        campaign.setSource_end(campaignRequest.getSource_end());
        campaign.setPo(campaignRequest.getPo());
        //changes for agency - starts
        campaign.setAgencyCode(campaignRequest.getAgencyCode());
        //changes for agency - ends

        if (jwtExtract.getUserRole().equals(Constant.ROLE_AGENCY_MEDIA_PLANNER) ||
				jwtExtract.getUserRole().equals(Constant.ROLE_DMS_MEDIA_PLANNER)) {
            campaign.setUserId(jwtExtract.getUserId());
        }

        if (action.equals(Constant.ACTION_DRAFT)) {
			List<Integer> secondaryTargetMarketslist = new ArrayList<>();
			campaign.setSecondaryTargetMarkets(secondaryTargetMarketslist);
		} else {
			ArrayList<Integer> secondaryTargetMarketslist = IntStream.of(campaignRequest.getSecondaryTargetMarkets())
					.boxed().collect(Collectors.toCollection(ArrayList::new));
			campaign.setSecondaryTargetMarkets(secondaryTargetMarketslist);
		}
        
        ArrayList<Integer> list = IntStream.of(campaignRequest.getTarget_markets()).boxed()
                .collect(Collectors.toCollection(ArrayList::new));
        campaign.setTarget_markets(list);

        campaign.setFlightCode(campaignRequest.getFlightCode());
        
        if (campaignRequest.getItems() != null) {
			for (Items item : campaignRequest.getItems()) {
				itemsList.add(item);
			}
			campaign.setItems(itemsList);
		}
        campaign.setItems(itemsList);
        // campaign.setRevision(campaignRequest.getRevision());

        // doc upload fields
        campaign.setFolderName(campaignRequest.getFolderName());
        
        campaign.setCreatedOn(LocalDateTime.now());
		campaign.setSubmittedDate(LocalDateTime.now());
		campaign.setModifiedOn(LocalDateTime.now());

        return campaign;
    }

    @Override
    public CampaignList getCampaignList(String sortdir, Pageable pageable, String token) {
        log.info("Inside CampaignServiceImpl: getCampaignList");

        JWTExtract jwtExtract = jwtUtil.getIdRoleFromToken(token);
        log.info("userid: " + jwtExtract.getUserId() + " userRole: " + jwtExtract.getUserRole());

        new PageImpl<>(new ArrayList<>());
        Page<Campaign> allList;
        Page<Campaign> activeList;
        Page<Campaign> pendingList;
        Page<Campaign> postList;
        Page<Campaign> draftList;
        Page<Campaign> cancelledList;
        //changes starts - setting statuses according to frondend
        if (sortdir.equals("asc")) {
            if (jwtExtract.getUserRole().equals("Media Planner")) {
                allList = campaignRepo.findAllByUserId(jwtExtract.getUserId(), pageable);
                activeList = campaignRepo.getApprovedCampaign(3, jwtExtract.getUserId(), pageable);
                pendingList = campaignRepo.getCampaignByStatus(1,2, jwtExtract.getUserId(), pageable);
                postList = campaignRepo.getCampaignByFlightDates(3, jwtExtract.getUserId(), pageable);
                draftList = campaignRepo.getCampaignByStatus(0,0, jwtExtract.getUserId(), pageable);
                cancelledList = campaignRepo.getCampaignByStatus(4,5, jwtExtract.getUserId(), pageable);
            } else {
                allList = campaignRepo.gelAllCampaignList(pageable);
                activeList = campaignRepo.getApprovedCampaign(3, pageable);
                pendingList = campaignRepo.getCampaignByStatus(1,2, pageable);
                postList = campaignRepo.getCampaignByFlightDates(3, pageable);
                draftList = campaignRepo.getCampaignByStatus(0,0, pageable);
                cancelledList = campaignRepo.getCampaignByStatus(4,5, pageable);
            }
        } else {
            if (jwtExtract.getUserRole().equals("Media Planner")) {
                allList = campaignRepo.gelAllCampaignList(jwtExtract.getUserId(), pageable);
                activeList = campaignRepo.getApprovedCampaignDesc(3,jwtExtract.getUserId(), pageable);
                pendingList = campaignRepo.getCampaignByStatusDesc(1,2, jwtExtract.getUserId(), pageable);
                postList = campaignRepo.getCampaignByFlightDatesDesc(3, jwtExtract.getUserId(), pageable);
                draftList = campaignRepo.getCampaignByStatusDesc(0,0, jwtExtract.getUserId(), pageable);
                cancelledList = campaignRepo.getCampaignByStatusDesc(4,5, jwtExtract.getUserId(), pageable);
            } else {
                allList = campaignRepo.gelAllCampaignList(pageable);
                activeList = campaignRepo.getApprovedCampaignDesc(3, pageable);
                pendingList = campaignRepo.getCampaignByStatusDesc(1,2, pageable);
                postList = campaignRepo.getCampaignByFlightDatesDesc(3, pageable);
                draftList = campaignRepo.getCampaignByStatusDesc(0,0, pageable);
                cancelledList = campaignRepo.getCampaignByStatusDesc(4,5, pageable);
            }
        }
        //changes ends - setting statuses according to frondend
        CampaignList campaignList = new CampaignList();
        campaignList.setAllList(new PageDecorator<>(allList));
        campaignList.setActiveList(new PageDecorator<>(activeList));
        campaignList.setPendingList(new PageDecorator<>(pendingList));
        campaignList.setPostList(new PageDecorator<>(postList));
        campaignList.setDraftList(new PageDecorator<>(draftList));
        campaignList.setCancelledList(new PageDecorator<>(cancelledList));
        return campaignList;
    }

    @Override
	public CampaignList getCampaignInfo(int status, String sortdir, Pageable pageable, String token) {
		log.info("Inside CampaignServiceImpl: getCampaignList");

		JWTExtract jwtExtract = jwtUtil.getIdRoleFromToken(token);
		log.info("userid: " + jwtExtract.getUserId() + " userRole: " + jwtExtract.getUserRole());

		new PageImpl<>(new ArrayList<>());
		Page<Campaign> campaignStatusList = null;

		if (sortdir.equals("asc")) {
			if (jwtExtract.getUserRole().equals(Constant.ROLE_AGENCY_MEDIA_PLANNER) ||
					jwtExtract.getUserRole().equals(Constant.ROLE_DMS_MEDIA_PLANNER)) {
				switch (status) {
				case 0: // active: approved
					campaignStatusList = campaignRepo.getApprovedCampaign(3, jwtExtract.getUserId(), pageable);
					break;
				case 1: // pending: submitted, amended
					campaignStatusList = campaignRepo.getCampaignByStatus(1, 2, jwtExtract.getUserId(), pageable);
					break;
				case 2: // post
					campaignStatusList = campaignRepo.getCampaignByFlightDates(3, jwtExtract.getUserId(), pageable);
					break;
				case 3: // draft
					campaignStatusList = campaignRepo.getCampaignByStatus(0, 0, jwtExtract.getUserId(), pageable);
					break;
				case 4: // cancelled
					campaignStatusList = campaignRepo.getCampaignByStatus(4, 4, jwtExtract.getUserId(), pageable);
					break;
				case 5: // rejected
					campaignStatusList = campaignRepo.getCampaignByStatus(5, 5, jwtExtract.getUserId(), pageable);
					break;
				default:
					campaignStatusList = campaignRepo.findAllByUserId(jwtExtract.getUserId(), pageable);
					log.info("Default ");
				}
			} else {
				switch (status) {
				case 0: // active: approved
					campaignStatusList = campaignRepo.getApprovedCampaign(3, pageable);
					break;
				case 1: // pending: submitted, amended
					campaignStatusList = campaignRepo.getCampaignByStatus(1, 2, pageable);
					break;
				case 2: // post
					campaignStatusList = campaignRepo.getCampaignByFlightDates(3, pageable);
					break;
				case 3: // draft
					campaignStatusList = campaignRepo.getCampaignByStatus(0, 0, pageable);
					break;
				case 4: // cancelled
					campaignStatusList = campaignRepo.getCampaignByStatus(4, 4, pageable);
					break;
				case 5: // rejected
					campaignStatusList = campaignRepo.getCampaignByStatus(5, 5, pageable);
					break;
				default:
					campaignStatusList = campaignRepo.gelAllCampaignList(pageable);
					log.info("Default ");
				}
			}
		} else {
			if (jwtExtract.getUserRole().equals(Constant.ROLE_AGENCY_MEDIA_PLANNER) ||
					jwtExtract.getUserRole().equals(Constant.ROLE_DMS_MEDIA_PLANNER)) {
				switch (status) {
				case 0: // active: approved
					campaignStatusList = campaignRepo.getApprovedCampaignDesc(3, jwtExtract.getUserId(), pageable);
					break;
				case 1: // pending: submitted, amended
					campaignStatusList = campaignRepo.getCampaignByStatusDesc(1, 2, jwtExtract.getUserId(), pageable);
					break;
				case 2: // post
					campaignStatusList = campaignRepo.getCampaignByFlightDatesDesc(3, jwtExtract.getUserId(), pageable);
					break;
				case 3: // draft
					campaignStatusList = campaignRepo.getCampaignByStatusDesc(0, 0, jwtExtract.getUserId(), pageable);
					break;
				case 4: // cancelled
					campaignStatusList = campaignRepo.getCampaignByStatusDesc(4, 4, jwtExtract.getUserId(), pageable);
					break;
				case 5: // rejected
					campaignStatusList = campaignRepo.getCampaignByStatusDesc(5, 5, jwtExtract.getUserId(), pageable);
					break;
				default:
					campaignStatusList = campaignRepo.gelAllCampaignList(jwtExtract.getUserId(), pageable);
					log.info("Default ");
				}

			} else {
				switch (status) {
				case 0: // active: approved
					campaignStatusList = campaignRepo.getApprovedCampaignDesc(3, pageable);
					break;
				case 1: // pending: submitted, amended
					campaignStatusList = campaignRepo.getCampaignByStatusDesc(1, 2, pageable);
					break;
				case 2: // post
					campaignStatusList = campaignRepo.getCampaignByFlightDatesDesc(3, pageable);
					break;
				case 3: // draft
					campaignStatusList = campaignRepo.getCampaignByStatusDesc(0, 0, pageable);
					break;
				case 4: // cancelled
					campaignStatusList = campaignRepo.getCampaignByStatusDesc(4, 4, pageable);
					break;
				case 5: // rejected
					campaignStatusList = campaignRepo.getCampaignByStatusDesc(5, 5, pageable);
					break;
				default:
					campaignStatusList = campaignRepo.gelAllCampaignList(pageable);
					log.info("Default ");
				}
			}
		}

		CampaignList campaignList = new CampaignList();
		/*
		 * campaignList.setAllList(new PageDecorator<>(allList));
		 * campaignList.setActiveList(new PageDecorator<>(activeList));
		 * campaignList.setPendingList(new PageDecorator<>(pendingList));
		 * campaignList.setPostList(new PageDecorator<>(postList));
		 * campaignList.setDraftList(new PageDecorator<>(draftList));
		 * campaignList.setCancelledList(new PageDecorator<>(cancelledList));
		 */

		campaignList.setCampaignStatusList(new PageDecorator<>(campaignStatusList));
		return campaignList;

	}
    
    
    @Override
    public Map<String, Object> deleteCampaign(int portalId) throws CampaignNotFoundException {
        log.info("Inside CampaignServiceImpl: deleteCampaign");
        Campaign campaign = campaignRepo.findById(portalId).orElseThrow(
                () -> new CampaignNotFoundException("Campaign not found for this PortalId :: " + portalId));

        campaignRepo.delete(campaign);
        Map<String, Object> response = new HashMap<>();
        response.put("status", Boolean.TRUE);
        response.put("Message", "Deleted successfully");
        return response;
    }

    @Bean
    public WebClient createWebClient() throws SSLException {
        SslContext sslContext = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();
        /*
         * ClientHttpConnector httpConnector = new ReactorClientHttpConnector(
         * sslProviderBuilder -> sslProviderBuilder.sslContext(sslContext)); return
         * WebClient.builder().clientConnector(httpConnector).build();
         */
        
        HttpClient httpClient = HttpClient.create().secure(sslContextSpec -> sslContextSpec.sslContext(sslContext));
        ClientHttpConnector httpConnector = new ReactorClientHttpConnector(httpClient);
        return WebClient.builder().clientConnector(httpConnector).build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    @Transactional
    public UpdateCampaignResponse updateCampaign(UpdateCampaignRequest updateCampaignRequest)
            throws CampaignNotFoundException {
        log.info("Inside CampaignServiceImpl: updateCampaign");

        CampaignRevision campaignRevision = new CampaignRevision();
        List<CampaignRevision> revisionList = new ArrayList<CampaignRevision>();

        Campaign c = campaignRepo.findById(updateCampaignRequest.getPortalId())
                .orElseThrow(() -> new CampaignNotFoundException(
                        "Couldn't find a Campaign with id: " + updateCampaignRequest.getPortalId()));
        UpdateCampaignResponse response = null;
        if (c.getStatus() != 0) {
            response = new UpdateCampaignResponse();
            response.setPortalId(updateCampaignRequest.getPortalId());
            response.setStatus(HttpStatus.EXPECTATION_FAILED);
            response.setMessage(Constant.CAMPAIGN_NOT_IN_DRAFT);
            response.setMessageSeverity(Constant.CAMPAIGN_NOT_UPDTAED);
            return response;
        }
        List<Items> itemList = c.getItems();
        try {
            itemsRepository.deleteAll(itemList);
            c.setTitle(updateCampaignRequest.getUpdatePayload().getTitle());
            ArrayList<Integer> list = IntStream.of(updateCampaignRequest.getUpdatePayload().getTarget_markets()).boxed()
                    .collect(Collectors.toCollection(ArrayList::new));
            c.setTarget_markets(list);
            c.setDeal_number(updateCampaignRequest.getUpdatePayload().getDeal_number());
            c.setProduct_code(updateCampaignRequest.getUpdatePayload().getProduct_code());
            c.setAdvertiser_code(updateCampaignRequest.getUpdatePayload().getAdvertiser_code());
            c.setObjective(updateCampaignRequest.getUpdatePayload().getObjective());
            c.setPanel(updateCampaignRequest.getUpdatePayload().getPanel());
            c.setBudget(updateCampaignRequest.getUpdatePayload().getBudget());
            c.setLength(updateCampaignRequest.getUpdatePayload().getLength());
            c.setRating(updateCampaignRequest.getUpdatePayload().getRating());
            c.setSource_start(updateCampaignRequest.getUpdatePayload().getSource_start());
            c.setSource_end(updateCampaignRequest.getUpdatePayload().getSource_end());
            c.getItems().clear();
            c.setItems(updateCampaignRequest.getUpdatePayload().getItems());
            //changes for agency - starts
            c.setAgencyCode(updateCampaignRequest.getUpdatePayload().getAgencyCode());
            //changes for agency - ends
            /** setting revision for campaign_revision **/
            LocalDateTime revision = LocalDateTime.now();
            log.info("setting revision for campaign_revision table: " + revision);

            campaignRevision.setRevision(revision);
            revisionList.add(campaignRevision);
            c.setRevision(revisionList);
            
            c.setModifiedOn(LocalDateTime.now());
          //emailUtil.sendMail(username, flag, reason);

            Campaign result = campaignRepo.save(c);
            response = new UpdateCampaignResponse();
            response.setPortalId(result.getPortal_id());
            response.setStatus(HttpStatus.OK);
            response.setMessage(Constant.CAMPAIGN_DRAFT_UPDATE);
            response.setMessageSeverity(Constant.SUCCESS);
            response.setCampaignStatus(Constant.CAMPAIGN_STATUS_DRAFT);
        } catch (Exception e) {
            log.error("Error in updateCampaign : CampaignServiceImpl  : " + e.getMessage());
            response.setMessage("error occurred: " + e.getMessage());
            throw e;
        }
        return response;
    }

    @Override
    @Transactional
    public UpdateCampaignResponse resubmitCampaign(UpdateCampaignRequest updateCampaignRequest, String token)
            throws CampaignNotFoundException {
        log.info("Inside CampaignServiceImpl: resubmitCampaign");

        JWTExtract jwtExtract = jwtUtil.getIdRoleFromToken(token);
        CampaignRevision campaignRevision = new CampaignRevision();
        List<CampaignRevision> revisionList = new ArrayList<CampaignRevision>();

        Campaign c = campaignRepo.findById(updateCampaignRequest.getPortalId())
                .orElseThrow(() -> new CampaignNotFoundException(
                        "Couldn't find a Campaign with id: " + updateCampaignRequest.getPortalId()));

        UpdateCampaignResponse response = null;
        List<Items> itemList = c.getItems();
        try {
            itemsRepository.deleteAll(itemList);

            c.setTitle(updateCampaignRequest.getUpdatePayload().getTitle());
            ArrayList<Integer> list = IntStream.of(updateCampaignRequest.getUpdatePayload().getTarget_markets()).boxed()
                    .collect(Collectors.toCollection(ArrayList::new));
            c.setTarget_markets(list);
            c.setDeal_number(updateCampaignRequest.getUpdatePayload().getDeal_number());
            c.setProduct_code(updateCampaignRequest.getUpdatePayload().getProduct_code());
            c.setAdvertiser_code(updateCampaignRequest.getUpdatePayload().getAdvertiser_code());
            c.setObjective(updateCampaignRequest.getUpdatePayload().getObjective());
            c.setPanel(updateCampaignRequest.getUpdatePayload().getPanel());
            c.setBudget(updateCampaignRequest.getUpdatePayload().getBudget());
            c.setLength(updateCampaignRequest.getUpdatePayload().getLength());
            c.setRating(updateCampaignRequest.getUpdatePayload().getRating());
            c.setSource_start(updateCampaignRequest.getUpdatePayload().getSource_start());
            c.setSource_end(updateCampaignRequest.getUpdatePayload().getSource_end());
            c.getItems().clear();
            c.setItems(updateCampaignRequest.getUpdatePayload().getItems());
            c.setUserId(jwtExtract.getUserId());
            //changes for agency - starts
            c.setAgencyCode(updateCampaignRequest.getUpdatePayload().getAgencyCode());
            //changes for agency - ends

            /** setting revision for campaign_revision **/
            LocalDateTime revision = LocalDateTime.now();
            log.info("setting revision for campaign_revision table: " + revision);

            campaignRevision.setRevision(revision);
            revisionList.add(campaignRevision);
            c.setRevision(revisionList);
            
            c.setModifiedOn(LocalDateTime.now());
          //emailUtil.sendMail(username, flag, reason);

            /**
             * 0:draft, 1:submitted, 2:amended, 3:approved, 4:cancelled, 5:rejected
             **/
            response = new UpdateCampaignResponse();
            if (jwtExtract.getUserRole().equals(Constant.ROLE_AGENCY_MEDIA_PLANNER) ||
					jwtExtract.getUserRole().equals(Constant.ROLE_DMS_MEDIA_PLANNER)) {
                log.info("updating the campaign status to 1:submitted");
                c.setStatus(1);
                response.setMessage(Constant.CAMPAIGN_SUBMITTED);
                response.setCampaignStatus(Constant.CAMPAIGN_STATUS_SUBMITTED);
            } else if (jwtExtract.getUserRole().equals(Constant.ROLE_CONTRACTOR)) {
                log.info("updating the campaign status to 2:amended");
                c.setStatus(2);
                response.setMessage(Constant.CAMPAIGN_AMENDED);
                response.setCampaignStatus(Constant.CAMPAIGN_STATUS_AMENDED);
            }

            Campaign result = campaignRepo.save(c);
            response.setPortalId(result.getPortal_id());
            response.setStatus(HttpStatus.OK);
            response.setMessageSeverity(Constant.SUCCESS);
            response.setRoleType(jwtExtract.getUserRole());
        } catch (Exception e) {
            log.error("Error in updateCampaign : CampaignServiceImpl  : " + e.getMessage());
            response.setMessage("error occurred: " + e.getMessage());
            throw e;
        }
        return response;
    }

    @Override
    public Campaign getCampaignById(int portalId) {

        Optional<Campaign> optionalCampaign = campaignRepo.findById(portalId);
        return optionalCampaign
                .orElseThrow(() -> new CampaignNotFoundException("Couldn't find a Campaign with id: " + portalId));

    }

    @Override
    public AvailLMKResponse getAvailableCherrypicks(AvailLMKRequest availLMKRequest) {
        log.info("Inside CampaignServiceImpl: getAvailableCherrypicks");

        final String url = "https://dmsbookingportaluat.multichoice.co.za/LMKServices/avail";
        // final String url = "http://10.75.23.39:9090/LMKServices/avail";

        try {
            log.info("request to lmk: " + new ObjectMapper().writeValueAsString(availLMKRequest));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<AvailLMKRequest> requestBody = new HttpEntity<AvailLMKRequest>(availLMKRequest, headers);
        AvailLMKResponse availLMKResponse = restTemplate.postForObject(url, requestBody, AvailLMKResponse.class);

        try {
            log.info("response from lmk: " + new ObjectMapper().writeValueAsString(availLMKResponse));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return availLMKResponse;
    }
    @Override
    public CampaignApproveRejectResp campaignApproval(CampaignApproveReject campaignApproval) {
        /**LMK api to fetch campaign status.
         *Update PTP.
         *Validate and approve.
         *If few are approved and few are not in LMK,
         *ptp should not approve and give a validation message
         *"Cannot approve or reject. Contractor should approve or reject all campaign".
         **/
        log.info("Inside CampaignServiceImpl: campaignApproval");
        String message = "";
        CampaignApproveRejectResp resp = new CampaignApproveRejectResp();
        Map<Integer, String> appKeyStatusMap;
        List<Integer> lst;
        Campaign campaign = campaignRepo.findById(campaignApproval.getPortalId()).orElseThrow(() -> new CampaignNotFoundException("Couldn't find a Campaign with id: " + campaignApproval.getPortalId()));
        log.info("portalId: " + campaignApproval.getPortalId());
        try {
            if (campaign.getItems().stream().anyMatch(item -> item.getCampaign_code() == null) || campaign.getItems().size() == 0) {
                resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                resp.setMessage(Constant.APP_REJ_FAIL_MSG);
            } else {
                lst = new ArrayList<>();
                CampaignApprovelmkReq campaignApprovelmkReq = new CampaignApprovelmkReq();
                for (Items item : campaign.getItems()) {
                    lst.add(item.getCampaign_code());
                }
                campaignApprovelmkReq.setCampaignIds(lst);
                final String url = "https://dmsbookingportaluat.multichoice.co.za/Ingres/status";
                HttpHeaders headers = new HttpHeaders();
                headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<CampaignApprovelmkReq> requestBody = new HttpEntity<>(campaignApprovelmkReq, headers);
                CampaignApprovelmkRespList responselist = restTemplate.postForObject(url, requestBody, CampaignApprovelmkRespList.class);
                if (responselist != null) {
                    appKeyStatusMap = new HashMap<>();
                    for (CampaignApprovelmkResp campaignApprovelmkResp : responselist.getCampaignStatusList()) {
                        appKeyStatusMap.put(campaignApprovelmkResp.getCampaignId(), campaignApprovelmkResp.getCampaignStatus());
                    }
                    for (Items item : campaign.getItems()) {
                        item.setStatus(appKeyStatusMap.get(item.getCampaign_code()));
                    }
                    campaignRepo.save(campaign);

                    if (!appKeyStatusMap.containsValue("Approved")) {
                        resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                        resp.setMessage(Constant.APP_REJ_FAIL_MSG);
                    } else {
                        campaign.setStatus(3);
                        campaignRepo.save(campaign);
                        emailUtil.sendMailCampaign(campaignApproval.getUserName(), "Y", null);
                        resp.setStatus(HttpStatus.OK);
                        resp.setMessage(Constant.CAMPAIGN_APPROVAL_ACCEPT);
                    }
                } else {
                    resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                    resp.setMessage(Constant.REQ_FAIL_MSG);
                }
            }

        } catch (Exception e) {
            e.getMessage();
        }

        return resp;
    }

    @Override
    public CampaignApproveRejectResp  campaignReject(CampaignApproveReject campaignReject) {
        log.info("Inside CampaignServiceImpl: campaignReject");
        Boolean erroFlag = false;
        CampaignApproveRejectResp resp = new CampaignApproveRejectResp();
        Campaign campaign = campaignRepo.findById(campaignReject.getPortalId()).orElseThrow(() -> new CampaignNotFoundException("Couldn't find a Campaign with id: " + campaignReject.getPortalId()));
        log.info("portalId: " + campaignReject.getPortalId());
        try {
            if (campaign.getItems().stream().allMatch(item -> item.getCampaign_code() == null) || campaign.getItems().size() == 0) {
                campaign.setStatus(5);
                campaignRepo.save(campaign);
                resp.setStatus(HttpStatus.OK);
                resp.setMessage(Constant.REJ_SUCCESS_MSG +campaignReject.getReason());
            } else {
                List<CampaignCancelReq> list = new ArrayList<>();
                for (Items item : campaign.getItems()) {
                    CampaignCancelReq cp = new CampaignCancelReq();
                    cp.setAppKeyId(item.getApprovalKeyID());
                    cp.setCampCode(item.getCampaign_code());
                    list.add(cp);
                }
                final String url = "https://dmsbookingportaluat.multichoice.co.za/CariaServices/campaign/cancel";
                log.info("request to lmk: " + new ObjectMapper().writeValueAsString(list));
                HttpHeaders headers = new HttpHeaders();
                headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
                headers.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<List<CampaignCancelReq>> requestBody = new HttpEntity<>(list, headers);
                CampaignCancelResp response = restTemplate.postForObject(url, requestBody, CampaignCancelResp.class);
                if (response != null) {

                    for (UploadCampaignResult uploadCampaignResult : response.getUploadCampaignResult()) {
                        if (!uploadCampaignResult.getCampaignStatus().equalsIgnoreCase("Cancelled")) {
                            resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                            resp.setMessage(Constant.REJ_FAILED_MSG);
                            log.error(Constant.REJ_FAILED_MSG +"for campaign code "+ uploadCampaignResult.getCampaignCode());
                            erroFlag = true;
                            break;
                        }

                    }
                    if (erroFlag == false) {
                        campaign.setStatus(5);
                        campaignRepo.save(campaign);
                        emailUtil.sendMailCampaign(campaignReject.getUserName(), "N", campaignReject.getReason());
                        resp.setStatus(HttpStatus.OK);
                        resp.setMessage(Constant.REJ_SUCCESS_MSG + campaignReject.getReason());
                    }
                } else {
                    resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
                    resp.setMessage(Constant.REJ_FAILED_MSG);
                }
            }

        } catch (Exception e) {
            e.getMessage();
        }
        return resp;
    }
    
    public TargetMarketLMKResp getTargetMarketDetailsPackage(TargetMarketPkgReq multipleTargetMarketReq)
			throws InvalidParamException {
		log.info("Inside CampaignServiceImpl: getTargetMarketDetailsPackage");
		TargetMarketLMKResp response = null;

		if (multipleTargetMarketReq.getTargetMarkets().length == 0) {
			throw new InvalidParamException("target market list cannot be null or empty");
		} else {

			try {
				final String url = "https://dmsbookingportaluat.multichoice.co.za/Ingres/audience/forecast/package";
				// final String url =
				// "http://10.75.23.39:9090/Ingres/audience/forecast/package";

				try {
					log.info("request to lmk: " + new ObjectMapper().writeValueAsString(multipleTargetMarketReq));
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				HttpHeaders headers = new HttpHeaders();
				headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
				headers.setContentType(MediaType.APPLICATION_JSON);
				HttpEntity<TargetMarketPkgReq> requestBody = new HttpEntity<>(multipleTargetMarketReq, headers);
				response = restTemplate.postForObject(url, requestBody, TargetMarketLMKResp.class);
				response.setMessage("success");

				if (response != null) {
					Items items = new Items();
					for (PackageForecast pkg : response.getItems()) {
						items.setCatalog_id(pkg.getCatalogId());
						items.setStart(pkg.getStartDate());
						items.setEnd(pkg.getEndDate());
						itemsRepository.save(items);

						if (pkg.getDemos() != null) {
							for (PackageForecastLMK pkgLMK : pkg.getDemos()) {
								PackageAudienceForecast pkgAudience = new PackageAudienceForecast();
								pkgAudience.setCpp(pkgLMK.getCpp());
								pkgAudience.setCpt(pkgLMK.getCpt());
								pkgAudience.setTvr(pkgLMK.getRatings());
								pkgAudience.setViews(pkgLMK.getViews());
								pkgAudience.setDemo_id(pkgLMK.getDemo());

								pkgAudience.setItems(items);

								packageAudienceRepo.save(pkgAudience);
							}
						}
					}
				}
			} catch (Exception e) {
				response.setMessage(e.getMessage());
			}

		}
		return response;
	}
    
    @Override
	public ResponseEntity<byte[]> getOrderConfirmation(Integer campaignCode, String type) {
		ResponseEntity<byte[]> response = null;
		log.info("Inside CampaignServiceImpl: getAvailableCherrypicks");

		final String url = "https://dmsbookingportaluat.multichoice.co.za/Report/order?campCode=" + campaignCode
				+ "&type=" + type;
		log.info("LMK URL-->" + url);
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));
			HttpEntity<String> entity = new HttpEntity<>(headers);
			response = restTemplate.exchange(url, HttpMethod.GET, entity, byte[].class);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return response;
	}
}
