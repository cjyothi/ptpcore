package com.dms.ptp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.persistence.PersistenceException;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.dms.ptp.config.AWSDynamoConfig;
import com.dms.ptp.controller.AffinityReachCalculator;
import com.dms.ptp.controller.CampaignController;
import com.dms.ptp.controller.CatalogController;
import com.dms.ptp.controller.ChannelDemographicController;
import com.dms.ptp.controller.DocumentController;
import com.dms.ptp.controller.PrincingController;
import com.dms.ptp.controller.UserController;
import com.dms.ptp.dto.AffCreateRequest;
import com.dms.ptp.dto.AffUpdateRequest;
import com.dms.ptp.dto.AffinityChannel;
import com.dms.ptp.dto.AffinityDemo;
import com.dms.ptp.dto.AffinityRequest;
import com.dms.ptp.dto.AffinityResponse;
import com.dms.ptp.dto.AvailLMKRequest;
import com.dms.ptp.dto.CampaignApproveReject;
import com.dms.ptp.dto.CampaignList;
import com.dms.ptp.dto.CampaignRequest;
import com.dms.ptp.dto.CatalogRequest;
import com.dms.ptp.dto.DaypartData;
import com.dms.ptp.dto.DemoAverage;
import com.dms.ptp.dto.JWTExtract;
import com.dms.ptp.dto.LmkAudienceResponse;
import com.dms.ptp.dto.PageDecorator;
import com.dms.ptp.dto.RateCardInput;
import com.dms.ptp.dto.RateDayPart;
import com.dms.ptp.dto.Rates;
import com.dms.ptp.dto.ResultMessageList;
import com.dms.ptp.dto.ResultMessages;
import com.dms.ptp.dto.SalesAreaLMK;
import com.dms.ptp.dto.SellOut;
import com.dms.ptp.dto.SellOutDaypart;
import com.dms.ptp.dto.SignUpRequestModel;
import com.dms.ptp.dto.TargetMarketValues;
import com.dms.ptp.dto.UniverseData;
import com.dms.ptp.dto.UpdateCampaignRequest;
import com.dms.ptp.dto.UserList;
import com.dms.ptp.dto.UserLoginRequest;
import com.dms.ptp.entity.Aff;
import com.dms.ptp.entity.AffChannelDayparts;
import com.dms.ptp.entity.AffChannelDemos;
import com.dms.ptp.entity.AffChannels;
import com.dms.ptp.entity.AffDemos;
import com.dms.ptp.entity.AffUniverse;
import com.dms.ptp.entity.Baseline;
import com.dms.ptp.entity.Campaign;
import com.dms.ptp.entity.CampaignDaypart;
import com.dms.ptp.entity.CampaignSalesArea;
import com.dms.ptp.entity.Catalog;
import com.dms.ptp.entity.CatalogDaypart;
import com.dms.ptp.entity.Channel;
import com.dms.ptp.entity.Demo;
import com.dms.ptp.entity.DocDetail;
import com.dms.ptp.entity.Items;
import com.dms.ptp.entity.LengthFactor;
import com.dms.ptp.entity.Plans;
import com.dms.ptp.entity.RateCards;
import com.dms.ptp.entity.RateInfo;
import com.dms.ptp.entity.SalesArea;
import com.dms.ptp.entity.SellOutInfo;
import com.dms.ptp.entity.Spots;
import com.dms.ptp.entity.User;
import com.dms.ptp.exception.BaselineNotFoundException;
import com.dms.ptp.exception.CampaignNotFoundException;
import com.dms.ptp.exception.InvalidLoginCredentialsException;
import com.dms.ptp.exception.InvalidParamException;
import com.dms.ptp.exception.UserNotFoundException;
import com.dms.ptp.repository.ApprovalKeyRepository;
import com.dms.ptp.repository.BaselineRepository;
import com.dms.ptp.repository.CampaignRepository;
import com.dms.ptp.repository.ChannelDemoRepository;
import com.dms.ptp.repository.ChannelRepository;
import com.dms.ptp.repository.DemoRepository;
import com.dms.ptp.repository.DocRepository;
import com.dms.ptp.repository.ItemsRepository;
import com.dms.ptp.repository.LengthFactorRepository;
import com.dms.ptp.repository.RateCardRepository;
import com.dms.ptp.repository.RateRepository;
import com.dms.ptp.repository.SeedRepository;
import com.dms.ptp.repository.SelloutRepository;
import com.dms.ptp.repository.UserRepository;
import com.dms.ptp.response.AvailItemLMKResponse;
import com.dms.ptp.response.AvailLMKResponse;
import com.dms.ptp.response.BaselineListResponse;
import com.dms.ptp.response.BaselineResponse;
import com.dms.ptp.response.CampaignApproveRejectResp;
import com.dms.ptp.response.CampaignCancelResp;
import com.dms.ptp.response.CampaignResponse;
import com.dms.ptp.response.CampaignResponseList;
import com.dms.ptp.response.CatalogResponse;
import com.dms.ptp.response.ChannelDemographicResponse;
import com.dms.ptp.response.ChannelResponse;
import com.dms.ptp.response.DemoInput;
import com.dms.ptp.response.DocResponse;
import com.dms.ptp.response.FinalChannelResponse;
import com.dms.ptp.response.LengthFactorResponse;
import com.dms.ptp.response.PriceingRatesResponse;
import com.dms.ptp.response.PriceingSelloutResponse;
import com.dms.ptp.response.PricingDataResponse;
import com.dms.ptp.response.SalesAreaLMKResponse;
import com.dms.ptp.response.SaveResponse;
import com.dms.ptp.response.UpdateCampaignResponse;
import com.dms.ptp.response.UploadCampaignResult;
import com.dms.ptp.response.UserLoginResponse;
import com.dms.ptp.service.AffinityReachService;
import com.dms.ptp.service.CampaignService;
import com.dms.ptp.service.CatalogService;
import com.dms.ptp.service.ChannelDemographicService;
import com.dms.ptp.service.DocService;
import com.dms.ptp.service.PricingService;
import com.dms.ptp.service.UserService;
import com.dms.ptp.serviceimplementation.AffinityReachServiceImplementation;
import com.dms.ptp.serviceimplementation.CampaignServiceImpl;
import com.dms.ptp.serviceimplementation.DocServiceImpl;
import com.dms.ptp.serviceimplementation.PricingServiceImplementation;
import com.dms.ptp.serviceimplementation.UserServiceImpl;
import com.dms.ptp.util.Constant;
import com.dms.ptp.util.EmailUtil;
import com.dms.ptp.util.JWTUtil;

import freemarker.template.TemplateException;
import io.github.perplexhub.rsql.RSQLJPASupport;
import reactor.core.publisher.Flux;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = PTPCoreServiceApplication.class)
public class PTPCoreServiceApplicationTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Mock
    PricingService mockPricingService;

    @InjectMocks
    PrincingController princingController;

    @InjectMocks
    PricingServiceImplementation pricingService;

    @Mock
    RateRepository mockRateRepo;

    @Mock
    SelloutRepository mockSelloutRepo;

    @Mock
    RateCardRepository mockRateCardRepository;

    @Mock
    LengthFactorRepository mockLengthFactorRepository;

    @Mock
    RestTemplate mockRestTemplate;

    @InjectMocks
    AffinityReachCalculator affinityReachCalculator;

    @Mock
    AffinityReachService mockAffinityReachService;

    @InjectMocks
    AffinityReachServiceImplementation affinityReachService;

    @Mock
    ChannelDemoRepository mockChannelDemoRepository;

    @Mock
    DemoRepository mockDemoRepository;
    
    @Mock
    BaselineRepository mockBaselineRepository;
    
    @InjectMocks
    CatalogController catalogController;

    @Mock
    CatalogService catalogService;
    
    @InjectMocks
    ChannelDemographicController channelDemographicController;
    
    @Mock
    ChannelRepository mockChannelRepository;
    
    @Mock
    ChannelDemographicService channelDemographicService;
    
    @Mock
    UserService userService;

    @Mock
    UserServiceImpl userServiceImplMock;
    
    @Mock
    UserRepository repoMock;
    
    @Mock
    AWSDynamoConfig dynamoMock;

    @InjectMocks
    UserController userController;  
    
    @InjectMocks
    CampaignController campaignController;

    @Mock
    CampaignService mockCampaignService;

    @InjectMocks
    CampaignServiceImpl campaignService;

    @InjectMocks
    DocumentController documentController;

    @Mock
    DocService mockDocService;

    @InjectMocks
    DocServiceImpl docService;

    @Mock
    AmazonS3 mockAmazonS3;

    @Mock
    DocRepository mockDocRepository;


    @Mock
    SeedRepository mockSeedRepo;

    @Mock
    CampaignRepository mockCampaignRepo;

    @Mock
    ItemsRepository mockItemsRepository;

    @Mock
    ApprovalKeyRepository mockApprovalKeyRepo;

    @Mock
    JWTUtil mockJWTUtil;

    @Mock
    EmailUtil mockEmailUtil;

    private String channelName = "BBC World";
    private String dayPart = "ALL DAY";
    private String weekPart = "ALLDAY";
    private String startDate = "2019-01-01";
    private String endDate = "2019-01-02";
    private String status = "Approved";
    private String demourl = "/demos";
    private String agencyListurl = "/users/agencyList";
    private String getAllUsersurl = "/users/user";
    String token ="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOsuffixiIxNzAiLCJpZCI6MTcwLCJyb2xlIjoiTWVkaWEgUGxhbm5lciIsImlhdCI6MTU5NDEwNDcxOSwiZXhwIjoxNTk0NzA5NTE5fQ.E8f_R42uAGwwgCOge5HmDEIpSweKF9E4b4Hw_mDwX-RKO_tGyYcSk-MtrsbRZj1m4CZrj8rQBFr_I8LAtuV0Ig";
    String awsFileName = "test contract.pdf";
    String folderName = "1593890897343-1";
    String bucketName = "ptp.test";
    String suffix = "/";
    String filePath = folderName+suffix+awsFileName;
    String advertiserCode = "A0001";
    String adminUsername = "arnabashutosh.d@tataelxsi.co.in";
    String campaignStatus = "status";
    String streamName = "<<pdf data>>";
    String type = "Telmar";
    String delResponse = "unable to delete ";
    String campaignName = "CampaignName";
    String campaignObjective = "Campaign Objective";
    String campaignStart = "2020-01-01";
    String campaignEnd = "2020-12-01";
    
    @Before
    public void setup(){
        ReflectionTestUtils.setField(docService,"bucketName",bucketName);
    }

    
    // ************************************************* Pricing test cases ****************************************************************
    
    
    @Test
    public void getAllOfferingSuccessResponse() {
        String offeringurl = "/pricing/offering";
        ResponseEntity<PricingDataResponse> responseEntity = restTemplate.exchange(offeringurl, HttpMethod.GET, null,
                PricingDataResponse.class);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getAllOfferingErrorResponse() {
        when(mockPricingService.getOffering()).thenThrow(PersistenceException.class);
        ResponseEntity<PricingDataResponse> responseEntity = princingController.getAllOfferings();
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void getAllPlatformSuccessResponse() {
        String platformurl = "/pricing/platform";
        ResponseEntity<PricingDataResponse> responseEntity = restTemplate.exchange(platformurl, HttpMethod.GET, null,
                PricingDataResponse.class);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getAllPlatformErrorResponse() {
        when(mockPricingService.getPlatform()).thenThrow(PersistenceException.class);
        ResponseEntity<PricingDataResponse> responseEntity = princingController.getAllPlatform();
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void getAllTerritorySuccessResponse() {
        String territoryurl = "/pricing/territory";
        ResponseEntity<PricingDataResponse> responseEntity = restTemplate.exchange(territoryurl, HttpMethod.GET, null,
                PricingDataResponse.class);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getAllTerritoryErrorResponse() {
        when(mockPricingService.getTerritory()).thenThrow(PersistenceException.class);
        ResponseEntity<PricingDataResponse> responseEntity = princingController.getAllTerritory();
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void getAllDayPartSuccessResponse() {
        String dayparturl = "/pricing/daypart";
        ResponseEntity<PricingDataResponse> responseEntity = restTemplate.exchange(dayparturl, HttpMethod.GET, null,
                PricingDataResponse.class);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getAllDayPartErrorResponse() {
        when(mockPricingService.getDayPart()).thenThrow(PersistenceException.class);
        ResponseEntity<PricingDataResponse> responseEntity = princingController.getAllDayPart();
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void getAllPanelSuccessResponse() {
        String panelurl = "/pricing/panel";
        ResponseEntity<PricingDataResponse> responseEntity = restTemplate.exchange(panelurl, HttpMethod.GET, null,
                PricingDataResponse.class);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getAllPanelErrorResponse() {
        when(mockPricingService.getPanel()).thenThrow(PersistenceException.class);
        ResponseEntity<PricingDataResponse> responseEntity = princingController.getAllPanel();
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test(expected = InvalidParamException.class)
    public void getRatesException() throws InvalidParamException, ParseException {
        princingController.getRates(endDate, startDate);

    }

    @Test
    public void getRatesSuccess() throws InvalidParamException, ParseException {
        when(mockPricingService.getRateList(startDate, endDate)).thenReturn(createPricingRatesResponse());
        ResponseEntity<PriceingRatesResponse> priceingRatesResponseResponseEntity = princingController
                .getRates(startDate, endDate);
        assertNotNull(priceingRatesResponseResponseEntity);
        assertEquals(priceingRatesResponseResponseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void getSellOutsSuccess() {
        when(mockPricingService.getSelloutList(startDate, endDate)).thenReturn(createPricingSelloutResponse());
        ResponseEntity<PriceingSelloutResponse> priceingSelloutResponseResponseEntity = princingController
                .getSellout(startDate, endDate);
        assertNotNull(priceingSelloutResponseResponseEntity);
        assertEquals(priceingSelloutResponseResponseEntity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void getRatesList() {
        when(mockRateRepo.getRatesInfo(startDate, endDate)).thenReturn(createRateInfoList());
        PriceingRatesResponse priceingRatesResponse = pricingService.getRateList(startDate, endDate);
        assertNotNull(priceingRatesResponse);
        assertEquals(priceingRatesResponse.getTotal(), createPricingRatesResponse().getTotal());
    }

    @Test
    public void getSelloutList() {
        when(mockSelloutRepo.getRatesInfo(startDate, endDate)).thenReturn(createSelloutInfoList());
        PriceingSelloutResponse priceingSelloutResponse = pricingService.getSelloutList(startDate, endDate);
        assertNotNull(priceingSelloutResponse);
        assertEquals(priceingSelloutResponse.getTotal(), createPricingSelloutResponse().getTotal());
    }


    @Test
    public void addRateCardServiceTest(){
        RateCardInput rateCardInput = createRateCardInput();
        RateCards mockRateCard = createRateCards();
        when(mockRateCardRepository.save(any(RateCards.class))).thenReturn(mockRateCard);
        int id = pricingService.addRateCard(rateCardInput);
        assertEquals(1,id);
    }

    @Test
    public void getAllRateCardsSuccessTest(){
        RateCards rateCards = createRateCards();
        List<RateCards> rateCardsList = new ArrayList<>();
        rateCardsList.add(rateCards);
        when(mockPricingService.findAllRateCard()).thenReturn(rateCardsList);
        ResponseEntity<List<RateCards>> rateCardsListResponse = princingController.findAllRateCards();
        assertNotNull(rateCardsListResponse);
        assertEquals(HttpStatus.OK,rateCardsListResponse.getStatusCode());

    }

    @Test
    public void getAllRateCardsErrorTest(){
        when(mockPricingService.findAllRateCard()).thenThrow(PersistenceException.class);
        ResponseEntity<List<RateCards>> rateCardsListResponse = princingController.findAllRateCards();
        assertNotNull(rateCardsListResponse);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,rateCardsListResponse.getStatusCode());

    }

    @Test
    public void getAllRateCardsServiceTest(){
        RateCards rateCards = createRateCards();
        List<RateCards> rateCardsList = new ArrayList<>();
        rateCardsList.add(rateCards);
        when(mockRateCardRepository.findAll()).thenReturn(rateCardsList);
        List<RateCards> rateCardsListResponse = pricingService.findAllRateCard();
        assertNotNull(rateCardsListResponse);
        assertEquals(1,rateCardsListResponse.size());

    }


    @Test
    public void getRateCardsByStatusSuccessTest(){
        RateCards rateCards = createRateCards();
        List<RateCards> rateCardsList = new ArrayList<>();
        rateCardsList.add(rateCards);
        when(mockPricingService.findRateCardByStatus(status)).thenReturn(rateCardsList);
        ResponseEntity<List<RateCards>> rateCardsListResponse = princingController.findRateCardByStatus(status);
        assertNotNull(rateCardsListResponse);
        assertEquals(HttpStatus.OK,rateCardsListResponse.getStatusCode());

    }

    @Test
    public void getRateCardsByStatusErrorTest(){
        when(mockPricingService.findRateCardByStatus(status)).thenThrow(PersistenceException.class);
        ResponseEntity<List<RateCards>> rateCardsListResponse = princingController.findRateCardByStatus(status);
        assertNotNull(rateCardsListResponse);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,rateCardsListResponse.getStatusCode());

    }

    @Test
    public void getRateCardsByStatusServiceTest(){
        RateCards rateCards = createRateCards();
        List<RateCards> rateCardsList = new ArrayList<>();
        rateCardsList.add(rateCards);
        when(mockRateCardRepository.findByStatus(status)).thenReturn(rateCardsList);
        List<RateCards> rateCardsListResponse = pricingService.findRateCardByStatus(status);
        assertNotNull(rateCardsListResponse);
        assertEquals(1,rateCardsListResponse.size());

    }

    @Test
    public void getLengthFactorServiceTest(){
        Pageable pageable = PageRequest.of(0, 20,Sort.unsorted());
        Specification specification = RSQLJPASupport.toSpecification("spotLength==180");
        List<LengthFactor> lengthFactorList = new ArrayList<>();
        LengthFactor lengthFactor = createLengthFactor();
        lengthFactorList.add(lengthFactor);
        Page<LengthFactor> page = new PageImpl<>(lengthFactorList,pageable,1L);
        when(mockLengthFactorRepository.findAll(specification,pageable)).thenReturn(page);
        Page<LengthFactorResponse> lengthFactorResponses = pricingService.getLengthFactor(specification,pageable);
        assertNotNull(lengthFactorResponses);
        assertEquals(1,lengthFactorResponses.getTotalElements());
    }

    @Test
    public void getAffinityCalculatorErrorTest(){
        AffinityRequest affinityRequest = createAffinityRequest();
        when(mockAffinityReachService.getAffinityReachCalculation(affinityRequest)).thenThrow(PersistenceException.class);
        ResponseEntity<AffinityResponse> affinityReachCalculatorResponse = affinityReachCalculator.getAffinity(affinityRequest);
        assertNotNull(affinityReachCalculatorResponse);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,affinityReachCalculatorResponse.getStatusCode());

    }

    @Test
    public void getAffinityCalculatorSuccessTest(){
        AffinityRequest affinityRequest = createAffinityRequest();
        AffinityResponse affinityResponse = createAffinityResponse();
        when(mockAffinityReachService.getAffinityReachCalculation(affinityRequest)).thenReturn(affinityResponse);
        ResponseEntity<AffinityResponse> affinityReachCalculatorResponse = affinityReachCalculator.getAffinity(affinityRequest);
        assertNotNull(affinityReachCalculatorResponse);
        assertEquals(HttpStatus.OK,affinityReachCalculatorResponse.getStatusCode());

    }

    @Test
    public void getAffinityCalculationServiceTest(){
        AffinityRequest affinityRequest = createAffinityRequest();
        LmkAudienceResponse[] mockLmkAudienceResponses = createLmkAudienceResponseList();
        UniverseData[] mockUniverseData = createUniverseDataList().toArray(new UniverseData[0]);
        List<Demo> mockDemoList =createDemoList();
        List<Channel> channelList = createMockChannelList();
        when(mockChannelDemoRepository.findByTerritory_IdAndPlatform_Id(1,1)).thenReturn(channelList);
        when(mockRestTemplate.postForObject(Mockito.any(String.class), Mockito.any(HttpEntity.class),
                Mockito.eq(LmkAudienceResponse[].class))).thenReturn(mockLmkAudienceResponses);
        when(mockRestTemplate.getForObject(Mockito.any(String.class),Mockito.eq(UniverseData[].class))).thenReturn(mockUniverseData);
        when(mockDemoRepository.findByIdIn(Arrays.asList(14,15,16))).thenReturn(mockDemoList);
        AffinityResponse affinityResponse = affinityReachService.getAffinityReachCalculation(affinityRequest);
        assertNotNull(affinityResponse);
    }

    @Test(expected=Exception.class)
    public void getAffinityCalculationLmkAudienceExceptionTest(){
        AffinityRequest affinityRequest = createAffinityRequest();
        List<Demo> mockDemoList =createDemoList();
        List<Channel> channelList = createMockChannelList();
        when(mockChannelDemoRepository.findByTerritory_IdAndPlatform_Id(1,1)).thenReturn(channelList);
        when(mockRestTemplate.postForObject(Mockito.any(String.class), Mockito.any(HttpEntity.class),
                Mockito.eq(LmkAudienceResponse[].class))).thenThrow(RestClientException.class);
        when(mockRestTemplate.getForObject(Mockito.any(String.class),Mockito.eq(UniverseData[].class))).thenThrow(RestClientException.class);
        when(mockDemoRepository.findByIdIn(Arrays.asList(14,15,16))).thenReturn(mockDemoList);
        AffinityResponse affinityResponse = affinityReachService.getAffinityReachCalculation(affinityRequest);
        assertNotNull(affinityResponse);
    }

    @Test
    public void saveBaselineTest(){
        AffCreateRequest affCreateRequest = createAffCreateRequest();
        when(mockAffinityReachService.saveAffinity(affCreateRequest)).thenReturn(new SaveResponse(1,"Created Succesfully"));
        ResponseEntity saveBaselineResponse = affinityReachCalculator.saveAffinity(affCreateRequest);
        assertNotNull(saveBaselineResponse);
        assertEquals(HttpStatus.OK,saveBaselineResponse.getStatusCode());
    }

    @Test
    public void saveBaselineErrorTest(){
        AffCreateRequest affCreateRequest = createAffCreateRequest();
        when(mockAffinityReachService.saveAffinity(affCreateRequest)).thenThrow(PersistenceException.class);
        ResponseEntity saveBaselineResponse = affinityReachCalculator.saveAffinity(affCreateRequest);
        assertNotNull(saveBaselineResponse);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,saveBaselineResponse.getStatusCode());
    }

    @Test
    public void editBaselineTest()  {
        AffUpdateRequest affUpdateRequest = createAffUpdateRequest();
        when(mockAffinityReachService.updateAffinity(affUpdateRequest)).thenReturn(new SaveResponse(affUpdateRequest.getId(),"Updated Successfully"));
        ResponseEntity updateResponse = affinityReachCalculator.updateAffinity(affUpdateRequest);
        assertNotNull(updateResponse);
        assertEquals(HttpStatus.OK,updateResponse.getStatusCode());
    }

    @Test
    public void editBaselineErrorTest() throws InvalidParamException {
        AffUpdateRequest affUpdateRequest = createAffUpdateRequest();
        when(mockAffinityReachService.updateAffinity(affUpdateRequest)).thenThrow(PersistenceException.class);
        ResponseEntity updateResponse = affinityReachCalculator.updateAffinity(affUpdateRequest);
        assertNotNull(updateResponse);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,updateResponse.getStatusCode());
    }
    @Test
    public void getBaselineListTest(){
        List<BaselineListResponse> mockBaselineListResponseList= createBaselineResponseList();
        when(mockAffinityReachService.getBaselineList()).thenReturn(mockBaselineListResponseList);
        ResponseEntity<List<BaselineListResponse>> baselineResponse = affinityReachCalculator.getBaselineList();
        assertNotNull(baselineResponse);
        assertEquals(HttpStatus.OK,baselineResponse.getStatusCode());
    }

    @Test
    public void getBaselineListExceptionTest(){
        when(mockAffinityReachService.getBaselineList()).thenThrow(PersistenceException.class);
        ResponseEntity<List<BaselineListResponse>> baselineResponse = affinityReachCalculator.getBaselineList();
        assertNotNull(baselineResponse);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,baselineResponse.getStatusCode());
    }

    @Test
    public void getBaselineListServiceTest(){
        List<Baseline> baselineList = Collections.singletonList(createBaseline());
        when(mockBaselineRepository.findAll()).thenReturn(baselineList);
        List<BaselineListResponse> baselineListResponseList= affinityReachService.getBaselineList();
        assertNotNull(baselineListResponseList);
        assertEquals(1, baselineListResponseList.size());

    }

    @Test
    public void getBaselineTest(){
        BaselineResponse baselineData = createBaselineData();
        when(mockAffinityReachService.getBaselineById(1)).thenReturn(baselineData);
        BaselineResponse baselineDataResponseEntity = affinityReachCalculator.getBaseline(1);
        assertNotNull(baselineDataResponseEntity);
        assertEquals(baselineData.getId(),baselineDataResponseEntity.getId());
    }

    @Test(expected=BaselineNotFoundException.class)
    public void getBaselineExceptionTest(){
        when(mockAffinityReachService.getBaselineById(1)).thenThrow(BaselineNotFoundException.class);
        affinityReachCalculator.getBaseline(1);
    }

    @Test
    public void getBaselineByIdApprovedSuccessTest(){
        Baseline baseline = createBaseline();
        Channel mockChannel = createMockChannelList().get(0);
        when(mockBaselineRepository.findById(1)).thenReturn(java.util.Optional.of(baseline));
        when(mockChannelDemoRepository.getOne(7)).thenReturn(mockChannel);
        BaselineResponse baselineData = affinityReachService.getBaselineById(1);
        assertNotNull(baselineData);
        assertEquals(baseline.getId(),baselineData.getId());
    }

    @Test
    public void getBaselineByIdDraftSuccessTest(){
        Baseline baseline = createBaseline();
        baseline.setStatus("D");
        baseline.getAff().setWeekpart("WD");
        Channel mockChannel = createMockChannelList().get(0);
        when(mockBaselineRepository.findById(1)).thenReturn(java.util.Optional.of(baseline));
        when(mockChannelDemoRepository.getOne(7)).thenReturn(mockChannel);
        BaselineResponse baselineData = affinityReachService.getBaselineById(1);
        assertNotNull(baselineData);
        assertEquals(baseline.getId(),baselineData.getId());
    }

    @Test
    public void getBaselineByIdAlldaySuccessTest(){
        Baseline baseline = createBaseline();
        baseline.getAff().setWeekpart("AD");
        Channel mockChannel = createMockChannelList().get(0);
        when(mockBaselineRepository.findById(1)).thenReturn(java.util.Optional.of(baseline));
        when(mockChannelDemoRepository.getOne(7)).thenReturn(mockChannel);
        BaselineResponse baselineData = affinityReachService.getBaselineById(1);
        assertNotNull(baselineData);
        assertEquals(baseline.getId(),baselineData.getId());
    }

    @Test(expected=BaselineNotFoundException.class)
    public void getBaselineByIdErrorTest(){
        when(mockBaselineRepository.findById(1)).thenReturn(Optional.empty());
        affinityReachService.getBaselineById(1);
    }

    @Test
    public void deleteBaselineTest(){
        when(mockAffinityReachService.deleteBaseline(1)).thenReturn(new SaveResponse(1,"Deleted Successfully"));
        SaveResponse deleteResponseEntity = affinityReachCalculator.delete(1);
        assertNotNull(deleteResponseEntity);
        assertEquals(1,deleteResponseEntity.getId());
    }


    @Test
    public void deleteBaselineServiceSuccessTest(){
        Baseline baseline = createBaseline();
        when(mockBaselineRepository.findById(1)).thenReturn(java.util.Optional.of(baseline));
        doNothing().when(mockBaselineRepository).delete(baseline);
        SaveResponse deleteMessage = affinityReachService.deleteBaseline(1);
        assertEquals(1,deleteMessage.getId());
    }

    @Test(expected=BaselineNotFoundException.class)
    public void deleteBaselineErrorTest(){
        when(mockBaselineRepository.findById(1)).thenReturn(Optional.empty());
        affinityReachService.deleteBaseline(1);
    }
    
    
    // ******************************************************* Catalog test cases *********************************************************
    
    
    @Test
    public void getCatalogByIdTest() {
        CatalogResponse mockResp = catalogMockResp();
        Mockito.when(catalogService.getCatalogById(anyInt())).thenReturn(mockResp);
        ResponseEntity<CatalogResponse> actualRespEntity = catalogController.getCatalogById(2);
        CatalogResponse actualResp = actualRespEntity.getBody();
        assertNotNull(actualResp);
        assertEquals(mockResp, actualResp);
    }

    @Test
    public void createCatalogTest() {
        CatalogResponse mockResp = catalogMockResp();
        Mockito.when(catalogService.createCatalog(any(CatalogRequest.class))).thenReturn(mockResp);
        ModelMapper modelMapper = new ModelMapper();
        CatalogRequest request = modelMapper.map(mockResp.getItems(), CatalogRequest.class);
        ResponseEntity<CatalogResponse> actualRespEntity = catalogController.createCatalog(request);
        CatalogResponse actualResp = actualRespEntity.getBody();
        assertNotNull(actualResp);
        assertEquals(mockResp, actualResp);
    }
    @Test
    public void updateCatalogTest()
    {
        CatalogResponse mockResp = catalogMockResp();
        mockResp.getItems().get(0).setTitle1("UpdatedZiyachisa");
        mockResp.getItems().get(0).setId(5);
        Mockito.when(catalogService.updateCatalog(any(CatalogRequest.class))).thenReturn(mockResp);
        ModelMapper modelMapper = new ModelMapper();
        CatalogRequest request = modelMapper.map(mockResp.getItems().get(0), CatalogRequest.class);
        ResponseEntity<CatalogResponse> actualRespEntity = catalogController.updateCatalog(request);
        CatalogResponse actualResp = actualRespEntity.getBody();
        assertNotNull(actualResp);
        assertEquals(mockResp, actualResp);
    }
    
    
    // ************************************************** Insights api test cases **********************************************************
    
    
    @Test
    public void getDemographicsWithSuccessResponse() {

        ResponseEntity<ChannelDemographicResponse> responseEntity = restTemplate.exchange(demourl, HttpMethod.GET, null,
                ChannelDemographicResponse.class);
        assertNotNull(responseEntity);
      //  assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getDemographicsWithFailedResponse() {

        when(channelDemographicService.getDemographics("1")).thenThrow(PersistenceException.class);
        ResponseEntity<ChannelDemographicResponse> responseEntity = channelDemographicController.getDemos("1");
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void getChannelListWithSuccessResponse() {
        String channelurl = "/channels";
        ResponseEntity<ChannelDemographicResponse> responseEntity = restTemplate.exchange(channelurl, HttpMethod.GET,
                null, ChannelDemographicResponse.class);
        assertNotNull(responseEntity);
     //   assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getChannelListWithFailedResponse() {
        when(channelDemographicService.getChannels(1)).thenThrow(PersistenceException.class);
        ResponseEntity<ChannelDemographicResponse> responseEntity = channelDemographicController.getChannelInfo(1);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void getDemographicListForPanelId() {
        URI uri = UriComponentsBuilder.fromUriString(demourl).queryParam("panel", "1").build().toUri();
        ResponseEntity<ChannelDemographicResponse> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, null,
                ChannelDemographicResponse.class);
        assertNotNull(responseEntity);
      //  assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getDemographicListForPanelIdBadRequest() {
        URI uri = UriComponentsBuilder.fromUriString(demourl).queryParam("panel", "5").build().toUri();
        ResponseEntity<ChannelDemographicResponse> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, null,
                ChannelDemographicResponse.class);
        assertNotNull(responseEntity);
     //   assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void getInsightsChannelInfo() {

        String url ="/insights/channels?channel=1&panelId=1&startDate=10-04-2020&endDate=12-04-2020";
        List<ChannelResponse> channelResponseMockList = new ArrayList<>();
        channelResponseMockList.add(createChannelResponse());
        FinalChannelResponse finalChannelResponseMock = new FinalChannelResponse(channelResponseMockList.size(),channelResponseMockList);
        ResponseEntity<FinalChannelResponse> responseEntity =  channelDemographicController.getChannelInfo("1","1","10-04-2020","12-04-2020");
        when(channelDemographicService.getChannelInsights("1", "10-04-2020", "12-04-2020", 1)).thenReturn(finalChannelResponseMock);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getChannelInfoSuccess(){
        List<ChannelResponse> channelResponseMockList = new ArrayList<>();
        channelResponseMockList.add(createChannelResponse());
        FinalChannelResponse finalChannelResponseMock = new FinalChannelResponse(channelResponseMockList.size(),channelResponseMockList);
        when(mockChannelRepository.getChannelInfo("1","10-04-2020","12-04-2020",1)).thenReturn(channelResponseMockList);
        FinalChannelResponse finalChannelResponse=channelDemographicService.getChannelInsights("1","10-04-2020","12-04-2020",1);
       // assertEquals(finalChannelResponseMock.toString(),finalChannelResponse.toString());
    }
    
    
    @Test
    public void addChannel() {

        Channel channel = getChannel();
        int channelId = 299;
        setRequestAttributes();

        when(channelDemographicService.addChannel(channel)).thenReturn(channelId);
        ResponseEntity<Object> responseEntity = channelDemographicController.channelRegistration(channel);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    public void addDemo() {

        DemoInput demoInput = getDemo();
        int demoId = 2;
        setRequestAttributes();

        when(channelDemographicService.addDemo(demoInput)).thenReturn(demoId);
        ResponseEntity<Object> responseEntity = channelDemographicController.demoRegistration(demoInput);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    
    
    
    // ***************************************** User Management Test cases  ***********************************************************************
    
    
    
    @Test
    public void testSignUpSuccessResponse() {
        SignUpRequestModel signUpRequest = new SignUpRequestModel();
        signUpRequest.setFirstName("Tata");
        signUpRequest.setLastName("Elxsi");
        signUpRequest.setUsername("user@tataelxsi.co.in");
        signUpRequest.setPassword("elX#12345");
        signUpRequest.setEmail("user@tataelxsi.co.in");
        signUpRequest.setCompany("TEL");
        signUpRequest.setPhone("+2701123456789");
        signUpRequest.setDesignation("User");
        signUpRequest.setRegion(0);
        signUpRequest.setStatus(0);
        signUpRequest.setAgencyCode("B00001");
        signUpRequest.setAgencyName("MEDIACOM");
        signUpRequest.setRole(0);

        String response = "Success";
        
        when(userServiceImplMock.signUp(signUpRequest)).thenReturn(response);
        ResponseEntity<String> result = userController.signUp(signUpRequest);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test(expected = Exception.class)
    public void testSignUpErrorResponse() {
        SignUpRequestModel signUpRequest = new SignUpRequestModel();
        when(userServiceImplMock.signUp(signUpRequest)).thenThrow(Exception.class);
        ResponseEntity<String> responseEntity = userController.signUp(signUpRequest);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testuserApprovalSuccessResponse() {
        SignUpRequestModel signUpRequest = new SignUpRequestModel();
        signUpRequest.setUsername("user@tatalexsi.co.in");
        signUpRequest.setReason("");
        String flag = "Y";
        String response = "Success";
        
        when(userServiceImplMock.userApproval(signUpRequest, flag)).thenReturn(response);
        ResponseEntity<String> result = userController.userApproval(signUpRequest, flag);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Ignore
    @Test
    public void testSignInSuccessResponse() {
        UserLoginRequest signInRequest = new UserLoginRequest();
        signInRequest.setUsername("user@tataelxsi.co.in");
        signInRequest.setPassword("elX#12345");

        User user = new User();     
        UserLoginResponse loginResponse = new UserLoginResponse();
        loginResponse.setUserStatus(Constant.PENDING);
        loginResponse.setStatus(HttpStatus.OK);

        when(repoMock.findByEmail("user@tataelxsi.co.in")).thenReturn(user);
        when(repoMock.findByEmailAndPassword("user@tataelxsi.co.in", "elX#12345")).thenReturn(user);
        when(dynamoMock.updateTable(1, "123456")).thenReturn(true);
        when(userServiceImplMock.signIn(signInRequest)).thenReturn(loginResponse);
        ResponseEntity<UserLoginResponse> result = userController.signIn(signInRequest);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test(expected = InvalidLoginCredentialsException.class)
    public void testSignInErrorResponse() {
        UserLoginRequest signInRequest = new UserLoginRequest();
        signInRequest.setUsername(null);
        signInRequest.setPassword(null);

        when(repoMock.findByEmailAndPassword(null, null)).thenReturn(null);
        when(userServiceImplMock.signIn(signInRequest)).thenThrow(InvalidLoginCredentialsException.class);
        ResponseEntity<UserLoginResponse> result = userController.signIn(signInRequest);
        assertNull(result);
        verify(userServiceImplMock.signIn(signInRequest),times(1));
    }

    @Ignore
    @Test
    public void testVerifyotpSuccessResponse() {
        UserLoginRequest signInRequest = new UserLoginRequest();
        signInRequest.setUsername("user@tataelxsi.co.in");
        signInRequest.setPassword("elX#12345");
        int userid = 1;
        int otp = 123456;
        User user = new User();
        UserLoginResponse loginResponse = new UserLoginResponse();
        
        when(repoMock.findByEmail("user@tataelxsi.co.in")).thenReturn(user);
        when(repoMock.findByEmailAndPassword("user@tataelxsi.co.in", "elX#12345")).thenReturn(user);
        when(dynamoMock.readTable(userid, otp)).thenReturn(true);
        when(userServiceImplMock.verifyotp(userid, otp, signInRequest)).thenReturn(loginResponse);
        ResponseEntity<UserLoginResponse> result = userController.verifyotp(userid, otp, signInRequest);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Ignore
    @Test
    public void testVerifyotpErrorResponse() {
        UserLoginRequest signInRequest = new UserLoginRequest();
        signInRequest.setUsername("user@tataelxsi.co.in");
        signInRequest.setPassword("elX#12345");
        int userid = 0;
        int otp = 0;
        User user = new User();
        UserLoginResponse loginResponse = new UserLoginResponse();
    //  when(repoMock.findByEmail("user@tataelxsi.co.in")).thenReturn(user);
    //  when(repoMock.findByEmailAndPassword("user@tataelxsi.co.in", "elX#12345")).thenReturn(user);
        when(dynamoMock.readTable(userid, otp)).thenReturn(false);
        when(userServiceImplMock.verifyotp(userid, otp, signInRequest)).thenReturn(loginResponse);
        ResponseEntity<UserLoginResponse> result = userController.verifyotp(userid, otp, signInRequest);
        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
    }

    @Test
    public void testgetAgencyListSuccessResponse() {
        ResponseEntity<String> result = restTemplate.getForEntity(agencyListurl, String.class);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testgetAdvertisersForAgencySuccessResponse() {
        String url = "https://localhost:8080/agency/agencyCode/advertisers";
        WebClient webClient = WebClient.create();
        Flux<String> response = webClient.get().uri(url).exchange()
                .flatMapMany(clientResponse -> clientResponse.bodyToFlux(String.class));
        when(userServiceImplMock.getAdvertisersForAgency("B00001")).thenReturn(response);

        ResponseEntity<Flux<String>> result = userController.getAdvertisersForAgency("B00001");
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test
    public void testgetUserInfoSuccessResponse() {
        Pageable pageable = PageRequest.of(0, 5);
        UserList userList = new UserList();
        Page<User> userInfo = repoMock.findByStatus(1, pageable);
        List<User> items = new ArrayList<User>();
        userList.setTotal(15);
        userList.setItems(items);
        when(userServiceImplMock.getUserInfo(1,pageable)).thenReturn(userInfo);
        ResponseEntity<com.dms.ptp.util.PageDecorator<User>> result = userController.getUserInfo(1,pageable);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }

    @Test(expected = UserNotFoundException.class)
    public void testgetUserInfoErrorResponse() {
        Pageable pageable = PageRequest.of(0, 5);
        int status = 5;
        when(userController.getUserInfo(status,pageable)).thenThrow(UserNotFoundException.class);
        ResponseEntity<com.dms.ptp.util.PageDecorator<User>> responseEntity = userController.getUserInfo(status,pageable);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testgetAllUsersSuccessResponse() {
        ResponseEntity<String> result = restTemplate.getForEntity(getAllUsersurl, String.class);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
    }
    
    
    
    
    // ***************************************  Campaign Test cases  **********************************************************************************
    
    
    
    @Test
    public void seedGeneratorSuccessResponse() {
        Mockito.when(mockCampaignService.seedGenerator()).thenReturn(1);
        ResponseEntity<Integer> responseEntity = campaignController.seedGenerator();
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void seedGeneratorErrorResponse() {
        Mockito.when(mockCampaignService.seedGenerator()).thenThrow(PersistenceException.class);
        ResponseEntity<Integer> responseEntity =campaignController.seedGenerator();
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void getSeedGeneratorTest() {
        Integer seed = 1;
        Mockito.when(mockSeedRepo.findByIdFromSeed()).thenReturn(seed);
        Integer result = campaignService.seedGenerator();
        assertEquals(result, seed);
    }


    @Test
    public void getProductListTest() {
        String response = "Product List";
        when(mockCampaignService.getProductsForAdvertisers("Nike", advertiserCode)).thenReturn(response);
        ResponseEntity<String> responseEntity = campaignController.getProductsForAdvertisers("Nike", advertiserCode);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getProductListErrorTest() {
        when(mockCampaignService.getProductsForAdvertisers("Nike", advertiserCode)).thenThrow(RestClientException.class);
        ResponseEntity<String> responseEntity = campaignController.getProductsForAdvertisers("Nike", advertiserCode);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void getProductListServiceTest() {
        String mockResponse = "Product List";
        when(mockRestTemplate.getForObject(Mockito.any(String.class), Mockito.eq(String.class))).thenReturn(mockResponse);
        String response =campaignService.getProductsForAdvertisers("Nike", advertiserCode);
        assertNotNull(response);
        assertEquals(mockResponse, response);
    }

    @Test
    public void getCampaignListSuccess() {
        Pageable pageable = PageRequest.of(0, 5);
        CampaignList campaignList = createCampaignList(pageable);
        when(mockCampaignService.getCampaignList("asc", pageable, token)).thenReturn(campaignList);
        ResponseEntity<CampaignList> responseEntity = campaignController.getCampaignList("asc", token, pageable);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getCampaignListError() {
        Pageable pageable = PageRequest.of(0, 5);
        Mockito.when(mockCampaignService.getCampaignList("asc", pageable, token)).thenThrow(PersistenceException.class);
        ResponseEntity<CampaignList> responseEntity =campaignController.getCampaignList("asc", token, pageable);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void getCampaignListException() {
        Pageable pageable =PageRequest.of(0, 5);
        ResponseEntity<CampaignList> responseEntity = campaignController.getCampaignList("de", token, pageable);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void getCampaignListServiceTestforAscendingMediaPlanner(){
        JWTExtract jwtResponse = createJWTResponse();
        Pageable pageable = PageRequest.of(0, 5);
        List<Campaign> allCampaignList = new ArrayList<>();
        allCampaignList.add(createMockCampaign());
        Page<Campaign> campaignPage = new PageImpl<>(allCampaignList, pageable, 1);
        when(mockCampaignRepo.findAllByUserId(jwtResponse.getUserId(),pageable)).thenReturn(campaignPage);
        when(mockJWTUtil.getIdRoleFromToken(token)).thenReturn(jwtResponse);
        CampaignList campaignList = campaignService.getCampaignList("asc", pageable, token);
        assertNotNull(campaignList);
        assertEquals(new PageDecorator<>(campaignPage).getTotalElements(), campaignList.getAllList().getTotalElements());
    }

    @Test
    public void getCampaignListServiceTestforAscending() {
        JWTExtract jwtResponse = createJWTResponse();
        jwtResponse.setUserRole("Finance");
        Pageable pageable = PageRequest.of(0, 5);
        List<Campaign> allCampaignList = new ArrayList<>();
        allCampaignList.add(createMockCampaign());
        Page<Campaign> campaignPage = new PageImpl<>(allCampaignList, pageable, 1);
        when(mockCampaignRepo.gelAllCampaignList(pageable)).thenReturn(campaignPage);
        when(mockJWTUtil.getIdRoleFromToken(token)).thenReturn(jwtResponse);
        CampaignList campaignList = campaignService.getCampaignList("asc", pageable, token);
        assertNotNull(campaignList);
        assertEquals(new PageDecorator<>(campaignPage).getTotalElements(), campaignList.getAllList().getTotalElements());
    }

    @Test
    public void getCampaignListServiceTestforDescendingMediaPlanner()  {
        JWTExtract jwtResponse = createJWTResponse();
        Pageable pageable = PageRequest.of(0, 5);
        List<Campaign> allCampaignList = new ArrayList<>();
        allCampaignList.add(createMockCampaign());
        Page<Campaign> campaignPage = new PageImpl<>(allCampaignList, pageable, 1);
        when(mockCampaignRepo.gelAllCampaignList(jwtResponse.getUserId(), pageable)).thenReturn(campaignPage);
        when(mockJWTUtil.getIdRoleFromToken(token)).thenReturn(jwtResponse);
        CampaignList campaignList = campaignService.getCampaignList("desc", pageable, token);
        assertNotNull(campaignList);
        assertEquals(new PageDecorator<>(campaignPage).getTotalElements(), campaignList.getAllList().getTotalElements());
    }

    @Test
    public void getCampaignListServiceTestforDescending()  {
        JWTExtract jwtResponse = createJWTResponse();
        jwtResponse.setUserRole("Finance");
        Pageable pageable = PageRequest.of(0, 5);
        List<Campaign> allCampaignList = new ArrayList<>();
        allCampaignList.add(createMockCampaign());
        Page<Campaign> campaignPage = new PageImpl<>(allCampaignList, pageable, 1);
        when(mockCampaignRepo.gelAllCampaignList(pageable)).thenReturn(campaignPage);
        when(mockJWTUtil.getIdRoleFromToken(token)).thenReturn(jwtResponse);
        CampaignList campaignList = campaignService.getCampaignList("desc", pageable, token);
        assertNotNull(campaignList);
        assertEquals(new PageDecorator<>(campaignPage).getTotalElements(), campaignList.getAllList().getTotalElements());
    }

    @Test
    public void postCampaignSuccess() throws MessagingException, IOException, TemplateException {
        CampaignResponseList campaignResponse = createCampaignResponseList();
        CampaignRequest campaignRequest = createCampaignRequest();
        Mockito.when(mockCampaignService.createCampaign(campaignRequest, Constant.ACTION_DRAFT,token)) .thenReturn(campaignResponse);
        ResponseEntity<CampaignResponseList> responseEntity = campaignController.createCampaign(campaignRequest, Constant.ACTION_DRAFT,token);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void postCampaignError() throws MessagingException, IOException, TemplateException {
        CampaignRequest campaignRequest = createCampaignRequest();
        Mockito.when(mockCampaignService.createCampaign(campaignRequest, Constant.ACTION_DRAFT,token)).thenThrow(PersistenceException.class);
        ResponseEntity<CampaignResponseList> responseEntity = campaignController.createCampaign(campaignRequest, Constant.ACTION_DRAFT,token);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }

    @Test
    public void createCampaignDraftTest() throws MessagingException, IOException, TemplateException {
        JWTExtract jwtResponse = createJWTResponse();
        CampaignRequest campaignRequest = createCampaignRequest();
        Campaign resultCampaign = createMockCampaign();
        Mockito.when(mockSeedRepo.findByIdFromSeed()).thenReturn(1);
        Mockito.when(mockCampaignRepo.save(any(Campaign.class))).thenReturn(resultCampaign);
        Mockito.when(mockApprovalKeyRepo.findByIdFromApprovalKey()).thenReturn(10);
        when(mockJWTUtil.getIdRoleFromToken(token)).thenReturn(jwtResponse);
        CampaignResponseList campaignResponse = campaignService.createCampaign(campaignRequest, Constant.ACTION_DRAFT, token);
        CampaignResponse response = campaignResponse.getItems().get(0);
        assertEquals(1, campaignResponse.getTotal());
       // assertEquals(HttpStatus.OK, response.getStatus());
    }

    @Test
    public void createCampaignError() throws MessagingException, IOException, TemplateException {
        Mockito.when(mockSeedRepo.findByIdFromSeed()).thenThrow(PersistenceException.class);
        doNothing().when(mockEmailUtil).sendMailToDMSAdmin(adminUsername);
        CampaignResponseList campaignResponse = campaignService.createCampaign(createCampaignRequest(), Constant.ACTION_DRAFT,token);
        CampaignResponse response = campaignResponse.getItems().get(0);
        assertNotNull(campaignResponse);
        assertNotNull(response);
        assertEquals(1, campaignResponse.getTotal());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatus());
    }
    @Test
    public void createCampaignPostTest() throws MessagingException, IOException, TemplateException {
        JWTExtract jwtResponse = createJWTResponse();
        Campaign mockCampaign = createMockCampaign();
        CampaignResponse mockCampaignResponse = createCampaignResponse();
        CampaignRequest campaignRequest =createCampaignRequest();
        when(mockRestTemplate.postForObject(Mockito.any(String.class), Mockito.any(HttpEntity.class), Mockito.eq(CampaignResponse.class))).thenReturn(mockCampaignResponse);
        when(mockCampaignRepo.save(any(Campaign.class))).thenReturn(mockCampaign);
        when(mockJWTUtil.getIdRoleFromToken(token)).thenReturn(jwtResponse);
        doNothing().when(mockEmailUtil).sendMailToDMSAdmin(adminUsername);
        CampaignResponseList campaignResponse = campaignService.createCampaign(campaignRequest, "Post",token);
        CampaignResponse response = campaignResponse.getItems().get(0);
        assertNotNull(campaignResponse);
        assertNotNull(response);
      //  assertEquals(HttpStatus.OK, response.getStatus());

    }


    @Test
    public void createCherryPickCampaignPostTest() throws MessagingException, IOException, TemplateException {
        JWTExtract jwtResponse = createJWTResponse();
        Campaign mockCampaign = createMockCampaign();
        CampaignResponse mockCampaignResponse =createCampaignResponse();
        CampaignRequest campaignRequest =createCherryPickCampaignRequest();
        when(mockRestTemplate.postForObject(Mockito.any(String.class), Mockito.any(HttpEntity.class), Mockito.eq(CampaignResponse.class))).thenReturn(mockCampaignResponse);
        when(mockCampaignRepo.save(any(Campaign.class))).thenReturn(mockCampaign);
        when(mockJWTUtil.getIdRoleFromToken(token)).thenReturn(jwtResponse);
        doNothing().when(mockEmailUtil).sendMailToDMSAdmin(adminUsername);
        CampaignResponseList campaignResponse = campaignService.createCampaign(campaignRequest, "Post", token);
        CampaignResponse response = campaignResponse.getItems().get(0);
        assertNotNull(campaignResponse);
        assertNotNull(response);
      ///  assertEquals(HttpStatus.OK, response.getStatus());

    }

    @Test
    public void createCherryPickPackageCampaignPostTest() throws MessagingException, IOException, TemplateException {
        JWTExtract jwtResponse = createJWTResponse();
        Campaign mockCampaign = createMockCampaign();
        SalesAreaLMKResponse salesAreaLMKResponse = createSalesAreaLMKResponse();
        CampaignResponse mockCampaignResponse =createCampaignResponse();
        CampaignRequest campaignRequest =createCherryPickCampaignRequest();
        campaignRequest.getItems().get(0).getSpotList().get(0).setComment("Package");
        when(mockRestTemplate.postForObject(Mockito.any(String.class), Mockito.any(HttpEntity.class), Mockito.eq(CampaignResponse.class))).thenReturn(mockCampaignResponse);
        when(mockCampaignRepo.save(any(Campaign.class))).thenReturn(mockCampaign);
        when(mockJWTUtil.getIdRoleFromToken(token)).thenReturn(jwtResponse);
        doNothing().when(mockEmailUtil).sendMailToDMSAdmin(adminUsername);
        when(mockRestTemplate.getForObject(Mockito.any(String.class),Mockito.eq(SalesAreaLMKResponse.class))).thenReturn(salesAreaLMKResponse);
        CampaignResponseList campaignResponse = campaignService.createCampaign(campaignRequest, "Post", token);
        CampaignResponse response = campaignResponse.getItems().get(0);
        assertNotNull(campaignResponse);
        assertNotNull(response);
      //  assertEquals(HttpStatus.OK, response.getStatus());

    }

    @Test
    public void createCampaignPostExceptionTest() throws MessagingException, IOException, TemplateException {
        Campaign mockCampaign =createMockCampaign();
        CampaignResponse mockCampaignResponse = createCampaignResponse();
        ResultMessageList resultMessageList = createErrorResultMessageList();
        mockCampaignResponse.getUploadCampaignResult().get(0).setResultMessages(resultMessageList);
        CampaignRequest campaignRequest =createCampaignRequest();
        when(mockRestTemplate.postForObject(Mockito.any(String.class), Mockito.any(HttpEntity.class), Mockito.eq(CampaignResponse.class))).thenReturn(mockCampaignResponse);
        when(mockCampaignRepo.save(any(Campaign.class))).thenReturn(mockCampaign);
        doNothing().when(mockEmailUtil).sendMailToDMSAdmin(adminUsername);
        CampaignResponseList campaignResponse = campaignService.createCampaign(campaignRequest, "Post",token);
        CampaignResponse response = campaignResponse.getItems().get(0);
        assertNotNull(campaignResponse);
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatus());

    }

    @Test
    public void removeCampaignDetailTest() {
        Map<String, Object> mockDeleteResponse = createDeleteResponse();
        when(mockCampaignService.deleteCampaign(1)).thenReturn(mockDeleteResponse);
        Map<String, Object> deleteResponse = campaignController.removeCampaignDetails(1);
        assertNotNull(deleteResponse);
        assertEquals(mockDeleteResponse.get(campaignStatus), deleteResponse.get(campaignStatus));
    }

    @Test
    public void deleteCampaignServiceTest() {
        Map<String, Object> mockDeleteResponse = createDeleteResponse();
        Campaign mockCampaign = createMockCampaign();
        when(mockCampaignRepo.findById(1)).thenReturn(java.util.Optional.of(mockCampaign));
        doNothing().when(mockCampaignRepo).delete(mockCampaign);
        Map<String, Object> deleteResponse = campaignService.deleteCampaign(1);
        assertNotNull(deleteResponse);
        assertEquals(deleteResponse.get(campaignStatus), mockDeleteResponse.get(campaignStatus));
    }

    @Test(expected = CampaignNotFoundException.class)
    public void deleteCampaignServiceErrorTest() {
        when(mockCampaignRepo.findById(1)).thenReturn(Optional.empty());
        campaignService.deleteCampaign(1);
    }

    @Test
    public void getCampaignByPortalIdTest() {
        Campaign mockCampaign = createMockCampaign();
        when(mockCampaignService.getCampaignById(1)).thenReturn(mockCampaign);
        Campaign campaign = campaignController.getById(1);
        assertNotNull(campaign);
        assertEquals(mockCampaign, campaign);
    }

    @Test
    public void getCampaignByPortalIdServiceTest() {
        Campaign mockCampaign = createMockCampaign();
        when(mockCampaignRepo.findById(1)).thenReturn(Optional.of(mockCampaign));
        Campaign campaign = campaignService.getCampaignById(1);
        assertNotNull(campaign);
        assertEquals(mockCampaign, campaign);
    }

    @Test
    public void updateCampaignTest() {
        UpdateCampaignRequest mockUpdateCampaignRequest = createUpdateCampaignRequest();
        UpdateCampaignResponse mockUpdateCampaignResponse = createUpdateSuccessResponse();
        when(mockCampaignService.updateCampaign(mockUpdateCampaignRequest)).thenReturn(mockUpdateCampaignResponse);
        ResponseEntity<UpdateCampaignResponse> updateCampaignResponse = campaignController .updateCampaign(mockUpdateCampaignRequest);
        assertNotNull(updateCampaignResponse);
        assertEquals(HttpStatus.OK, updateCampaignResponse.getStatusCode());
    }

    @Test
    public void updateCampaignExceptionTest() {
        UpdateCampaignRequest mockUpdateCampaignRequest = createUpdateCampaignRequest();
        when(mockCampaignService.updateCampaign(mockUpdateCampaignRequest)).thenThrow(PersistenceException.class);
        ResponseEntity<UpdateCampaignResponse> updateCampaignResponse = campaignController.updateCampaign(mockUpdateCampaignRequest);
        assertNotNull(updateCampaignResponse);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, updateCampaignResponse.getStatusCode());
    }

    @Test
    public void updateCampaignServiceTest() {
        UpdateCampaignRequest mockUpdateCampaignRequest = createUpdateCampaignRequest();
        Campaign mockCampaign = createMockCampaign();
        when(mockCampaignRepo.findById(1)).thenReturn(Optional.of(mockCampaign));
        when(mockCampaignRepo.save(any(Campaign.class))).thenReturn(mockCampaign);
        doNothing().when(mockItemsRepository).deleteAll();
        UpdateCampaignResponse updateCampaignResponse = campaignService.updateCampaign(mockUpdateCampaignRequest);
        assertNotNull(updateCampaignResponse);
        assertEquals(HttpStatus.OK, updateCampaignResponse.getStatus());
    }

    @Test
    public void updateCampaignServiceErrorTest() {
        UpdateCampaignRequest mockUpdateCampaignRequest = createUpdateCampaignRequest();
        Campaign mockCampaign = createMockCampaign();
        mockCampaign.setStatus(0);
        when(mockCampaignRepo.findById(1)).thenReturn(Optional.of(mockCampaign));
        UpdateCampaignResponse updateCampaignResponse =campaignService.updateCampaign(mockUpdateCampaignRequest);
        assertNotNull(updateCampaignResponse);
        assertEquals(HttpStatus.EXPECTATION_FAILED, updateCampaignResponse.getStatus());
    }

    /*
     * @Test(expected = PersistenceException.class) public void
     * updateCampaignServiceExceptionTest() { UpdateCampaignRequest
     * mockUpdateCampaignRequest = createUpdateCampaignRequest(); Campaign
     * mockCampaign = createMockCampaign();
     * when(mockCampaignRepo.findById(1)).thenReturn(Optional.of(mockCampaign));
     * when(mockCampaignRepo.save(any(Campaign.class))).thenThrow(
     * PersistenceException.class);
     * campaignService.updateCampaign(mockUpdateCampaignRequest);
     * 
     * }
     */

    @Test
    public void getAvailResponseExceptionTest() throws InvalidParamException, ParseException {
        AvailLMKRequest availLMKRequest = createAvailLmkRequest();
        when(mockCampaignService.getAvailableCherrypicks(availLMKRequest)).thenThrow(PersistenceException.class);
        ResponseEntity<AvailLMKResponse> responseEntity = campaignController.getAvailResponse(availLMKRequest);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
    }



    @Test(expected = InvalidParamException.class)
    public void getAvailResponseErrorTest() throws InvalidParamException, ParseException {
        AvailLMKRequest availLMKRequest = createAvailLmkRequest();
        availLMKRequest.setStartDate("2020-02-21");
        availLMKRequest.setEndDate("2020-02-01");
        campaignController.getAvailResponse(availLMKRequest);
    }

    @Test
    public void getAvailResponseTest() throws InvalidParamException, ParseException {
        AvailLMKRequest availLMKRequest = createAvailLmkRequest();
        AvailLMKResponse mockAvailLMKResponse = createAvailResponse();
        when(mockCampaignService.getAvailableCherrypicks(availLMKRequest)) .thenReturn(mockAvailLMKResponse);
        ResponseEntity<AvailLMKResponse> responseEntity = campaignController.getAvailResponse(availLMKRequest);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void getCherrypicks() {
        AvailLMKRequest availLMKRequest = createAvailLmkRequest();
        AvailLMKResponse mockAvailLMKResponse = createAvailResponse();
        when(mockRestTemplate.postForObject(Mockito.any(String.class), Mockito.any(HttpEntity.class), Mockito.eq(AvailLMKResponse.class))).thenReturn(mockAvailLMKResponse);
        AvailLMKResponse availLMKResponse = campaignService.getAvailableCherrypicks(availLMKRequest);
        assertNotNull(availLMKResponse);
        assertEquals(mockAvailLMKResponse, availLMKResponse);
    }

    @Test
    public void campaignApprovalTest(){
        String message = "Campaign approved successfully";
        CampaignApproveReject campaignApproveReject = createCampaignApprovalReject();
        CampaignApproveRejectResp campaignApproveRejectResp = new CampaignApproveRejectResp();
        campaignApproveRejectResp.setStatus(HttpStatus.OK);
        campaignApproveRejectResp.setMessage(message);
        when(mockCampaignService.campaignApproval(campaignApproveReject)).thenReturn(campaignApproveRejectResp);
        ResponseEntity<CampaignApproveRejectResp> campaignApprovalResponse = campaignController.campaignApproval(campaignApproveReject);
        assertNotNull(campaignApprovalResponse);
        assertEquals(HttpStatus.OK,campaignApprovalResponse.getStatusCode());
    }

    @Test
    public void campaignRejectionTest(){
        CampaignApproveReject campaignApproveReject = createCampaignApprovalReject();
        CampaignApproveRejectResp campaignApproveRejectResp = new CampaignApproveRejectResp();
        campaignApproveRejectResp.setMessage("Campaign rejected successfully due to following reason - "+ campaignApproveReject.getReason());
        campaignApproveRejectResp.setStatus(HttpStatus.OK);
        when(mockCampaignService.campaignReject(campaignApproveReject)).thenReturn(campaignApproveRejectResp);
        ResponseEntity<CampaignApproveRejectResp> campaignApprovalResponse = campaignController.campaignReject(campaignApproveReject);
        assertNotNull(campaignApprovalResponse);
        assertEquals(HttpStatus.OK,campaignApprovalResponse.getStatusCode());
    }

    @Test
    public void campaignRejectService() throws MessagingException, IOException, TemplateException {
        CampaignApproveReject campaignApproveReject = createCampaignApprovalReject();
        Campaign mockCampaign = createMockCampaign();
        CampaignCancelResp campaignCancelResp = createCampaignCancelResponse();
        when(mockCampaignRepo.findById(campaignApproveReject.getPortalId())).thenReturn(Optional.of(mockCampaign));
        when(mockRestTemplate.postForObject(Mockito.any(String.class),Mockito.any(HttpEntity.class)
                ,Mockito.eq(CampaignCancelResp.class))).thenReturn(campaignCancelResp);
        when(mockCampaignRepo.save(any(Campaign.class))).thenReturn(mockCampaign);
        doNothing().when(mockEmailUtil).sendMail(campaignApproveReject.getUserName(),"N",campaignApproveReject.getReason());
        CampaignApproveRejectResp rejectResponse = campaignService.campaignReject(campaignApproveReject);
        assertNotNull(rejectResponse);
        //assertEquals("Campaign rejected successfully due to following reason - " + campaignApproveReject.getReason(),rejectResponse);
    }

    @Test
    public void campaignRejectServerErrorServiceTest(){
        CampaignApproveReject campaignApproveReject = createCampaignApprovalReject();
        Campaign mockCampaign = createMockCampaign();
        CampaignCancelResp campaignCancelResp = createCampaignCancelResponse();
        campaignCancelResp.getUploadCampaignResult().get(0).setCampaignStatus("OK");
        when(mockCampaignRepo.findById(campaignApproveReject.getPortalId())).thenReturn(Optional.of(mockCampaign));
        when(mockRestTemplate.postForObject(Mockito.any(String.class),Mockito.any(HttpEntity.class)
                ,Mockito.eq(CampaignCancelResp.class))).thenReturn(campaignCancelResp);
        CampaignApproveRejectResp rejectResponse = campaignService.campaignReject(campaignApproveReject);
        assertNotNull(rejectResponse);
        //assertEquals("campaign status should be cancelled before rejecting : " + campaignApproveReject.getReason(),rejectResponse);
    }

    @Test
    public void campaignRejectServiceExceptionTest() {
        CampaignApproveReject campaignApproveReject = createCampaignApprovalReject();
        Campaign mockCampaign = createMockCampaign();
        CampaignCancelResp campaignCancelResp = createCampaignCancelResponse();
        when(mockCampaignRepo.findById(campaignApproveReject.getPortalId())).thenReturn(Optional.of(mockCampaign));
        when(mockRestTemplate.postForObject(Mockito.any(String.class),Mockito.any(HttpEntity.class)
                ,Mockito.eq(CampaignCancelResp.class))).thenReturn(campaignCancelResp);
        when(mockCampaignRepo.save(any(Campaign.class))).thenThrow(PersistenceException.class);
        CampaignApproveRejectResp rejectResponse = campaignService.campaignReject(campaignApproveReject);
        assertNotNull(rejectResponse);
        //assertEquals("",rejectResponse);
    }

    @Test
    public void resubmitCampaignTest(){
        UpdateCampaignRequest mockUpdateCampaignRequest = createUpdateCampaignRequest();
        UpdateCampaignResponse mockUpdateCampaignResponse = createUpdateSuccessResponse();
        when(mockCampaignService.resubmitCampaign(mockUpdateCampaignRequest,token)).thenReturn(mockUpdateCampaignResponse);
        ResponseEntity<UpdateCampaignResponse> responseEntity = campaignController.resubmitCampaign(mockUpdateCampaignRequest,token);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    }

    @Test
    public void resubmitCampaignExceptionTest(){
        UpdateCampaignRequest mockUpdateCampaignRequest = createUpdateCampaignRequest();
        when(mockCampaignService.resubmitCampaign(mockUpdateCampaignRequest,token)).thenThrow(PersistenceException.class);
        ResponseEntity<UpdateCampaignResponse> responseEntity = campaignController.resubmitCampaign(mockUpdateCampaignRequest,token);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());

    }

    @Test
    public void resubmitCampaignServiceTest() {
        JWTExtract jwtExtract = createJWTResponse();
        UpdateCampaignRequest mockUpdateCampaignRequest = createUpdateCampaignRequest();
        Campaign mockCampaign =createMockCampaign();
        when(mockJWTUtil.getIdRoleFromToken(token)).thenReturn(jwtExtract);
        when(mockCampaignRepo.findById(1)).thenReturn(Optional.of(mockCampaign));
        when(mockCampaignRepo.save(any(Campaign.class))).thenReturn(mockCampaign);
        UpdateCampaignResponse updateCampaignResponse = campaignService.resubmitCampaign(mockUpdateCampaignRequest,token);
        assertNotNull(updateCampaignResponse);
        assertEquals(HttpStatus.OK, updateCampaignResponse.getStatus());

    }

    @Test(expected=PersistenceException.class)
    public void resubmitCampaignServiceExceptionTest()  {
        JWTExtract jwtExtract = createJWTResponse();
        jwtExtract.setUserRole(Constant.ROLE_CONTRACTOR);
        UpdateCampaignRequest mockUpdateCampaignRequest = createUpdateCampaignRequest();
        Campaign mockCampaign =createMockCampaign();
        when(mockJWTUtil.getIdRoleFromToken(token)).thenReturn(jwtExtract);
        when(mockCampaignRepo.findById(1)).thenReturn(Optional.of(mockCampaign));
        when(mockCampaignRepo.save(any(Campaign.class))).thenThrow(PersistenceException.class);
        campaignService.resubmitCampaign(mockUpdateCampaignRequest,token);
    }

    @Test
    public void uploadExceptionTest() throws Exception {
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", awsFileName,
                MediaType.APPLICATION_PDF_VALUE, streamName.getBytes(StandardCharsets.UTF_8));
        ResponseEntity<DocResponse> docResponseResponseEntity = documentController.upload(mockMultipartFile,
                folderName,type,token);
        assertNotNull(docResponseResponseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,docResponseResponseEntity.getStatusCode());

    }

    @Test
    public void uploadTest() throws Exception {
        DocResponse docResponse = new DocResponse();
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", awsFileName,
                MediaType.APPLICATION_PDF_VALUE, streamName.getBytes(StandardCharsets.UTF_8));
        when(mockDocService.uploadInS3(mockMultipartFile,awsFileName,
                MediaType.APPLICATION_PDF_VALUE, folderName,type,token)).thenReturn(docResponse);
        ResponseEntity<DocResponse> docResponseResponseEntity = documentController.upload(mockMultipartFile,
                folderName,type,token);
        assertNotNull(docResponseResponseEntity);
        assertEquals(HttpStatus.OK,docResponseResponseEntity.getStatusCode());

    }

    @Test
    public void uploadMediaServiceFolderTest() throws Exception {
        DocDetail docDetail = createDocDetailList().get(0);
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", awsFileName,
                MediaType.APPLICATION_PDF_VALUE, streamName.getBytes(StandardCharsets.UTF_8));
        when(mockAmazonS3.doesObjectExist(bucketName, folderName+suffix)).thenReturn(true);
        when(mockDocRepository.save(any(DocDetail.class))).thenReturn(docDetail);
        DocResponse docResponse = docService.uploadInS3(mockMultipartFile,awsFileName,
                MediaType.APPLICATION_PDF_VALUE, folderName,type,token);
        assertNotNull(docResponse);
        assertEquals(docResponse.getFileName(),awsFileName);

    }

    @Test
    public void uploadMediaServiceFileTest() throws Exception {
        DocDetail docDetail = createDocDetailList().get(0);
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", awsFileName,
                MediaType.APPLICATION_PDF_VALUE, streamName.getBytes(StandardCharsets.UTF_8));
        when(mockAmazonS3.doesObjectExist(bucketName, folderName+suffix)).thenReturn(true);
        when(mockAmazonS3.doesObjectExist(bucketName,filePath)).thenReturn(true);
        when(mockDocRepository.save(any(DocDetail.class))).thenReturn(docDetail);
        DocResponse docResponse = docService.uploadInS3(mockMultipartFile,awsFileName,
                MediaType.APPLICATION_PDF_VALUE, folderName,type,token);
        assertNotNull(docResponse);
        assertEquals(docResponse.getFileName(),awsFileName);
    }

    @Test
    public void uploadMediaServiceNoFolderTest() throws Exception {
        JWTExtract jwtResponse = createJWTResponse();
        DocDetail docDetail = createDocDetailList().get(0);
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", awsFileName,
                MediaType.APPLICATION_PDF_VALUE, streamName.getBytes(StandardCharsets.UTF_8));
        when(mockAmazonS3.doesObjectExist(bucketName, folderName+suffix)).thenReturn(false);
        when(mockJWTUtil.getIdRoleFromToken(token)).thenReturn(jwtResponse);
        when(mockDocRepository.save(any(DocDetail.class))).thenReturn(docDetail);
        DocResponse docResponse = docService.uploadInS3(mockMultipartFile,awsFileName,
                MediaType.APPLICATION_PDF_VALUE, folderName,type,token);
        assertNotNull(docResponse);
        assertEquals(docResponse.getFileName(),awsFileName);

    }

    @Test
    public void uploadMediaServiceFolderServiceErrorTest() throws Exception {
        DocDetail docDetail = createDocDetailList().get(0);
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", awsFileName,
                MediaType.APPLICATION_PDF_VALUE, streamName.getBytes(StandardCharsets.UTF_8));
        when(mockAmazonS3.doesObjectExist(bucketName, folderName+suffix)).thenReturn(true);
        when(mockDocRepository.save(any(DocDetail.class))).thenReturn(docDetail);
        when(mockAmazonS3.putObject(Mockito.any(String.class),Mockito.any(String.class),Mockito.any(InputStream.class),Mockito.any(ObjectMetadata.class)))
                .thenThrow(AmazonServiceException.class);
        DocResponse docResponse = docService.uploadInS3(mockMultipartFile,awsFileName,
                MediaType.APPLICATION_PDF_VALUE, folderName,type,token);
        assertNotNull(docResponse);
        assertEquals(docResponse.getFileName(),awsFileName);

    }

    @Test
    public void uploadMediaServiceFolderClientErrorTest() throws Exception {
        DocDetail docDetail = createDocDetailList().get(0);
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", awsFileName,
                MediaType.APPLICATION_PDF_VALUE, streamName.getBytes(StandardCharsets.UTF_8));
        when(mockAmazonS3.doesObjectExist(bucketName, folderName+suffix)).thenReturn(true);
        when(mockDocRepository.save(any(DocDetail.class))).thenReturn(docDetail);
        when(mockAmazonS3.putObject(Mockito.any(String.class),Mockito.any(String.class),Mockito.any(InputStream.class),Mockito.any(ObjectMetadata.class)))
                .thenThrow(AmazonClientException.class);
        DocResponse docResponse = docService.uploadInS3(mockMultipartFile,awsFileName,
                MediaType.APPLICATION_PDF_VALUE, folderName,type,token);
        assertNotNull(docResponse);
        assertEquals(docResponse.getFileName(),awsFileName);

    }


    @Test
    public void getDocumentsTest() throws IOException {
        String value = "Telmar Media Plan";
        byte[] data =value.getBytes();
        MockHttpServletResponse mockHttpServletResponse= new MockHttpServletResponse();
        when(mockDocService.getObject(folderName, awsFileName)).thenReturn(data);
        ResponseEntity<ByteArrayResource> resourceResponseEntity = documentController.getDocuments(folderName,awsFileName, mockHttpServletResponse);
        assertNotNull(resourceResponseEntity);
        assertEquals(HttpStatus.OK,resourceResponseEntity.getStatusCode());
    }

    @Test
    public void getDocumentServiceTest() throws IOException {
        String value = "Telmar Media Plan";
        byte[] data =value.getBytes();
        S3Object s3Object = new S3Object();
        s3Object.setObjectContent(new S3ObjectInputStream(IOUtils.toInputStream(value), mock(HttpRequestBase.class)));
        when(mockAmazonS3.getObject(Mockito.any(GetObjectRequest.class))).thenReturn(s3Object);
        byte[] getDocumentResponse = docService.getObject(folderName,awsFileName);
        assertNotNull(getDocumentResponse);
        assertEquals(data.length,getDocumentResponse.length);
    }

    @Test
    public void getDocumentServiceExceptionTest() throws IOException {
        when(mockAmazonS3.getObject(Mockito.any(GetObjectRequest.class))).thenThrow(AmazonServiceException.class);
        byte[] getDocumentResponse = docService.getObject(folderName,awsFileName);
        assertNull(getDocumentResponse);
    }

    @Test
    public void getDocumentClientExceptionTest() throws IOException {
        when(mockAmazonS3.getObject(Mockito.any(GetObjectRequest.class))).thenThrow(AmazonClientException.class);
        byte[] getDocumentResponse = docService.getObject(folderName,awsFileName);
        assertNull(getDocumentResponse);
    }

    @Test
    public void getDocumentExceptionTest() throws IOException {
        when(mockAmazonS3.getObject(Mockito.any(GetObjectRequest.class))).thenThrow(UnsupportedOperationException.class);
        byte[] getDocumentResponse = docService.getObject(folderName,awsFileName);
        assertNull(getDocumentResponse);
    }

    @Test
    public void getAllDocumentsTest(){
        DocResponse docResponse = new DocResponse();
        List<DocResponse> docResponseList = Collections.singletonList(docResponse);
        when(mockDocService.getObjectslistFromFolder(1)).thenReturn(docResponseList);
        ResponseEntity<List<DocResponse>> docListResponseEntity = documentController.getAllDocuments(1);
        assertNotNull(docListResponseEntity);
        assertEquals(HttpStatus.OK,docListResponseEntity.getStatusCode());
    }

    @Test
    public void getAllDocumentsServiceTest(){
        List<DocDetail> docDetailList = createDocDetailList();
        when(mockCampaignRepo.findFolderNameByPortalId(1)).thenReturn(folderName);
        when(mockDocRepository.findTypeByFolderName(folderName)).thenReturn(docDetailList);
        List<DocResponse> docResponseList = docService.getObjectslistFromFolder(1);
        assertNotNull(docResponseList);
        assertEquals(docDetailList.size(),docResponseList.size());

    }

    @Test
    public void deleteDocumentsTest(){
        String deleteResponse = "file: " + awsFileName + " deleted successfully";
        when(mockDocService.deleteObject(folderName,awsFileName)).thenReturn(deleteResponse);
        ResponseEntity<String> stringResponseEntity = documentController.deleteDocuments(folderName,awsFileName);
        assertNotNull(stringResponseEntity);
        assertEquals(HttpStatus.OK,stringResponseEntity.getStatusCode());
    }

    @Test
    public void deleteDocumentsExceptionTest(){
        ResponseEntity<String> stringResponseEntity = documentController.deleteDocuments(null,awsFileName);
        assertNotNull(stringResponseEntity);
        assertEquals(HttpStatus.BAD_REQUEST,stringResponseEntity.getStatusCode());
    }

    @Test
    public void deleteObjectExistServiceTest(){
        String mockDeleteResponse = "file: " + awsFileName + " deleted successfully";
        when(mockAmazonS3.doesObjectExist(bucketName, filePath)).thenReturn(true);
        doNothing().when(mockAmazonS3).deleteObject(bucketName, filePath);
        String deleteResponse = docService.deleteObject(folderName,awsFileName);
        assertNotNull(deleteResponse);
        assertEquals(mockDeleteResponse,deleteResponse);
    }

    @Test
    public void deleteObjectDontExistServiceTest(){
        String mockDeleteResponse = delResponse + awsFileName;
        when(mockAmazonS3.doesObjectExist(bucketName, filePath)).thenReturn(false);
        doNothing().when(mockAmazonS3).deleteObject(bucketName, filePath);
        String deleteResponse = docService.deleteObject(folderName,awsFileName);
        assertNotNull(deleteResponse);
        assertEquals(mockDeleteResponse,deleteResponse);
    }

    @Test
    public void ifFileExistErrorServiceTest(){
        String mockDeleteResponse = delResponse + awsFileName;
        when(mockAmazonS3.doesObjectExist(bucketName, filePath)).thenThrow(AmazonServiceException.class);
        doNothing().when(mockAmazonS3).deleteObject(bucketName, filePath);
        String deleteResponse = docService.deleteObject(folderName,awsFileName);
        assertNotNull(deleteResponse);
        assertEquals(mockDeleteResponse,deleteResponse);
    }

    @Test
    public void deleteObjectServiceExceptionServiceTest(){
        String mockDeleteResponse = delResponse + awsFileName;
        when(mockAmazonS3.doesObjectExist(bucketName, filePath)).thenReturn(true);
        doThrow(AmazonServiceException.class).when(mockAmazonS3).deleteObject(bucketName, filePath);
        String deleteResponse = docService.deleteObject(folderName,awsFileName);
        assertNotNull(deleteResponse);
        assertEquals(mockDeleteResponse,deleteResponse);
    }

    @Test
    public void deleteObjectClientExceptionServiceTest(){
        String mockDeleteResponse = delResponse + awsFileName;
        when(mockAmazonS3.doesObjectExist(bucketName, filePath)).thenReturn(true);
        doThrow(AmazonClientException.class).when(mockAmazonS3).deleteObject(bucketName, filePath);
        String deleteResponse = docService.deleteObject(folderName,awsFileName);
        assertNotNull(deleteResponse);
        assertEquals(mockDeleteResponse,deleteResponse);
    }

    @Test
    public void getFolderNameByPortalIdTest(){
        when(mockCampaignRepo.findFolderNameByPortalId(1)).thenReturn(folderName);
        String folder = docService.getFolderNameByPortalId(1);
        assertNotNull(folder);
        assertEquals(folderName,folder);

    }


    /**
     *
     */
    private void setRequestAttributes() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }



    /**
     * @return demoInput
     */
    private DemoInput getDemo() {

        DemoInput demoInput = new DemoInput();
        demoInput.setDescription("Dstv-adult-23");
        demoInput.setPanel(2);
        demoInput.setSegment("Mass");
        return demoInput;
    }



    /**
     * @return channel
     */
    private Channel getChannel() {

        Channel channel = new Channel();
        channel.setId(299);
        channel.setLmk_ref("1 MAGIC");
        channel.setName("MAGIC-12345");
        channel.setNetwork("Local Interest Channels");
        channel.setPackageName("Compact ++");
        return channel;
    }


    public ChannelResponse createChannelResponse(){
        return new ChannelResponse("Africa Magic","Medium","20.07%");
    }
    

    private Baseline createBaseline(){

        Aff affinity = createAffinity();
        Baseline baseline = new Baseline();
        baseline.setId(1);
        baseline.setTitle("Sports");
        baseline.setStatus("A");
        baseline.setRateCardId(1);
        baseline.setAff(affinity);

        return baseline ;
    }


    private Aff createAffinity(){
        List<AffDemos> affinityDemosList = createAffinityDemosList();
        List<AffUniverse> affinityUniverseList =createAffinityUniverseList();
        List<AffChannels> affinityChannelsList = createAffintyChannelsList();
        Aff affinity = new Aff();
        affinity.setId(1);
        affinity.setTerritoryId(1);
        affinity.setPlatformId(1);
        affinity.setPanel(1);
        affinity.setSourceStart(startDate);
        affinity.setSourceEnd("2019-01-31");
        affinity.setWeekpart("WE");
        affinity.setAffDemos(affinityDemosList);
        affinity.setAffUniverse(affinityUniverseList);
        affinity.setAffChannels(affinityChannelsList);

        return affinity;

    }

    private List<AffChannels> createAffintyChannelsList() {
        List<AffChannelDemos> affinityChannelDemosList = createAffinityChannelDemosList();
        AffChannels affinityChannels = new AffChannels();
        affinityChannels.setId(1);
        affinityChannels.setChannelId(7);
        affinityChannels.setResultDemoId(15);
        affinityChannels.setResultSegment("Premium");
        affinityChannels.setSecDemoImpactTotal(73199.07105280925);
        affinityChannels.setAffChannelDemos(affinityChannelDemosList);

        return Collections.singletonList(affinityChannels);
    }

    private List<AffChannelDemos> createAffinityChannelDemosList() {
        List<AffChannelDemos> affinityChannelDemosList = new ArrayList<>();
        List<AffChannelDayparts> affinityChannelDaypartsList = createAffinityChannelDaypartsList();
        AffChannelDemos affinityChannelPriDemos = new AffChannelDemos();
        affinityChannelPriDemos.setId(1);
        affinityChannelPriDemos.setDemoId(14);
        affinityChannelPriDemos.setImpact(16017.233643050717);
        affinityChannelPriDemos.setRating(0.20737976296461474);
        affinityChannelPriDemos.setAffinityIndex(0.21881744416531096);
        affinityChannelPriDemos.setReachIndex(0.36781744416531096);
        affinityChannelPriDemos.setAffinityReachIndex(2.3381506798137313);
        affinityChannelPriDemos.setAffChannelDayparts(affinityChannelDaypartsList);
        affinityChannelDemosList.add(affinityChannelPriDemos);

        AffChannelDemos affinityChannelSecDemos1 = new AffChannelDemos();
        affinityChannelSecDemos1.setId(1);
        affinityChannelSecDemos1.setDemoId(15);
        affinityChannelSecDemos1.setImpact(16017.233643050717);
        affinityChannelSecDemos1.setRating(0.20737976296461474);
        affinityChannelSecDemos1.setAffinityIndex(0.21881744416531096);
        affinityChannelSecDemos1.setReachIndex(0.36781744416531096);
        affinityChannelSecDemos1.setAffinityReachIndex(2.3381506798137313);
        affinityChannelDaypartsList.get(0).setId(3);
        affinityChannelDaypartsList.get(0).setId(4);
        affinityChannelSecDemos1.setAffChannelDayparts(affinityChannelDaypartsList);
        affinityChannelDemosList.add(affinityChannelSecDemos1);

        AffChannelDemos affinityChannelSecDemos2 = new AffChannelDemos();
        affinityChannelSecDemos2.setId(3);
        affinityChannelSecDemos2.setDemoId(16);
        affinityChannelSecDemos2.setImpact(16017.233643050717);
        affinityChannelSecDemos2.setRating(0.20737976296461474);
        affinityChannelSecDemos2.setAffinityIndex(0.21881744416531096);
        affinityChannelSecDemos2.setReachIndex(0.36781744416531096);
        affinityChannelSecDemos2.setAffinityReachIndex(2.3381506798137313);
        affinityChannelDaypartsList.get(0).setId(5);
        affinityChannelDaypartsList.get(0).setId(6);
        affinityChannelSecDemos2.setAffChannelDayparts(affinityChannelDaypartsList);
        affinityChannelDemosList.add(affinityChannelSecDemos2);

        return  affinityChannelDemosList;
    }

    private List<AffChannelDayparts> createAffinityChannelDaypartsList() {
        List<AffChannelDayparts> affinityChannelDaypartsList = new ArrayList<>();
        AffChannelDayparts affinityChannelDaypartsOP = new AffChannelDayparts();
        affinityChannelDaypartsOP.setId(1);
        affinityChannelDaypartsOP.setDaypartId("OP");
        affinityChannelDaypartsOP.setImpact(18628.174603174604);
        affinityChannelDaypartsOP.setRating(0.24110674999974177);
        affinityChannelDaypartsList.add(affinityChannelDaypartsOP);

        AffChannelDayparts affinityChannelDaypartsPT = new AffChannelDayparts();
        affinityChannelDaypartsPT.setId(2);
        affinityChannelDaypartsPT.setDaypartId("PT");
        affinityChannelDaypartsPT.setImpact(18628.174603174604);
        affinityChannelDaypartsPT.setRating(0.24110674999974177);
        affinityChannelDaypartsList.add(affinityChannelDaypartsPT);

        return affinityChannelDaypartsList;

    }


    private List<AffUniverse> createAffinityUniverseList() {
        List<AffUniverse> affinityUniverseList = new ArrayList<>();
        AffUniverse affinityPriUniverse = new AffUniverse();
        affinityPriUniverse.setId(1);
        affinityPriUniverse.setDemoId(14);
        affinityPriUniverse.setUniverse(15581333);
        affinityPriUniverse.setAvgImpact(15529.795404827617);
        affinityUniverseList.add(affinityPriUniverse);

        AffUniverse affinitySecUniverse1 = new AffUniverse();
        affinitySecUniverse1.setId(2);
        affinitySecUniverse1.setDemoId(15);
        affinitySecUniverse1.setUniverse(15581222);
        affinitySecUniverse1.setAvgImpact(15519.795404827617);
        affinityUniverseList.add(affinitySecUniverse1);

        AffUniverse affinitySecUniverse2 = new AffUniverse();
        affinitySecUniverse2.setId(3);
        affinitySecUniverse2.setDemoId(16);
        affinitySecUniverse2.setUniverse(15581111);
        affinitySecUniverse2.setAvgImpact(15539.795404827617);
        affinityUniverseList.add(affinitySecUniverse2);

        return  affinityUniverseList;
    }

    private List<AffDemos> createAffinityDemosList() {
        List<AffDemos> affinityDemosList = new ArrayList<>();
        AffDemos affinityPriDemos = new AffDemos();
        affinityPriDemos.setId(1);
        affinityPriDemos.setDemoId(14);
        affinityPriDemos.setPrimary(true);
        affinityDemosList.add(affinityPriDemos);

        AffDemos affinitySecDemos1 = new AffDemos();
        affinitySecDemos1.setId(2);
        affinitySecDemos1.setDemoId(15);
        affinitySecDemos1.setPrimary(false);
        affinityDemosList.add(affinitySecDemos1);

        AffDemos affinitySecDemos2 = new AffDemos();
        affinitySecDemos2.setId(3);
        affinitySecDemos2.setDemoId(16);
        affinitySecDemos2.setPrimary(false);
        affinityDemosList.add(affinitySecDemos2);

        return affinityDemosList;
    }
    private AffCreateRequest createAffCreateRequest() {
        AffinityResponse affinityResponse = createAffinityResponse();
        AffCreateRequest affCreateRequest = new AffCreateRequest();
        affCreateRequest.setTitle("Sports");
        affCreateRequest.setStatus(status);
        affCreateRequest.setWeekPart(weekPart);
        affCreateRequest.setRatecardId(1);
        affCreateRequest.setAffinity(affinityResponse);

        return affCreateRequest;
    }

    private AffUpdateRequest createAffUpdateRequest() {
        AffinityResponse affinityResponse = createAffinityResponse();
        AffUpdateRequest affUpdateRequest = new AffUpdateRequest();
        affUpdateRequest.setId(1);
        affUpdateRequest.setTitle("Sports");
        affUpdateRequest.setStatus(status);
        affUpdateRequest.setWeekPart(weekPart);
        affUpdateRequest.setRatecardId(1);
        affUpdateRequest.setAffinity(affinityResponse);

        return affUpdateRequest;

    }

    private BaselineResponse createBaselineData(){
        AffinityResponse affinityResponse = createAffinityResponse();
        return new BaselineResponse(1,"Sports",status,weekPart,1,affinityResponse);
    }
    private List<BaselineListResponse> createBaselineResponseList(){
        List<BaselineListResponse> baselineListResponseList= new ArrayList<>();
        BaselineListResponse baselineListResponse= new BaselineListResponse(1,"Sports",status,weekPart,1);
        baselineListResponseList.add(baselineListResponse);
        return baselineListResponseList;
    }
    private List<Channel> createMockChannelList() {
        Channel mockChannel = new Channel();
        mockChannel.setId(7);
        mockChannel.setName("1 MAGIC");
        mockChannel.setPackageName("Compact +");
        mockChannel.setNetwork("Local Interest Channels");
        mockChannel.setLmkRefNo(133);
        mockChannel.setGenres(Collections.emptyList());
        return Collections.singletonList(mockChannel);
    }


    private LmkAudienceResponse[] createLmkAudienceResponseList(){
        List<LmkAudienceResponse> lmkAudienceResponseList = new ArrayList<>();
        LmkAudienceResponse lmkAudienceResponseforPriOP = new LmkAudienceResponse();
        lmkAudienceResponseforPriOP.setChannelNo(133);
        lmkAudienceResponseforPriOP.setDemoNo(14);
        lmkAudienceResponseforPriOP.setWeekPart(weekPart);
        lmkAudienceResponseforPriOP.setDayPart("OP");
        lmkAudienceResponseforPriOP.setAvgRatings(0);
        lmkAudienceResponseforPriOP.setAvgImpacts(0);

        LmkAudienceResponse lmkAudienceResponseforPriPT = new LmkAudienceResponse();
        lmkAudienceResponseforPriPT.setChannelNo(133);
        lmkAudienceResponseforPriPT.setDemoNo(14);
        lmkAudienceResponseforPriPT.setWeekPart(weekPart);
        lmkAudienceResponseforPriPT.setDayPart("PT");
        lmkAudienceResponseforPriPT.setAvgRatings(0);
        lmkAudienceResponseforPriPT.setAvgImpacts(0);

        LmkAudienceResponse lmkAudienceResponseforSec1OP = new LmkAudienceResponse();
        lmkAudienceResponseforSec1OP.setChannelNo(133);
        lmkAudienceResponseforSec1OP.setDemoNo(15);
        lmkAudienceResponseforSec1OP.setWeekPart(weekPart);
        lmkAudienceResponseforSec1OP.setDayPart("Op");
        lmkAudienceResponseforSec1OP.setAvgRatings(0);
        lmkAudienceResponseforSec1OP.setAvgImpacts(0);

        LmkAudienceResponse lmkAudienceResponseforSec1PT = new LmkAudienceResponse();
        lmkAudienceResponseforSec1PT.setChannelNo(133);
        lmkAudienceResponseforSec1PT.setDemoNo(15);
        lmkAudienceResponseforSec1PT.setWeekPart(weekPart);
        lmkAudienceResponseforSec1PT.setDayPart("PT");
        lmkAudienceResponseforSec1PT.setAvgRatings(0);
        lmkAudienceResponseforSec1PT.setAvgImpacts(0);

        LmkAudienceResponse lmkAudienceResponseforSec2OP = new LmkAudienceResponse();
        lmkAudienceResponseforSec2OP.setChannelNo(133);
        lmkAudienceResponseforSec2OP.setDemoNo(16);
        lmkAudienceResponseforSec2OP.setWeekPart(weekPart);
        lmkAudienceResponseforSec2OP.setDayPart("OP");
        lmkAudienceResponseforSec2OP.setAvgRatings(0);
        lmkAudienceResponseforSec2OP.setAvgImpacts(0);

        LmkAudienceResponse lmkAudienceResponseforSec2PT = new LmkAudienceResponse();
        lmkAudienceResponseforSec2PT.setChannelNo(133);
        lmkAudienceResponseforSec2PT.setDemoNo(16);
        lmkAudienceResponseforSec2PT.setWeekPart(weekPart);
        lmkAudienceResponseforSec2PT.setDayPart("PT");
        lmkAudienceResponseforSec2PT.setAvgRatings(0);
        lmkAudienceResponseforSec2PT.setAvgImpacts(0);
        lmkAudienceResponseList.add(lmkAudienceResponseforPriOP);
        lmkAudienceResponseList.add(lmkAudienceResponseforPriPT);
        lmkAudienceResponseList.add(lmkAudienceResponseforSec1OP);
        lmkAudienceResponseList.add(lmkAudienceResponseforSec1PT);
        lmkAudienceResponseList.add(lmkAudienceResponseforSec2OP);
        lmkAudienceResponseList.add(lmkAudienceResponseforSec2PT);

        return lmkAudienceResponseList.toArray(new LmkAudienceResponse[0]);

    }

    private List<Demo> createDemoList(){
        List<Demo> demoList = new ArrayList<>();
        Demo demo = new Demo();
        demo.setId(14);
        demo.setSegment("Mass");
        demoList.add(demo);
        return demoList;
    }

    private AffinityResponse createAffinityResponse(){
        AffinityResponse affinityResponse = new AffinityResponse();
        List<UniverseData> universeDataList = createUniverseDataList();
        List<DemoAverage> demoAverageList = createDemoAverageList();
        List<AffinityChannel> affinityChannelList = createAffinityChannelList();
        AffinityRequest affinityRequest = createAffinityRequest();
        affinityResponse.setTerritory(affinityRequest.getTerritory());
        affinityResponse.setPlatform(affinityRequest.getPlatform());
        affinityResponse.setPanel(affinityRequest.getPlatform());
        affinityResponse.setPriDemos(affinityRequest.getPrimaryDemos());
        affinityResponse.setSecDemos(affinityRequest.getSecondaryDemos());
        affinityResponse.setDayparts(affinityRequest.getDayParts());
        affinityResponse.setUniverses(universeDataList);
        affinityResponse.setDemoAverage(demoAverageList);
        affinityResponse.setChannels(affinityChannelList);

        return affinityResponse;
    }

    private List<AffinityChannel> createAffinityChannelList() {
        List<AffinityChannel> affinityChannelList = new ArrayList<>();
        List<String> genreList = Collections.singletonList("General Entertainment");
        List<AffinityDemo> affinityDemoList = createAffinityDemoList();
        AffinityChannel affinityChannel = new AffinityChannel();
        affinityChannel.setId(7);
        affinityChannel.setName("1 MAGIC");
        affinityChannel.setTier("Compact +");
        affinityChannel.setNetwork("Local Interest Channels");
        affinityChannel.setGenre(genreList);
        affinityChannel.setDemos(affinityDemoList);
        return affinityChannelList;

    }



    private List<DemoAverage> createDemoAverageList() {
        List<DemoAverage> demoAverageList = new ArrayList<>();
        DemoAverage demoAverage = new DemoAverage(14,15529.795404827617);
        demoAverageList.add(demoAverage);
        return demoAverageList;
    }

    private List<UniverseData> createUniverseDataList() {
        List<UniverseData> universeDataList = new ArrayList<>();
        UniverseData universeDataPri = new UniverseData();
        universeDataPri.setDemoNo(14);
        universeDataPri.setAverage(15581333);
        UniverseData universeDataSec1 = new UniverseData();
        universeDataSec1.setDemoNo(15);
        universeDataSec1.setAverage(15581222);
        UniverseData universeDataSec2 = new UniverseData();
        universeDataSec2.setDemoNo(16);
        universeDataSec2.setAverage(15581111);

        universeDataList.add(universeDataPri);
        universeDataList.add(universeDataSec1);
        universeDataList.add(universeDataSec2);
        return universeDataList;
    }

    private List<AffinityDemo> createAffinityDemoList(){
        List<AffinityDemo> affinityDemoList = new ArrayList<>();
        List<DaypartData> daypartDataList = createDayPartDataList();
        AffinityDemo affinityDemoPri = new AffinityDemo();
        affinityDemoPri.setId(14);
        affinityDemoPri.setDaypart(daypartDataList);
        affinityDemoPri.setImpact(0);
        affinityDemoPri.setRating(0);
        affinityDemoPri.setAffinityIndex(0);
        affinityDemoPri.setReachIndex(0);
        affinityDemoPri.setAffinityReachIndex(0);
        affinityDemoList.add(affinityDemoPri);
        AffinityDemo affinityDemoSec1 = new AffinityDemo();
        affinityDemoSec1.setId(15);
        affinityDemoSec1.setDaypart(daypartDataList);
        affinityDemoSec1.setImpact(0);
        affinityDemoSec1.setRating(0);
        affinityDemoSec1.setAffinityIndex(0);
        affinityDemoSec1.setReachIndex(0);
        affinityDemoSec1.setAffinityReachIndex(0);
        affinityDemoList.add(affinityDemoSec1);
        AffinityDemo affinityDemoSec2 = new AffinityDemo();
        affinityDemoSec2.setId(15);
        affinityDemoSec2.setDaypart(daypartDataList);
        affinityDemoSec2.setImpact(0);
        affinityDemoSec2.setRating(0);
        affinityDemoSec2.setAffinityIndex(0);
        affinityDemoSec2.setReachIndex(0);
        affinityDemoSec2.setAffinityReachIndex(0);
        affinityDemoList.add(affinityDemoSec2);


        return affinityDemoList;
    }

    private List<DaypartData> createDayPartDataList(){
        List<DaypartData> daypartDataList = new ArrayList<>();
        DaypartData daypartDataForOP = new DaypartData("OP",0.0,0.0);
        DaypartData daypartDataForPT = new DaypartData("PT",0.0,0.0);
        daypartDataList.add(daypartDataForOP);
        daypartDataList.add(daypartDataForPT);
        return daypartDataList;
    }

    private AffinityRequest createAffinityRequest(){
        List<Integer> primaryTargetMarket = Collections.singletonList(14);
        List<Integer> secondaryTargetMarket = Arrays.asList(15,16);
        List<String> dayParts = Arrays.asList("OP","PT");
        return new AffinityRequest(1,1,1,primaryTargetMarket,secondaryTargetMarket,"2019-01-01","2019-03-31",weekPart,dayParts);

    }

    private LengthFactor createLengthFactor(){
        LengthFactor lengthFactor = new LengthFactor();
        lengthFactor.setPriceFactor(180);
        return lengthFactor;
    }


    private RateCardInput createRateCardInput(){
        RateCardInput rateCardInput = new RateCardInput();
        rateCardInput.setTitle("June 2020");
        rateCardInput.setStart_date("2020-06-01");
        return rateCardInput;
    }

    private RateCards createRateCards(){
        RateCards rateCards = new RateCards();
        rateCards.setId(1);
        rateCards.setTitle("June 2020");
        rateCards.setStartDate(Date.valueOf("2020-06-01"));
        rateCards.setStatus("Approved");
        return rateCards;
    }
    private List<SellOutInfo> createSelloutInfoList() {
        List<SellOutInfo> sellOutInfoList = new ArrayList<>();
        SellOutInfo sellOutInfo = new SellOutInfo();
        sellOutInfo.setId(1);
        sellOutInfo.setChannel_name(channelName);
        sellOutInfo.setDaypart(dayPart);
        sellOutInfo.setPercentage(45.23);
        sellOutInfoList.add(sellOutInfo);
        return sellOutInfoList;
    }

    private PriceingRatesResponse createPricingRatesResponse() {
        List<Rates> ratesList = createRateList();
        return new PriceingRatesResponse(ratesList.size(), ratesList);

    }

    private List<Rates> createRateList() {
        List<Rates> ratesList = new ArrayList<>();
        Rates rates = new Rates();
        rates.setId(1);
        rates.setName(channelName);
        rates.setRates(createRateDayPartList());
        ratesList.add(rates);
        return ratesList;

    }

    private List<RateDayPart> createRateDayPartList() {
        List<RateDayPart> rateDayPartList = new ArrayList<>();
        RateDayPart rateDayPart = new RateDayPart();
        rateDayPart.setDaypart(dayPart);
        rateDayPart.setRate_actual(20);
        rateDayPart.setRate_forecast(100);
        rateDayPartList.add(rateDayPart);
        return rateDayPartList;
    }

    private PriceingSelloutResponse createPricingSelloutResponse() {
        List<SellOut> sellOutList = createSelloutList();
        PriceingSelloutResponse priceingSelloutResponse = new PriceingSelloutResponse();
        priceingSelloutResponse.setTotal(sellOutList.size());
        priceingSelloutResponse.setItems(sellOutList);
        return priceingSelloutResponse;
    }

    private List<SellOut> createSelloutList() {
        List<SellOut> sellOutsList = new ArrayList<>();
        SellOut sellOut = new SellOut();
        sellOut.setId(1);
        sellOut.setName(channelName);
        sellOut.setRates(createSellOutDayPartList());
        sellOutsList.add(sellOut);
        return sellOutsList;
    }

    private List<SellOutDaypart> createSellOutDayPartList() {
        List<SellOutDaypart> sellOutDaypartList = new ArrayList<>();
        SellOutDaypart sellOutDaypart = new SellOutDaypart();
        sellOutDaypart.setDaypart(dayPart);
        sellOutDaypart.setSo(45.23);
        sellOutDaypartList.add(sellOutDaypart);
        return sellOutDaypartList;

    }

    private List<RateInfo> createRateInfoList() {
        List<RateInfo> rateInfoList = new ArrayList<>();
        RateInfo rateInfo = new RateInfo();
        rateInfo.setId(1);
        rateInfo.setChannel_name(channelName);
        rateInfo.setDay_part(dayPart);
        rateInfo.setRate_actual(20);
        rateInfo.setRate_forecast(100);

        rateInfoList.add(rateInfo);
        return rateInfoList;
    }
    
    private CatalogResponse catalogMockResp() {
        CatalogResponse response = new CatalogResponse();
        List<Catalog> cList = createCatalog();
        response.setTotal(1);
        response.setItems(cList);
        return response;
    }

    private List<Catalog> createCatalog() {
        Catalog c = new Catalog();
        List<Catalog> cList = new ArrayList<Catalog>();
        CatalogDaypart daypart = new CatalogDaypart();
        Plans plans = new Plans();
        SalesArea salesArea = new SalesArea();
        List<CatalogDaypart> daypartList = new ArrayList<>();
        List<Plans> plansList = new ArrayList<>();
        List<SalesArea> salesAreaList = new ArrayList<>();
        c.setId(1);
        c.setTitle1("Ziyachisa");
        c.setTitle2("Mass Market");
        c.setShortDescr(
                "TV is a family event & viwership is live. Quality & a varied viewing experience are the key drivers to the platform.");
        c.setType("spotbundle");
        c.setGraphic("");
        c.setLogo("");
        daypart.setCode("PT");
        daypart.setTitle("Prime Time");
        daypart.setPercentage(60);
        daypart.setAverage(true);
        daypartList.add(daypart);
        daypart.setCode("OP");
        daypart.setTitle("Off Peak");
        daypart.setPercentage(40);
        daypart.setAverage(false);
        daypartList.add(daypart);

        c.setDaypart(daypartList);
        c.setExclude("Mzansi Wethu - Off Peak Only");

        plans.setWeeks(4);
        plans.setSpotRate(813);
        plans.setSpots(480);
        plans.setSpotLength(30);
        plans.setRate(390000);
        plans.setBusinessType(55);

        salesArea.setSalesArea(105);
        salesArea.setName("TRACE TV");
        salesArea.setSpots(96);

        salesAreaList.add(salesArea);

        plans.setSalesAreaList(salesAreaList);
        plansList.add(plans);

        c.setRate(55000);
        c.setViews(733000);
        c.setCpt(75);
        c.setTvr(24);
        c.setCpp(7684);
        c.setPlans(plansList);
        cList.add(c);
        return cList;
    }
    
    private CampaignApproveReject createCampaignApprovalReject() {
        CampaignApproveReject campaignApproveReject = new CampaignApproveReject();
        campaignApproveReject.setPortalId(1);
        campaignApproveReject.setUserName("user");
        campaignApproveReject.setReason("No Spots available");
        return campaignApproveReject;
    }



    private List<DocDetail> createDocDetailList(){
        DocDetail docDetail = new DocDetail();
        docDetail.setDoc_id(1);
        docDetail.setFileName(awsFileName);
        docDetail.setS3FileName(awsFileName);
        docDetail.setFileType(type);
        docDetail.setFolderName(folderName);

        return Collections.singletonList(docDetail);

    }

    private JWTExtract createJWTResponse(){
        JWTExtract response = new JWTExtract();
        response.setUserId(1);
        response.setUserRole("Media Planner");
        return response;
    }

    private SalesAreaLMKResponse createSalesAreaLMKResponse() {
        SalesAreaLMKResponse salesAreaLMKResponse = new SalesAreaLMKResponse();
        SalesAreaLMK salesAreaLMK = new SalesAreaLMK();
        salesAreaLMK.setSalesAreaNumber(83);
        salesAreaLMK.setName("M-NET CHANNEL");
        List<SalesAreaLMK> salesAreaLMKList = Collections.singletonList(salesAreaLMK);
        salesAreaLMKResponse.setTotal(1);
        salesAreaLMKResponse.setItems(salesAreaLMKList);
        return salesAreaLMKResponse;
    }

    private UpdateCampaignRequest createUpdateCampaignRequest() {
        UpdateCampaignRequest updateCampaignRequest = new UpdateCampaignRequest();
        updateCampaignRequest.setPortalId(1);
        CampaignRequest campaignRequest = createCampaignRequest();
        updateCampaignRequest.setUpdatePayload(campaignRequest);
        return updateCampaignRequest;
    }

    private UpdateCampaignResponse createUpdateSuccessResponse() {
        UpdateCampaignResponse updateCampaignResponse = new UpdateCampaignResponse();
        updateCampaignResponse.setPortalId(1);
        updateCampaignResponse.setStatus(HttpStatus.OK);
        updateCampaignResponse.setMessage(Constant.APPROVAL_COMPLETED);
        updateCampaignResponse.setMessageSeverity(Constant.SUCCESS);
        return updateCampaignResponse;
    }

    private CampaignList createCampaignList(Pageable pageable) {
        CampaignList campaignList = new CampaignList();
        List<Campaign> allCampaignList = new ArrayList<>();
        allCampaignList.add(createMockCampaign());
        Page<Campaign> campaignPage = new PageImpl<>(allCampaignList, pageable, 1);
        campaignList.setAllList(new PageDecorator<>(campaignPage));
        campaignList.setActiveList(new PageDecorator<>(campaignPage));
        campaignList.setCancelledList(new PageDecorator<>(campaignPage));
        campaignList.setDraftList(new PageDecorator<>(campaignPage));
        campaignList.setPostList(new PageDecorator<>(campaignPage));
        campaignList.setPendingList(new PageDecorator<>(campaignPage));
        return campaignList;
    }

    private CampaignCancelResp createCampaignCancelResponse() {
        CampaignCancelResp campaignCancelResp = new CampaignCancelResp();
        List<UploadCampaignResult> uploadCampaignResultList = createUploadCampaignResult();
        uploadCampaignResultList.get(0).setCampaignStatus("CANCELLED");
        campaignCancelResp.setUploadCampaignResult(uploadCampaignResultList);
        return campaignCancelResp;
    }

    private AvailLMKRequest createAvailLmkRequest() {
        int[] targetMarket = {15};
        AvailLMKRequest availLMKRequest = new AvailLMKRequest();
        availLMKRequest.setStartDate("2020-02-20");
        availLMKRequest.setEndDate("2020-02-21");
        availLMKRequest.setSalesArea(44);
        availLMKRequest.setTargetMarkets(targetMarket);
        return availLMKRequest;
    }

    private AvailLMKResponse createAvailResponse() {
        AvailLMKResponse availLMKResponse = new AvailLMKResponse();
        availLMKResponse.setTotal("1");
        availLMKResponse.setType("cherrypick");
        availLMKResponse.setBusinessType(16);

        List<AvailItemLMKResponse> availItemLMKResponseList = new ArrayList<>();

        TargetMarketValues targetMarketValues = new TargetMarketValues();
        targetMarketValues.setId(15);
        targetMarketValues.setImpact(0);
        targetMarketValues.setRating(0);
        targetMarketValues.setCpp(0);
        targetMarketValues.setCpt(0);

        List<TargetMarketValues> targetMarketValuesList = Collections.singletonList(targetMarketValues);

        AvailItemLMKResponse availItemLMKResponse = new AvailItemLMKResponse();
        availItemLMKResponse.setSales_area_no(44);
        availItemLMKResponse.setSales_area_name("BBC BRIT");
        availItemLMKResponse.setDemos(targetMarketValuesList);
        availItemLMKResponse.setPrice(10000);
        availItemLMKResponse.setProgram_no(452630);
        availItemLMKResponse.setProgram_name("T.B.A");
        availItemLMKResponse.setStart_time(63613);
        availItemLMKResponse.setEnd_time(73224);
        availItemLMKResponse.setRating(0);
        availItemLMKResponse.setImpact(0);
        availItemLMKResponse.setCpp(0);
        availItemLMKResponse.setCpt(0);
        availItemLMKResponse.setScheduled_on("2020-00-20");
        availItemLMKResponse.setScheduled_time(70728);
        availItemLMKResponse.setDay("Sun");
        availItemLMKResponse.setUniverse(0);

        availItemLMKResponseList.add(availItemLMKResponse);
        availLMKResponse.setItems(availItemLMKResponseList);
        return availLMKResponse;
    }

    private Campaign createMockCampaign() {
        Campaign campaign = new Campaign();
        campaign.setPortal_id(1);
        campaign.setTitle(campaignName);
        List<Integer> targetMarkets = new ArrayList<>();
        targetMarkets.add(15);
        targetMarkets.add(16);
        targetMarkets.add(58);
        campaign.setTarget_markets(targetMarkets);
        campaign.setDeal_number(520970);
        campaign.setProduct_code(5970);
        campaign.setAdvertiser_code(advertiserCode);
        campaign.setObjective(campaignObjective);
        campaign.setPanel("TAMS");
        campaign.setBudget(100);
        campaign.setRating(33.33);
        campaign.setStatus(3);
        campaign.setStart(campaignStart);
        campaign.setEnd(campaignEnd);
        campaign.setSource_start(campaignStart);
        campaign.setSource_end(campaignEnd);
        List<Items> itemsList = new ArrayList<>();
        Items items = new Items();
        items.setApprovalKeyID(1);
        items.setType("gtt");
        items.setStart(campaignStart);
        items.setEnd(campaignEnd);
        items.setRate(185000);
        items.setCampaign_code(1);
        List<CampaignSalesArea> campaignSalesAreaList = new ArrayList<>();
        CampaignSalesArea campaignSalesArea = new CampaignSalesArea();
        campaignSalesAreaList.add(campaignSalesArea);
        items.setSalesAreaOnCampaign(campaignSalesAreaList);
        itemsList.add(items);
        campaign.setItems(itemsList);

        return campaign;

    }

    private CampaignResponseList createCampaignResponseList() {
        CampaignResponseList campaignResponseList = new CampaignResponseList();
        List<CampaignResponse> campaignResponses = new ArrayList<>();
        campaignResponses.add(createCampaignResponse());
        campaignResponseList.setTotal(campaignResponses.size());
        campaignResponseList.setItems(campaignResponses);
        campaignResponseList.setStatus(HttpStatus.OK);
        return campaignResponseList;
    }

    private CampaignResponse createCampaignResponse() {
        CampaignResponse campaignResponse = new CampaignResponse();
        campaignResponse.setStatus(HttpStatus.OK);
        campaignResponse.setMessage("Approval completed");
        campaignResponse.setCampaignCode(330824);
        campaignResponse.setUploadCampaignResult(createUploadCampaignResult());
        return campaignResponse; }

    private List<UploadCampaignResult> createUploadCampaignResult() {
        List<UploadCampaignResult> uploadCampaignResultList = new ArrayList<>();
        UploadCampaignResult uploadCampaignResult = new UploadCampaignResult();
        uploadCampaignResult.setApprovalKeyID(1);
        uploadCampaignResult.setCampaignCode(330824);
        uploadCampaignResult.setCampaignStatus("OK");
        uploadCampaignResult.setCampaignIntendedAction("Post");
        uploadCampaignResult.setResultMessages(createResultMessageList());
        uploadCampaignResultList.add(uploadCampaignResult);
        return uploadCampaignResultList;
    }

    private ResultMessageList createResultMessageList() {
        ResultMessageList resultMessageList = new ResultMessageList();
        List<ResultMessages> resultMessagesList = new ArrayList<>();
        ResultMessages resultMessages = new ResultMessages();
        resultMessages.setMessageCode("0");
        resultMessages.setMessageSeverity("Warning");
        resultMessages.setMessageText("Greater");
        resultMessagesList.add(resultMessages);

        resultMessageList.setResultMessage(resultMessagesList);
        return resultMessageList;
    }

    private ResultMessageList createErrorResultMessageList() {
        ResultMessageList resultMessageList = new ResultMessageList();
        List<ResultMessages> resultMessagesList = new ArrayList<>();
        ResultMessages resultMessages = new ResultMessages();
        resultMessages.setMessageCode("0");
        resultMessages.setMessageSeverity("Error");
        resultMessages.setMessageText("Greater");
        resultMessagesList.add(resultMessages);

        resultMessageList.setResultMessage(resultMessagesList);
        return resultMessageList;
    }

    private CampaignRequest createCherryPickCampaignRequest() {
        List<Items> itemsList = createItemList();
        CampaignRequest campaignRequest = new CampaignRequest();
        campaignRequest.setTitle(campaignName);
        int[] targetMarkets = { 15, 16, 58 };
        campaignRequest.setTarget_markets(targetMarkets);
        campaignRequest.setDeal_number(520970);
        campaignRequest.setProduct_code(5970);
        campaignRequest.setAdvertiser_code(advertiserCode);
        campaignRequest.setObjective(campaignObjective);
        campaignRequest.setPanel("TAMS");
        campaignRequest.setBudget(100);
        campaignRequest.setRating(33.33);
        campaignRequest.setStart("2020-05-15");
        campaignRequest.setEnd("2020-05-30");
        campaignRequest.setSource_start(campaignStart);
        campaignRequest.setSource_end(campaignEnd);
        campaignRequest.setItems(itemsList);

        return campaignRequest;
    }

    private List<Items> createItemList() {
        List<Spots> spotsList = createSpotList();
        List<Items> itemsList = new ArrayList<>();
        Items items =new Items();
        items.setType("cherrypick");
        items.setOptimization(null);
        items.setSpot_length(30);
        items.setBusiness_type(16);
        items.setTarget_market(15);
        items.setSpotList(spotsList);
        itemsList.add(items);

        return itemsList;
    }

    private List<Spots> createSpotList() {
        List<Spots> spotsList = new ArrayList<>();
        Spots spots = new Spots();
        spots.setSales_area_name("M-Net Channel");
        spots.setPrice(1000);
        spots.setProgram_name("9-1-1: LONE STAR 01");
        spots.setStart_time(800);
        spots.setEnd_time(859);
        spots.setRating(0);
        spots.setImpact(1115);
        spots.setCpp(0);
        spots.setCpt(897);
        spots.setDay("Sun");
        spots.setScheduled_on("01/03/2020");
        spots.setDuration(30);
        spots.setSpots(1);

        spotsList.add(spots);

        return spotsList;

    }

    private Map<String, Object> createDeleteResponse() {
        Map<String, Object> response = new HashMap<>();
        response.put(campaignStatus, Boolean.TRUE);
        response.put("Message", "Deleted successfully");
        return response;
    }

    private CampaignRequest createCampaignRequest() {
        CampaignRequest campaignRequest = new CampaignRequest();
        campaignRequest.setTitle(campaignName);
        int[] targetMarkets = { 15, 16, 58};
        campaignRequest.setTarget_markets(targetMarkets);
        campaignRequest.setDeal_number(520970);
        campaignRequest.setProduct_code(5970);
        campaignRequest.setAdvertiser_code(advertiserCode);
        campaignRequest.setObjective(campaignObjective);
        campaignRequest.setPanel("TAMS");
        campaignRequest.setBudget(100);
        campaignRequest.setRating(33.33);
        campaignRequest.setStart(campaignStart);
        campaignRequest.setEnd(campaignEnd);
        campaignRequest.setSource_start(campaignStart);
        campaignRequest.setSource_end(campaignEnd);
        List<CampaignDaypart> dayPartList = new ArrayList<>();
        CampaignDaypart dayPart = new CampaignDaypart();
        dayPart.setId(1);
        dayPart.setTitle("PRIME");
        dayPartList.add(dayPart);
        List<Items> itemsList =new ArrayList<>();
        Items items = new Items();
        items.setType("gtt");
        items.setStart(campaignStart);
        items.setEnd(campaignEnd);
        items.setRate(185000);
        items.setDaypart(dayPartList);
        itemsList.add(items);
        List<CampaignSalesArea> campaignSalesAreaList = new ArrayList<>();
        CampaignSalesArea campaignSalesArea = new CampaignSalesArea();
        campaignSalesAreaList.add(campaignSalesArea);
        items.setSalesAreaOnCampaign(campaignSalesAreaList);
        campaignRequest.setItems(itemsList);

        return campaignRequest;
    }

}
