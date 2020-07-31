package com.dms.ptp;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.ptp.campaign.CampaignApplication;
import com.ptp.campaign.controller.CampaignController;
import com.ptp.campaign.controller.DocumentController;
import com.ptp.campaign.dao.*;
import com.ptp.campaign.exception.CampaignNotFoundException;
import com.ptp.campaign.exception.InvalidParamException;
import com.ptp.campaign.model.*;
import com.ptp.campaign.service.CampaignService;
import com.ptp.campaign.service.DocService;
import com.ptp.campaign.service.impl.CampaignServiceImpl;
import com.ptp.campaign.service.impl.DocServiceImpl;
import com.ptp.campaign.util.Constants;
import com.ptp.campaign.util.EmailUtil;
import com.ptp.campaign.util.JWTUtil;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.mail.MessagingException;
import javax.persistence.PersistenceException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = CampaignApplication.class)
public class CampaignServiceTest {

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
    RestTemplate mockRestTemplate;

    @Mock
    JWTUtil mockJWTUtil;

    @Mock
    EmailUtil mockEmailUtil;

    String token ="eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOsuffixiIxNzAiLCJpZCI6MTcwLCJyb2xlIjoiTWVkaWEgUGxhbm5lciIsImlhdCI6MTU5NDEwNDcxOSwiZXhwIjoxNTk0NzA5NTE5fQ.E8f_R42uAGwwgCOge5HmDEIpSweKF9E4b4Hw_mDwX-RKO_tGyYcSk-MtrsbRZj1m4CZrj8rQBFr_I8LAtuV0Ig";
    String awsFileName = "test contract.pdf";
    String folderName = "1593890897343-1";
    String bucketName = "ptp.test";
    String suffix = "/";
    String filePath = folderName+suffix+awsFileName;
    String advertiserCode = "A0001";
    String adminUsername = "arnabashutosh.d@tataelxsi.co.in";
    String status = "status";
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
        Mockito.when(mockCampaignService.createCampaign(campaignRequest, Constants.ACTION_DRAFT,token)) .thenReturn(campaignResponse);
        ResponseEntity<CampaignResponseList> responseEntity = campaignController.createCampaign(campaignRequest, Constants.ACTION_DRAFT,token);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void postCampaignError() throws MessagingException, IOException, TemplateException {
        CampaignRequest campaignRequest = createCampaignRequest();
        Mockito.when(mockCampaignService.createCampaign(campaignRequest, Constants.ACTION_DRAFT,token)).thenThrow(PersistenceException.class);
        ResponseEntity<CampaignResponseList> responseEntity = campaignController.createCampaign(campaignRequest, Constants.ACTION_DRAFT,token);
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
        CampaignResponseList campaignResponse = campaignService.createCampaign(campaignRequest, Constants.ACTION_DRAFT, token);
        CampaignResponse response = campaignResponse.getItems().get(0);
        assertEquals(1, campaignResponse.getTotal());
        //assertEquals(HttpStatus.OK, response.getStatus());
    }

    @Test
    public void createCampaignError() throws MessagingException, IOException, TemplateException {
        Mockito.when(mockSeedRepo.findByIdFromSeed()).thenThrow(PersistenceException.class);
        doNothing().when(mockEmailUtil).sendMailToDMSAdmin(adminUsername);
        CampaignResponseList campaignResponse = campaignService.createCampaign(createCampaignRequest(), Constants.ACTION_DRAFT,token);
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
        //assertEquals(HttpStatus.OK, response.getStatus());

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
        //assertEquals(HttpStatus.OK, response.getStatus());

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
        //assertEquals(HttpStatus.OK, response.getStatus());

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
        assertEquals(mockDeleteResponse.get(status), deleteResponse.get(status));
    }

    @Test
    public void deleteCampaignServiceTest() {
        Map<String, Object> mockDeleteResponse = createDeleteResponse();
        Campaign mockCampaign = createMockCampaign();
        when(mockCampaignRepo.findById(1)).thenReturn(java.util.Optional.of(mockCampaign));
        doNothing().when(mockCampaignRepo).delete(mockCampaign);
        Map<String, Object> deleteResponse = campaignService.deleteCampaign(1);
        assertNotNull(deleteResponse);
        assertEquals(deleteResponse.get(status), mockDeleteResponse.get(status));
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

    @Ignore
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

    @Ignore
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

    @Ignore
    @Test(expected = PersistenceException.class)
    public void updateCampaignServiceExceptionTest() {
        UpdateCampaignRequest mockUpdateCampaignRequest = createUpdateCampaignRequest();
        Campaign mockCampaign = createMockCampaign();
        when(mockCampaignRepo.findById(1)).thenReturn(Optional.of(mockCampaign));
        when(mockCampaignRepo.save(any(Campaign.class))).thenThrow(PersistenceException.class);
        campaignService.updateCampaign(mockUpdateCampaignRequest);

    }

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
        jwtExtract.setUserRole(Constants.ROLE_CONTRACTOR);
        UpdateCampaignRequest mockUpdateCampaignRequest = createUpdateCampaignRequest();
        Campaign mockCampaign =createMockCampaign();
        when(mockJWTUtil.getIdRoleFromToken(token)).thenReturn(jwtExtract);
        when(mockCampaignRepo.findById(1)).thenReturn(Optional.of(mockCampaign));
        when(mockCampaignRepo.save(any(Campaign.class))).thenThrow(PersistenceException.class);
        campaignService.resubmitCampaign(mockUpdateCampaignRequest,token);
    }

    
    @Test
    public void getTargetMarketDetailsPackageTest() throws ParseException, InvalidParamException {
    	TargetMarketPkgReq multipleTargetMarketReq = createTargetMarketPkgReq();
    	TargetMarketLMKResp resp = new TargetMarketLMKResp();
        when(mockCampaignService.getTargetMarketDetailsPackage(multipleTargetMarketReq)).thenReturn(resp);
        ResponseEntity<TargetMarketLMKResp> responseEntity = campaignController.getTargetMarketDetailsPackage(multipleTargetMarketReq);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
    
    @Test(expected=InvalidParamException.class)
    public void getTargetMarketDetailsPackageErrorTest() throws ParseException, InvalidParamException {
    	TargetMarketPkgReq multipleTargetMarketReq = createTargetMarketPkgReq();
        when(mockCampaignService.getTargetMarketDetailsPackage(multipleTargetMarketReq)).thenThrow(PersistenceException.class);
        ResponseEntity<TargetMarketLMKResp> responseEntity = campaignController.getTargetMarketDetailsPackage(multipleTargetMarketReq);
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
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
        updateCampaignResponse.setMessage(Constants.APPROVAL_COMPLETED);
        updateCampaignResponse.setMessageSeverity(Constants.SUCCESS);
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
    
    private TargetMarketPkgReq createTargetMarketPkgReq() {
        int[] targetMarket = {15,14};
        int[] channels = {81,72,143,210,24};
        TargetMarketPkgReq targetMarketPkgReq = new TargetMarketPkgReq();
        TargetMarketPackages packages = new TargetMarketPackages();
        List<TargetMarketPackages> packagesList = new ArrayList<TargetMarketPackages>();
        
        packages.setCatalogId(1);
        packages.setStartDate("2020-02-20");
        packages.setEndDate("2020-02-21");
        packages.setSpotPrice(0);
        packages.setChannels(channels);
        targetMarketPkgReq.setPackages(packagesList);
        
        targetMarketPkgReq.setTargetMarkets(targetMarket);
        return targetMarketPkgReq;
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
        response.put(status, Boolean.TRUE);
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
        List<DayPart> dayPartList = new ArrayList<>();
        DayPart dayPart = new DayPart();
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
