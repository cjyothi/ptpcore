package com.dms.ptp.serviceimplementation;

import com.dms.ptp.dto.*;
import com.dms.ptp.entity.Aff;
import com.dms.ptp.entity.AffChannelDayparts;
import com.dms.ptp.entity.AffChannelDemos;
import com.dms.ptp.entity.AffChannels;
import com.dms.ptp.entity.AffDemos;
import com.dms.ptp.entity.AffUniverse;
import com.dms.ptp.entity.Baseline;
import com.dms.ptp.entity.Channel;
import com.dms.ptp.entity.Demo;
import com.dms.ptp.exception.BaselineNotFoundException;
import com.dms.ptp.exception.InvalidParamException;
import com.dms.ptp.repository.AffinityRepository;
import com.dms.ptp.repository.BaselineRepository;
import com.dms.ptp.repository.ChannelDemoRepository;
import com.dms.ptp.repository.DemoRepository;
import com.dms.ptp.response.BaselineListResponse;
import com.dms.ptp.response.BaselineResponse;
import com.dms.ptp.response.SaveResponse;
import com.dms.ptp.service.AffinityReachService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AffinityReachServiceImplementation implements AffinityReachService {

    RestTemplate restTemplate= new RestTemplate();
    
    static Logger logger = LoggerFactory.getLogger(AffinityReachServiceImplementation.class);

    @Autowired
    ChannelDemoRepository channelDemoRepository;

    @Autowired
    DemoRepository demoRepository;
    
    @Autowired
    BaselineRepository baselineRepository;
    
    @Autowired
    AffinityRepository affinityRepository;


    @Override
    public AffinityResponse getAffinityReachCalculation(AffinityRequest affinityRequest) {
        AffinityResponse affinityResponse = new AffinityResponse();
        AffinityRequest affinityRequestCopy = new AffinityRequest(affinityRequest.getTerritory(),affinityRequest.getPlatform(),
                affinityRequest.getPanel(),affinityRequest.getPrimaryDemos(),affinityRequest.getSecondaryDemos(),affinityRequest.getSourceStart(),affinityRequest.getSourceEnd(),
                affinityRequest.getWeekParts(),affinityRequest.getDayParts());

        // Demo List
        List<Integer> demoList = new ArrayList<>();
        demoList.addAll(affinityRequestCopy.getPrimaryDemos());
        if (!affinityRequestCopy.getSecondaryDemos().isEmpty()) {
            demoList.addAll(affinityRequestCopy.getSecondaryDemos());
        }

        // Get the channel data by platform and territory
        // Get list of lmkrefno to pass to LMK API
        List<Channel> channelList = channelDemoRepository.findByTerritory_IdAndPlatform_Id(affinityRequestCopy.getTerritory(),affinityRequestCopy.getPlatform());
        List<InterimChannel> interimChannels = new ArrayList<>();
        List<Integer> lmkRefNoList= new ArrayList<>();
        channelList.forEach(channel-> {
            List<String> genreNames = new ArrayList<>();
            channel.getGenres().forEach(genre -> genreNames.add(genre.getName()));
            interimChannels.add(new InterimChannel(channel.getId(),channel.getName(),channel.getPackageName(),channel.getNetwork(),genreNames,channel.getLmkRefNo()));
            lmkRefNoList.add(channel.getLmkRefNo());
        });

        // Get Audience data by calling LMK API
        List<LmkAudienceResponse> lmkAudienceResponseList = getLmkAudienceData(lmkRefNoList,demoList,affinityRequestCopy);

        //Get universe data by calling LMK API

        UniverseData[] universeDataList=getUniverseData(affinityRequestCopy);


        // get the demo for prior List as list from DB
        List<Demo> demoDataList = demoRepository.findByIdIn(demoList);

        /************************** CACULATORS STARTS *******************************/

        // Get Secondary Demos as List
        List<Integer> secDemos = affinityRequestCopy.getSecondaryDemos();

        // Get the interim list which is having channel no, demo no and average of impacts
        List<InterimAudienceData> interimAudienceData = lmkAudienceResponseList.stream().collect(Collectors.groupingBy(x -> getGroupingByKey(x),
                Collectors.averagingDouble(LmkAudienceResponse::getAvgImpacts))).entrySet().stream().map(entry -> {
            List lst = entry.getKey();
            int i = (int) lst.get(0);
            int j = (int) lst.get(1);
            InterimAudienceData a = new InterimAudienceData(i, j, entry.getValue());
            return a;
        }).collect(Collectors.toList());

        //Map containing demo no and average of impacts across channels
        Map<Integer, Double> demoAvgMap = interimAudienceData.stream()
                .collect(Collectors.groupingBy(InterimAudienceData :: getDemoNo, Collectors.averagingDouble(InterimAudienceData :: getAvgImpact)));

        // Filtered interimAudienceData for only secondary demos
        List<InterimAudienceData> secDemoInterimAudienceData = new ArrayList<>();
        for (Integer sd:secDemos) {
            secDemoInterimAudienceData.addAll(interimAudienceData.stream().filter(l -> l.getDemoNo() == sd).collect(Collectors.toList())) ;
        }

        //Map containing channel lmkrefno and sum of impacts across secondary demos
        Map<Integer, Double> demoSumMap = secDemoInterimAudienceData.stream()
                .collect(Collectors.groupingBy(InterimAudienceData :: getChannelNo, Collectors.summingDouble(InterimAudienceData :: getAvgImpact)));

        //Map containing secondary demo no and map of channel no and calculated affinity value
        Map<Integer, Map<Integer, Double>> affinityMap = secDemoInterimAudienceData.stream().collect(
                Collectors.groupingBy(InterimAudienceData::getDemoNo, Collectors.toMap(InterimAudienceData::getChannelNo, x -> {
                    if(x.getAvgImpact()==0){
                        return 0.0;
                    }
                    double z = (x.getAvgImpact() / demoSumMap.get(x.getChannelNo()));
                    return z;
                })));

        //Map containing secondary demo no and map of channel no and calculated reach value
        Map<Integer, Map<Integer, Double>> reachMap = secDemoInterimAudienceData.stream().collect(
                Collectors.groupingBy(InterimAudienceData::getDemoNo, Collectors.toMap(InterimAudienceData::getChannelNo, x -> {
                    double z = (x.getAvgImpact() / demoAvgMap.get(x.getDemoNo()));
                    return z;
                })));

        //Map containing channel no and map of demo no and calculated affinityreach value
        Map<Integer, Map<Integer, Double>> affinityReachMap = new HashMap<Integer, Map<Integer, Double>>();
        affinityReachMap = secDemoInterimAudienceData.stream().collect(
                Collectors.groupingBy(InterimAudienceData::getChannelNo, Collectors.toMap(InterimAudienceData::getDemoNo, x -> {
                    double z = (affinityMap.get(x.getDemoNo()).get(x.getChannelNo()) * reachMap.get(x.getDemoNo()).get(x.getChannelNo()));
                    return z;
                })));

        // Get the interim list which is having channel no, demo no and average of ratings
        List<InterimRatingAvg> interimRatingAvg = lmkAudienceResponseList.stream().collect(Collectors.groupingBy(x -> getGroupingByKey(x),
                Collectors.averagingDouble(LmkAudienceResponse::getAvgRatings))).entrySet().stream().map(entry -> {
            List lst = entry.getKey();
            int i = (int) lst.get(0);
            int j = (int) lst.get(1);
            InterimRatingAvg a = new InterimRatingAvg(i, j, entry.getValue());
            return a;
        }).collect(Collectors.toList());

        //Filtered LMKAudienceResponse List for valid weekpart and daypart values
        List<LmkAudienceResponse> validAudienceDataList = lmkAudienceResponseList.stream().filter(l-> l.getWeekPart()!= null && l.getDayPart()!=null)
              .collect(Collectors.toList());


        /************************** CACULATORS ENDS *******************************/

        // set inputs from UI
          affinityResponse.setTerritory(affinityRequest.getTerritory());
          affinityResponse.setPlatform(affinityRequest.getPlatform());
          affinityResponse.setPanel(affinityRequest.getPanel());
          affinityResponse.setPriDemos(affinityRequest.getPrimaryDemos());
          affinityResponse.setSecDemos(affinityRequest.getSecondaryDemos());
          affinityResponse.setSourceStart(affinityRequest.getSourceStart());
          affinityResponse.setSourceEnd(affinityRequest.getSourceEnd());
          affinityResponse.setDayparts(affinityRequest.getDayParts());

        // set universe list by calling LMK universe api
          affinityResponse.setUniverses(Arrays.asList(universeDataList));

        // set demo average
        List<DemoAverage> demoAverageList = demoAvgMap.entrySet().stream()
                .map(e -> new DemoAverage(e.getKey(), e.getValue())).collect(Collectors.toList());
        affinityResponse.setDemoAverage(demoAverageList);

        // set channels data from channel list and audience list with all calculations
        List<AffinityChannel> channels = new ArrayList<AffinityChannel>();


        for(InterimChannel c : interimChannels){

            AffinityChannel channel = new AffinityChannel();
            channel.setId(c.getId());
            channel.setName(c.getName());
            channel.setTier(c.getTier());
            channel.setNetwork(c.getNetwork());
            channel.setGenre(c.getGenre());

            List<AffinityDemo> affinityDemos = new ArrayList<AffinityDemo>();

            for(Integer demoId : demoList){

                AffinityDemo demoData = new AffinityDemo();
                demoData.setId(demoId);

                //Filtered LMKAudienceResponse List for only daypart = OP
                List<LmkAudienceResponse> channelAudienceOP = validAudienceDataList.stream().filter(al -> al.getChannelNo() == c.getLmkRefNo()
                        && al.getDemoNo() == demoId && al.getDayPart().equalsIgnoreCase("OP"))
                        .collect(Collectors.toList());

                //Filtered LMKAudienceResponse List for only daypart = PT
                List<LmkAudienceResponse> channelAudiencePT = validAudienceDataList.stream()
                        .filter(al -> al.getChannelNo() == c.getLmkRefNo() && al.getDemoNo() == demoId && al.getDayPart().equalsIgnoreCase("PT"))
                        .collect(Collectors.toList());

                //List of avgImpacts for particular channel and demo
                List<InterimAudienceData> interimAudienceList = interimAudienceData.stream()
                        .filter(id -> id.getChannelNo() == c.getLmkRefNo() && id.getDemoNo() == demoId)
                        .collect(Collectors.toList());

                //List of avgRatings for particular channel and demo
                List<InterimRatingAvg> interimRatingList = interimRatingAvg.stream()
                        .filter(id -> id.getChannelNo() == c.getLmkRefNo() && id.getDemoNo() == demoId)
                        .collect(Collectors.toList());

                List<DaypartData> dayPartList = new ArrayList<DaypartData>();
                //Setting DaypartData for daypart = "OP"
                if(null!=channelAudienceOP && channelAudienceOP.size()>0){
                    LmkAudienceResponse ad = channelAudienceOP.get(0);
                    DaypartData daypartData = new DaypartData();
                    daypartData.setId("OP");
                    daypartData.setImpact(ad.getAvgImpacts());
                    daypartData.setRating(ad.getAvgRatings());
                    dayPartList.add(daypartData);

                }

                //Setting DaypartData for daypart = "PT"
                if(null!=channelAudiencePT && channelAudiencePT.size()>0){
                    LmkAudienceResponse ad = channelAudiencePT.get(0);
                    DaypartData daypartData = new DaypartData();
                    daypartData.setId("PT");
                    daypartData.setImpact(ad.getAvgImpacts());
                    daypartData.setRating(ad.getAvgRatings());
                    dayPartList.add(daypartData);

                }

                demoData.setDaypart(dayPartList);

                //Setting avgImpact to demoData
                if(null!=interimAudienceList && interimAudienceList.size()>0){
                    demoData.setImpact(interimAudienceList.get(0).getAvgImpact());
                }

                //Setting avgRating to demoData
                if(null!=interimRatingList && interimRatingList.size()>0){
                    demoData.setRating(interimRatingList.get(0).getAvgRatings());
                }

                //Setting Affinity, Reach and AffinityReach Value only for secondary demos
                if(secDemos.contains(demoId)){

                if(null!=affinityMap.get(demoId).get(c.getLmkRefNo())){
                    demoData.setAffinityIndex(affinityMap.get(demoId).get(c.getLmkRefNo()));
                }
                if(null!=reachMap.get(demoId).get(c.getLmkRefNo())){
                    demoData.setReachIndex(reachMap.get(demoId).get(c.getLmkRefNo()));
                }
                if(null!=affinityReachMap.get(c.getLmkRefNo()).get(demoId)){
                    demoData.setAffinityReachIndex(affinityReachMap.get(c.getLmkRefNo()).get(demoId));
                }}
                affinityDemos.add(demoData);
            }

            channel.setDemos(affinityDemos);
            channel.setSecDemoSum(demoSumMap.get(c.getLmkRefNo()));

            //Calculating the resultDemo for the channel
            double maxValueInMap=(Collections.max(affinityReachMap.get(c.getLmkRefNo()).values()));
            affinityReachMap.get(c.getLmkRefNo()).forEach((k,v) -> {
                ResultDemo resultDemo = new ResultDemo();
                if(v==maxValueInMap&&v!=0.0) {
                    resultDemo.setId(k);
                    Optional<Demo> demo = demoDataList.stream().filter(d-> d.getId()==k).findAny();
                    resultDemo.setSegment(demo.get().getSegment());

                    channel.setResultDemo(resultDemo);
                }

            });

            channels.add(channel);

        }

        //Setting the ChannelData to the AffinityResponse
        affinityResponse.setChannels(channels);


          return affinityResponse;
    }

    private UniverseData[] getUniverseData(AffinityRequest affinityRequestCopy) {
        UniverseData[] universeDataList = new UniverseData[0];
        String demos = affinityRequestCopy.getPrimaryDemos().stream().map(Object::toString).collect(Collectors.joining(","));
        if (!affinityRequestCopy.getSecondaryDemos().isEmpty()){
            demos = demos + "," + affinityRequestCopy.getSecondaryDemos().stream().map(Object::toString).collect(Collectors.joining(","));
        }

        String url = "https://dmsbookingportaluat.multichoice.co.za/Ingres/universe?targetMarkets="+demos+"&start="+affinityRequestCopy.getSourceStart()+"&end="+affinityRequestCopy.getSourceEnd();

        try {
            universeDataList = restTemplate.getForObject(url, UniverseData[].class);
        }catch (Exception e){
            logger.error("Error in UniverseLMKCall:"+e);
        }
        try {
            logger.info("response from lmk universe: " + new ObjectMapper().writeValueAsString(universeDataList));
        } catch (JsonProcessingException e) {
            logger.info(e.getMessage());
            

        }
        return universeDataList;
    }

    private static List getGroupingByKey(LmkAudienceResponse x) {
        return Arrays.asList(x.getChannelNo(), x.getDemoNo());
    }


    public List<LmkAudienceResponse> getLmkAudienceData(List<Integer> lmkList, List<Integer> demosList,AffinityRequest affinityRequestCopy) {
        LmkAudienceResponse[] lmkAudienceResponses = new LmkAudienceResponse[0];

        String url = "https://dmsbookingportaluat.multichoice.co.za/Ingres/audience";


      //  List<Integer> mockChannelList =Arrays.asList(83,44);
        LmkAudienceRequest lmkAudienceRequest = new LmkAudienceRequest(lmkList, demosList, affinityRequestCopy.getSourceStart(),
                affinityRequestCopy.getSourceEnd(),affinityRequestCopy.getWeekParts(),affinityRequestCopy.getDayParts());
        try {
            logger.info("request to lmk: " + new ObjectMapper().writeValueAsString(lmkAudienceRequest));
        } catch (JsonProcessingException e) {
            logger.info(e.getMessage());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LmkAudienceRequest> requestBody = new HttpEntity<>(lmkAudienceRequest, headers);

        try {
             lmkAudienceResponses = restTemplate.postForObject(url, requestBody, LmkAudienceResponse[].class);
        }catch (Exception e){
            logger.error("Error calling Lmk:",e);
        }
        try {
            logger.info("response from lmk: " + new ObjectMapper().writeValueAsString(lmkAudienceResponses));
        } catch (JsonProcessingException e) {
            logger.info(e.getMessage());

        }
        assert lmkAudienceResponses != null;
        return Arrays.asList(lmkAudienceResponses);
    }

    
    /**
     * Method to save affinity response
     * @param affinityCreateRequest
     * @return int
     */
    @Override
    public SaveResponse saveAffinity(AffCreateRequest affinityCreateRequest) {

        AffinityResponse affResponse = affinityCreateRequest.getAffinity();
        
        // create aff Channel list
        List<AffChannels> affChannelList = streamChannelData(affResponse);
        
        // create aff universe list
        List<AffUniverse> affUniverseList = streamUniverseData(affResponse);
        
        // create aff demo list
        List<AffDemos> affDemoList = streamDemoData(affResponse);

        // create affinity
        Aff aff = populateAffinityData(affinityCreateRequest, affChannelList, affUniverseList, affDemoList);

        // create baseline
        Baseline baseline = populateBaselineData(affinityCreateRequest, aff);

        Baseline savedBaseline = baselineRepository.save(baseline);

        return new SaveResponse(savedBaseline.getId(),"Created Successfully");

    }

    /**
     * @param affinityCreateRequest
     * @param aff
     * @return
     */
    private Baseline populateBaselineData(AffCreateRequest affinityCreateRequest, Aff aff) {
        Baseline baseline = new Baseline();
        baseline.setRateCardId(affinityCreateRequest.getRatecardId());
        baseline.setTitle(affinityCreateRequest.getTitle());
        if(affinityCreateRequest.getStatus().equalsIgnoreCase("Draft")) {
            baseline.setStatus("D");
        }else if (affinityCreateRequest.getStatus().equalsIgnoreCase("Approved")) {
            baseline.setStatus("A");
        }
        baseline.setCreatedBy(1);
        baseline.setLastModifiedBy(1);
        baseline.setAff(aff);
        return baseline;
    }

    /**
     * @param affinityCreateRequest
     * @param affChannelList
     * @param affUniverseList
     * @param affDemoList
     * @return
     */
    private Aff populateAffinityData(AffCreateRequest affinityCreateRequest, List<AffChannels> affChannelList,
            List<AffUniverse> affUniverseList, List<AffDemos> affDemoList) {
        Aff aff = new Aff();
        aff.setTerritoryId(affinityCreateRequest.getAffinity().getTerritory());
        aff.setPlatformId(affinityCreateRequest.getAffinity().getPlatform());
        aff.setSourceStart(affinityCreateRequest.getAffinity().getSourceStart());
        aff.setSourceEnd(affinityCreateRequest.getAffinity().getSourceEnd());
        aff.setPanel(affinityCreateRequest.getAffinity().getPanel());
        if (affinityCreateRequest.getWeekPart().equalsIgnoreCase("WEEKDAY")) {
            aff.setWeekpart("WD");
        } else if (affinityCreateRequest.getWeekPart().equalsIgnoreCase("ALLDAY")) {
            aff.setWeekpart("AD");
        } else if (affinityCreateRequest.getWeekPart().equalsIgnoreCase("WEEKEND")) {
            aff.setWeekpart("WE");
        }
        aff.setAffChannels(affChannelList);
        aff.setAffDemos(affDemoList);
        aff.setAffUniverse(affUniverseList);
        return aff;
    }

    /**
     * @param affResponse
     * @return
     */
    private List<AffDemos> streamDemoData(AffinityResponse affResponse) {
        // create aff_demos
        
        List<AffDemos> affDemoList = new ArrayList<AffDemos>();

        affResponse.getPriDemos().forEach(a -> {
            AffDemos affDemo = new AffDemos();
            affDemo.setDemoId(a);
            affDemo.setPrimary(true);
            affDemoList.add(affDemo);
        });

        affResponse.getSecDemos().forEach(a -> {
            AffDemos affDemo = new AffDemos();
            affDemo.setDemoId(a);
            affDemo.setPrimary(false);
            affDemoList.add(affDemo);
        });
        return affDemoList;
    }

    /**
     * @param affResponse
     * @return
     */
    private List<AffUniverse> streamUniverseData(AffinityResponse affResponse) {
        // create aff_Universe
        
        List<AffUniverse> affUniverseList = new ArrayList<AffUniverse>();
        for (UniverseData universeData : affResponse.getUniverses()) {
            AffUniverse affUniverse = new AffUniverse();
            for (DemoAverage demoAvg : affResponse.getDemoAverage()) {
                if (demoAvg.getId() == universeData.getDemoNo()) {
                    affUniverse.setDemoId(universeData.getDemoNo());
                    affUniverse.setAvgImpact(demoAvg.getAverageImpact());
                    affUniverse.setUniverse(universeData.getAverage());
                }
            }
            affUniverseList.add(affUniverse);
        }
        return affUniverseList;
    }

    /**
     * @param affResponse
     * @return
     */
    private List<AffChannels> streamChannelData(AffinityResponse affResponse) {
        // create channels ---> demos ----> daypart
        List<AffChannels> affChannelList = new ArrayList<AffChannels>();
        
        for (AffinityChannel affCh : affResponse.getChannels()) {

            List<AffChannelDemos> affChannelDemoList = new ArrayList<AffChannelDemos>();
            AffChannels affChannel = new AffChannels();
            affChannel.setChannelId(affCh.getId());
            affChannel.setSecDemoImpactTotal(affCh.getSecDemoSum());
            if(affCh.getResultDemo()!=null) {
            affChannel.setResultDemoId(affCh.getResultDemo().getId());
            affChannel.setResultSegment(affCh.getResultDemo().getSegment());
            }
            for (AffinityDemo affinityDemo : affCh.getDemos()) {

                List<AffChannelDayparts> affChannelDaypartList = new ArrayList<AffChannelDayparts>();
                AffChannelDemos affChannelDemo = new AffChannelDemos();
                affChannelDemo.setDemoId(affinityDemo.getId());
                affChannelDemo.setImpact(affinityDemo.getImpact());
                affChannelDemo.setRating(affinityDemo.getRating());
                affChannelDemo.setAffinityIndex(affinityDemo.getAffinityIndex());
                affChannelDemo.setReachIndex(affinityDemo.getReachIndex());
                affChannelDemo.setAffinityReachIndex(affinityDemo.getAffinityReachIndex());

                for (DaypartData daypart : affinityDemo.getDaypart()) {

                    AffChannelDayparts affDaypart = new AffChannelDayparts();
                    affDaypart.setDaypartId(daypart.getId());
                    affDaypart.setImpact(daypart.getImpact());
                    affDaypart.setRating(daypart.getRating());
                    affChannelDaypartList.add(affDaypart);
                }
                affChannelDemo.setAffChannelDayparts(affChannelDaypartList);
                affChannelDemoList.add(affChannelDemo);
            }
            affChannel.setAffChannelDemos(affChannelDemoList);
            affChannelList.add(affChannel);
        }
        return affChannelList;
    }
    
    
    /**
     * Method to update affinity response
     * @param affinityUpdateRequest
     * @return int
     */
    public SaveResponse updateAffinity(AffUpdateRequest affinityUpdateRequest) {
        
        Baseline existingBaseline = baselineRepository.findById(affinityUpdateRequest.getId()).orElseThrow(
                () -> new BaselineNotFoundException("Baseline not found for id : " + affinityUpdateRequest.getId()));
        
        deleteAffinity(existingBaseline.getId());
        
        AffinityResponse affResponse = affinityUpdateRequest.getAffinity();
        
        // create aff Channel list
        List<AffChannels> affChannelList = streamChannelData(affResponse);
        
        // create aff universe list
        List<AffUniverse> affUniverseList = streamUniverseData(affResponse);
        
        // create aff demo list
        List<AffDemos> affDemoList = streamDemoData(affResponse);

         
        Aff aff = new Aff();
        aff.setTerritoryId(affinityUpdateRequest.getAffinity().getTerritory());
        aff.setPlatformId(affinityUpdateRequest.getAffinity().getPlatform());
        aff.setSourceStart(affinityUpdateRequest.getAffinity().getSourceStart());
        aff.setSourceEnd(affinityUpdateRequest.getAffinity().getSourceEnd());
        aff.setPanel(affinityUpdateRequest.getAffinity().getPanel());
        if (affinityUpdateRequest.getWeekPart().equalsIgnoreCase("WEEKDAY")) {
            aff.setWeekpart("WD");
        } else if (affinityUpdateRequest.getWeekPart().equalsIgnoreCase("ALLDAY")) {
            aff.setWeekpart("AD");
        } else if (affinityUpdateRequest.getWeekPart().equalsIgnoreCase("WEEKEND")) {
            aff.setWeekpart("WE");
        }
        aff.setAffChannels(affChannelList);
        aff.setAffDemos(affDemoList);
        aff.setAffUniverse(affUniverseList);
        
        existingBaseline.setRateCardId(affinityUpdateRequest.getRatecardId());
        existingBaseline.setTitle(affinityUpdateRequest.getTitle());
        if(affinityUpdateRequest.getStatus().equalsIgnoreCase("Draft")) {
            existingBaseline.setStatus("D");
        }else if (affinityUpdateRequest.getStatus().equalsIgnoreCase("Approved")) {
            existingBaseline.setStatus("A");
        }
        existingBaseline.setAff(aff);
        
        Baseline updateBaseline = baselineRepository.save(existingBaseline);

        return new SaveResponse(updateBaseline.getId(),"Updated Successfully");
        
    }
    
    
    /**
     * Method to delete affinity 
     * @param baselineId
     * @return void
     */
    @Override
    public void deleteAffinity(int baselineId) {
        Baseline baseline = baselineRepository.getOne(baselineId);
        Aff affinity = baseline.getAff();
        baseline.setAff(null);
        affinityRepository.deleteById(affinity.getId());

    }
    
    
    /**
     * Method to get list of baseline
     * @return List<BaselieListResponse>
     */
    @Override
    public List<BaselineListResponse> getBaselineList() {
        List<BaselineListResponse> baselineListResponseList= new ArrayList<>();
        List<Baseline> baselineList = baselineRepository.findAll();
        baselineList.forEach(baseline ->
        {   BaselineListResponse baselineListResponse = new BaselineListResponse();
            baselineListResponse.setId(baseline.getId());
            baselineListResponse.setTitle(baseline.getTitle());
            baselineListResponse.setRateCardId(baseline.getRateCardId());
            if(baseline.getStatus().equalsIgnoreCase("A"))
                baselineListResponse.setStatus("Approved");
            else
                baselineListResponse.setStatus("Draft");
            baselineListResponse.setWeekPart(baseline.getAff().getWeekpart());
            if (baseline.getAff().getWeekpart().equalsIgnoreCase("WE"))
                baselineListResponse.setWeekPart("WEEKEND");
            else {
                if (baseline.getAff().getWeekpart().equalsIgnoreCase(("WD")))
                    baselineListResponse.setWeekPart("WEEKDAY");
                else
                    baselineListResponse.setWeekPart("ALLDAY");
            }
            baselineListResponseList.add(baselineListResponse);
        });
        return baselineListResponseList;
    }
    
    
    
    /**
     * Method to delete the baseline
     * @param baselineId
     * @return String
     */
    @Override
    public SaveResponse deleteBaseline(int baseLineId) {
        Baseline baseline = baselineRepository.findById(baseLineId)
                .orElseThrow(() -> new BaselineNotFoundException("Baseline not found for this Id :: " + baseLineId));
        baselineRepository.delete(baseline);
        return new SaveResponse(baseLineId, "Deleted Successfully");
    }
    
    
    
    /**
     * Method to get the baseline by id
     * @param bselineId
     * @return BaselineResponse
     */
    @Override
    public BaselineResponse getBaselineById(int baseLineId) {

        Baseline baseline = baselineRepository.findById(baseLineId)
                .orElseThrow(() -> new BaselineNotFoundException("Baseline not found for this Id :: " + baseLineId));

        List<AffDemos> affinityDemosList = baseline.getAff().getAffDemos();
        List<Integer> primaryDemosList = new ArrayList<>();
        List<Integer> secDemosList = new ArrayList<>();
        affinityDemosList.forEach(affinityDemos -> {
            if (affinityDemos.isPrimary())
                primaryDemosList.add(affinityDemos.getDemoId());
            else
                secDemosList.add(affinityDemos.getDemoId());
        });

        List<AffUniverse> affinityUniverseList = baseline.getAff().getAffUniverse();
        List<UniverseData> universeDataList = new ArrayList<>();
        List<DemoAverage> demoAverageList = new ArrayList<>();

        affinityUniverseList.forEach(affinityUniverse -> {
            UniverseData universeData = new UniverseData();
            universeData.setDemoNo(affinityUniverse.getDemoId());
            universeData.setAverage(affinityUniverse.getUniverse());
            universeDataList.add(universeData);

            DemoAverage demoAverage = new DemoAverage(affinityUniverse.getDemoId(), affinityUniverse.getAvgImpact());
            demoAverageList.add(demoAverage);
        });

        Set<String> dayPartSet = new HashSet<>();

        List<AffChannels> affinityChannelsList = baseline.getAff().getAffChannels();
        List<AffinityChannel> affinityChannelResponseList = new ArrayList<>();
        affinityChannelsList.forEach(affinityChannels -> {
            List<AffChannelDemos> affinityChannelDemoList = affinityChannels.getAffChannelDemos();
            List<AffinityDemo> affinityDemoList = new ArrayList<>();
            affinityChannelDemoList.forEach(affinityChannelDemos -> {
                List<AffChannelDayparts> affinityChannelDaypartsList = affinityChannelDemos
                        .getAffChannelDayparts();
                List<DaypartData> daypartDataList = new ArrayList<>();
                affinityChannelDaypartsList.forEach(affinityChannelDayparts -> {
                    DaypartData daypartData = new DaypartData(affinityChannelDayparts.getDaypartId(),
                            affinityChannelDayparts.getImpact(), affinityChannelDayparts.getRating());
                    daypartDataList.add(daypartData);
                    dayPartSet.add(affinityChannelDayparts.getDaypartId());
                });
                affinityDemoList.add(new AffinityDemo(affinityChannelDemos.getDemoId(), daypartDataList,
                        affinityChannelDemos.getImpact(), affinityChannelDemos.getRating(),
                        affinityChannelDemos.getAffinityIndex(), affinityChannelDemos.getReachIndex(),
                        affinityChannelDemos.getAffinityReachIndex()));
            });
            Channel channel = channelDemoRepository.getOne(affinityChannels.getChannelId());
            List<String> genres = new ArrayList<>();
            channel.getGenres().forEach(genre -> genres.add(genre.getName()));
            AffinityChannel affinityChannel = new AffinityChannel();
            affinityChannel.setId(affinityChannels.getId());
            affinityChannel.setName(channel.getName());
            affinityChannel.setTier(channel.getPackageName());
            affinityChannel.setNetwork(channel.getNetwork());
            affinityChannel.setGenre(genres);
            affinityChannel.setDemos(affinityDemoList);
            affinityChannel.setSecDemoSum(affinityChannels.getSecDemoImpactTotal());
            if (affinityChannels.getResultDemoId() != 0) {
                affinityChannel
                        .setResultDemo(new ResultDemo(affinityChannels.getResultDemoId(), affinityChannels.getResultSegment()));
            }
            affinityChannelResponseList.add(affinityChannel);
        });


        List<String> daypartList = new ArrayList<>(dayPartSet);
        AffinityResponse affinityResponse = new AffinityResponse();
        affinityResponse.setTerritory(baseline.getAff().getTerritoryId());
        affinityResponse.setPlatform(baseline.getAff().getPlatformId());
        affinityResponse.setPanel(baseline.getAff().getPanel());
        affinityResponse.setSourceStart(baseline.getAff().getSourceStart());
        affinityResponse.setSourceEnd(baseline.getAff().getSourceEnd());
        affinityResponse.setDayparts(daypartList);
        affinityResponse.setPriDemos(primaryDemosList);
        affinityResponse.setSecDemos(secDemosList);
        affinityResponse.setUniverses(universeDataList);
        affinityResponse.setDemoAverage(demoAverageList);
        affinityResponse.setChannels(affinityChannelResponseList);

        BaselineResponse baselineResponse = new BaselineResponse();
        baselineResponse.setId(baseline.getId());
        baselineResponse.setTitle(baseline.getTitle());
        baselineResponse.setRatecardId(baseline.getRateCardId());
        if (baseline.getStatus().equals("A"))
            baselineResponse.setStatus("Approved");
        else
            baselineResponse.setStatus("Draft");
        if (baseline.getAff().getWeekpart().equals("WE"))
            baselineResponse.setWeekPart("WEEKEND");
        else {
            if (baseline.getAff().getWeekpart().equals("WD"))
                baselineResponse.setWeekPart("WEEKDAY");
            else
                baselineResponse.setWeekPart("ALLDAY");
        }

        baselineResponse.setAffinityResponse(affinityResponse);

        return baselineResponse;
    }
    
}
