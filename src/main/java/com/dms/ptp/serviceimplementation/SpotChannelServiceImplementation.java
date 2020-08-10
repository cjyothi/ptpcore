package com.dms.ptp.serviceimplementation;

import com.dms.ptp.dto.*;
import com.dms.ptp.entity.*;
import com.dms.ptp.exception.BaselineNotFoundException;
import com.dms.ptp.exception.InvalidParamException;
import com.dms.ptp.repository.ChannelDemoRepository;
import com.dms.ptp.repository.SCChannelRepository;
import com.dms.ptp.repository.SCRepository;
import com.dms.ptp.response.*;
import com.dms.ptp.response.SCSplitByDaypart;
import com.dms.ptp.response.SCSpotsByWeekpart;
import com.dms.ptp.service.AffinityReachService;
import com.dms.ptp.service.PricingCalculatorService;
import com.dms.ptp.service.SpotChannelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class SpotChannelServiceImplementation implements SpotChannelService{
    
    @Autowired
    AffinityReachService affinityReachService;
    
    @Autowired
    PricingCalculatorService pricingCalculatorService;

    @Autowired
    ChannelDemoRepository channelDemoRepository;
    
    @Autowired
    SCRepository scRepository;
    
    @Autowired
    SCChannelRepository scChannelRepository;

    static Logger logger = LoggerFactory.getLogger(SpotChannelServiceImplementation.class);

    private static final String WEEKDAY_END = "WEEKDAY_END";
    private static final String ALLDAY = "ALLDAY";
    private static final String WEEKDAY = "WEEKDAY";
    private static final String WEEKEND = "WEEKEND";
    /**
     * Method to get the spot channel response
     * 
     * @param spotChannelRequest SpotChannelRequest
     * @return SpotChannelResponse
     */
    @Override
    public SpotChannelResponse getSpotChannel(SpotChannelRequest spotChannelRequest) throws InvalidParamException {

        String weekPartType = spotChannelRequest.getComponent().equals("Sport")? WEEKDAY_END:ALLDAY;

        // Get the Affinity response by baselineId1
        BaselineResponse baseline1  = affinityReachService.getBaselineById(spotChannelRequest.getBaseline1());

       if(!validateWeekpart(weekPartType,baseline1.getWeekPart()))
           throw new InvalidParamException("Weekpart doesn't align for the baseline : "+ baseline1.getId());
        //Get affinity channel list to be used as master channel list
        List<CatalogDetail> catalogDetailList = spotChannelRequest.getCatalogDetail();
        List<Integer> channelList = new ArrayList<>();
        catalogDetailList.forEach(catalogDetail -> channelList.add(catalogDetail.getChannelId()));
        List<AffinityChannel> affinityChannelList1 = baseline1.getAffinityResponse().getChannels();
        
        // Get the Affinity response by baselineId2 if there
        List<AffinityChannel> affinityChannelList2 = new ArrayList<>();
        BaselineResponse baseline2 = new BaselineResponse();
        if(weekPartType.equalsIgnoreCase(WEEKDAY_END)) {
        baseline2  = affinityReachService.getBaselineById(spotChannelRequest.getBaseline2());
            if(!validateWeekpart(weekPartType,baseline1.getWeekPart(),baseline2.getWeekPart()))
                throw new InvalidParamException("Weekpart should not be same for baseline1 : "+ baseline1.getId() + " baseline2 : " +baseline2.getId());
        affinityChannelList2 = baseline2.getAffinityResponse().getChannels();
        }
        
        // Get loose spot data with baseline
        List<InterimSCLooseSpotRate> looseSpotYOYMap = getLooseSpotRateList(channelList,
                weekPartType, spotChannelRequest.getYoyBaseline1(),
                spotChannelRequest.getYoyBaseline2());

        List<InterimSCLooseSpotRate> looseSpotQOQMap = getLooseSpotRateList(channelList,
                weekPartType, spotChannelRequest.getQoqBaseline1(),
                spotChannelRequest.getQoqBaseline2());

        List<InterimSCLooseSpotRate> looseSpotNewMap = getLooseSpotRateList(channelList,
                weekPartType, spotChannelRequest.getBaseline1(),
                spotChannelRequest.getBaseline2());
        
        return streamSpotChannelResponse(spotChannelRequest,weekPartType, catalogDetailList, affinityChannelList1, affinityChannelList2, looseSpotYOYMap, looseSpotQOQMap,
                looseSpotNewMap, baseline1, baseline2);
        
    }

    private boolean validateWeekpart(String weekPartType,String baseLineWeekPart) {
        boolean valid ;
        if(weekPartType.equals(ALLDAY)){
            valid = (weekPartType.equals(baseLineWeekPart));
        }
        else{
            List<String> weekPartList =Arrays.asList(WEEKDAY,WEEKEND);
            valid = (weekPartList.contains(baseLineWeekPart));
        }
        return valid;
    }


    private boolean validateWeekpart(String weekPartType,String baseLineWeekPart1,String baseLineWeekPart2) {
        boolean valid =true;
        if(baseLineWeekPart1.equals(baseLineWeekPart2))
            valid=false;

        return valid;
    }

        /**
         * @param spotChannelRequest SpotChannelRequest
         * @param affinityChannelList1 List<AffinityChannel>
         * @param looseSpotYOYMap List<InterimSCLooseSpotRate>
         * @param looseSpotQOQMap List<InterimSCLooseSpotRate>
         * @param looseSpotNewMap List<InterimSCLooseSpotRate>
         * @return SpotChannelResponse
         */
    private SpotChannelResponse streamSpotChannelResponse(SpotChannelRequest spotChannelRequest,String weekPartType,List<CatalogDetail> catalogDetailList, List<AffinityChannel> affinityChannelList1, List<AffinityChannel> affinityChannelList2,
            List<InterimSCLooseSpotRate> looseSpotYOYMap, List<InterimSCLooseSpotRate> looseSpotQOQMap,
            List<InterimSCLooseSpotRate> looseSpotNewMap, BaselineResponse baseline1, BaselineResponse baseline2) {
        

        List<SpotChannels> spotChannelsList=new ArrayList<>();
        // list of demo id from baseline1

        Set<Integer> demoList1 = new HashSet<>();
        for(AffinityChannel affChannel : affinityChannelList1) {
            for(AffinityDemo affDemo : affChannel.getDemos()) {
                demoList1.add(affDemo.getId());
            }
        }
        
        // list of demo id from baseline2
        Set<Integer> demoList2 = new HashSet<>();
        if (affinityChannelList2.size() > 0) {
            for (AffinityChannel affChannel : affinityChannelList2) {
                for (AffinityDemo affDemo : affChannel.getDemos()) {
                    demoList2.add(affDemo.getId());
                }
            }
        }
        
        // union of demo id's
        Set<Integer> set = new HashSet<>();
        set.addAll(demoList1);
        set.addAll(demoList2);
        List<Integer> resultDemoList = new ArrayList<>(set);

        catalogDetailList.forEach(catalogDetail -> {

            AffinityChannel affinityChannel1 = affinityChannelList1.stream().filter(af->af.getId()==catalogDetail.getChannelId())
                            .findFirst().orElse(null);
            AffinityChannel affinityChannel2 = null;
            if(!affinityChannelList2.isEmpty()) {
                affinityChannel2=affinityChannelList2.stream().filter(af -> af.getId() == catalogDetail.getChannelId())
                        .findFirst().orElse(null);
            }
            SpotChannels spotChannels=new SpotChannels();

            // channel basic info
            spotChannels.setChannelId(catalogDetail.getChannelId());
            spotChannels.setCatalogId(catalogDetail.getCatalogId());
            Channel channelDB = channelDemoRepository.getOne(catalogDetail.getChannelId());// create a list and iterate inside
            if(affinityChannel1!=null){
                if(affinityChannel1.getResultDemo()!=null){
                    spotChannels.setAffinity(affinityChannel1.getResultDemo().getSegment());
                }
            }else{
                if(affinityChannel2!=null){
                    if(affinityChannel2.getResultDemo()!=null){
                        spotChannels.setAffinity(affinityChannel2.getResultDemo().getSegment());
                    }
                }
            }
            spotChannels.setBouquet(channelDB.getPackageName());
            spotChannels.setName(channelDB.getName());

            // loose spot rates
            List<SCLooseSpotRate> scLooseSpotRatesYOY = looseSpotYOYMap.stream()
                    .filter(interimSCLooseSpotRate -> interimSCLooseSpotRate.getChannelId()==catalogDetail.getChannelId())
                    .map(i->new SCLooseSpotRate(i.getScLooseSpotRate().getWeekPart(),i.getScLooseSpotRate().getDayPart(),i.getScLooseSpotRate().getRate()))
                    .collect(Collectors.toList());

            List<SCLooseSpotRate> scLooseSpotRatesQOQ = looseSpotQOQMap.stream()
                    .filter(interimSCLooseSpotRate -> interimSCLooseSpotRate.getChannelId()==catalogDetail.getChannelId())
                    .map(i->new SCLooseSpotRate(i.getScLooseSpotRate().getWeekPart(),i.getScLooseSpotRate().getDayPart(),i.getScLooseSpotRate().getRate()))
                    .collect(Collectors.toList());

            List<SCLooseSpotRate> scLooseSpotRatesNew = looseSpotNewMap.stream()
                    .filter(interimSCLooseSpotRate -> interimSCLooseSpotRate.getChannelId()==catalogDetail.getChannelId())
                    .map(i->new SCLooseSpotRate(i.getScLooseSpotRate().getWeekPart(),i.getScLooseSpotRate().getDayPart(),i.getScLooseSpotRate().getRate()))
                    .collect(Collectors.toList());

            spotChannels.setLooseSpotYOY(scLooseSpotRatesYOY);
            spotChannels.setLooseSpotQOQ(scLooseSpotRatesQOQ);
            spotChannels.setLooseSpotNew(scLooseSpotRatesNew);

            // package details
            SCPackageDetail packageDetails = new SCPackageDetail();
            packageDetails.setCost(0);
            packageDetails.setFreq(0);
            packageDetails.setTotalSpots(0);
            packageDetails.setSpotRate(0);
            packageDetails.setEstValue(0);
            packageDetails.setDiscount(0);
            
            // set spots by week part
            List<SCSpotsByWeekpart> spotsByWeekpartList = new ArrayList<>();
            if(weekPartType.equalsIgnoreCase(WEEKDAY_END)) {
                SCSpotsByWeekpart sCSpotsByWeekDayOP = new SCSpotsByWeekpart();
                sCSpotsByWeekDayOP.setWeekPart(WEEKDAY);
                sCSpotsByWeekDayOP.setDayPart("OP");
                sCSpotsByWeekDayOP.setSpots(0);
                SCSpotsByWeekpart sCSpotsByWeekDayPT = new SCSpotsByWeekpart();
                sCSpotsByWeekDayPT.setWeekPart(WEEKDAY);
                sCSpotsByWeekDayPT.setDayPart("PT");
                sCSpotsByWeekDayPT.setSpots(0);
                SCSpotsByWeekpart sCSpotsByWeekEndPT = new SCSpotsByWeekpart();
                sCSpotsByWeekEndPT.setWeekPart(WEEKEND);
                sCSpotsByWeekEndPT.setDayPart("PT");
                sCSpotsByWeekEndPT.setSpots(0);
                SCSpotsByWeekpart sCSpotsByWeekEndOP = new SCSpotsByWeekpart();
                sCSpotsByWeekEndOP.setWeekPart(WEEKEND);
                sCSpotsByWeekEndOP.setDayPart("OP");
                sCSpotsByWeekEndOP.setSpots(0);
                spotsByWeekpartList.add(sCSpotsByWeekDayOP);
                spotsByWeekpartList.add(sCSpotsByWeekDayPT);
                spotsByWeekpartList.add(sCSpotsByWeekEndPT);
                spotsByWeekpartList.add(sCSpotsByWeekEndOP);
            }else {
                SCSpotsByWeekpart sCSpotsByAlldayOP = new SCSpotsByWeekpart();
                sCSpotsByAlldayOP.setWeekPart(ALLDAY);
                sCSpotsByAlldayOP.setDayPart("OP");
                sCSpotsByAlldayOP.setSpots(0);
                SCSpotsByWeekpart sCSpotsByAllDayPT = new SCSpotsByWeekpart();
                sCSpotsByAllDayPT.setWeekPart(ALLDAY);
                sCSpotsByAllDayPT.setDayPart("PT");
                sCSpotsByAllDayPT.setSpots(0);
                spotsByWeekpartList.add(sCSpotsByAlldayOP);
                spotsByWeekpartList.add(sCSpotsByAllDayPT);
            }
            packageDetails.setSpotsByWeekpart(spotsByWeekpartList);
            
            // set split by week part
            List<SCSplitByWeekpart> splitByWeekPartList = new ArrayList<>();
            if(weekPartType.equalsIgnoreCase(WEEKDAY_END)) {
                SCSplitByWeekpart weekEndShare = new SCSplitByWeekpart();
                weekEndShare.setWeekPart(WEEKEND);
                weekEndShare.setPercentage(0);
                SCSplitByWeekpart weekDayShare = new SCSplitByWeekpart();
                weekDayShare.setWeekPart(WEEKDAY);
                weekDayShare.setPercentage(0);
                splitByWeekPartList.add(weekDayShare);
                splitByWeekPartList.add(weekEndShare);
            }else {
                SCSplitByWeekpart allDayShare = new SCSplitByWeekpart();
                allDayShare.setWeekPart(ALLDAY);
                allDayShare.setPercentage(100);
                splitByWeekPartList.add(allDayShare);
            }
            packageDetails.setSplitByWeekpart(splitByWeekPartList);
            
            // split by day part
            List<SCSplitByDaypart> splitByDayPartList = new ArrayList<>();
            SCSplitByDaypart sCSplitByOP = new SCSplitByDaypart();
            SCSplitByDaypart sCSplitByPT = new SCSplitByDaypart();
            sCSplitByOP.setDayPart("OP");
            sCSplitByOP.setPercentage(0);
            sCSplitByPT.setDayPart("PT");
            sCSplitByPT.setPercentage(0);
            splitByDayPartList.add(sCSplitByPT);
            splitByDayPartList.add(sCSplitByOP);
            
            packageDetails.setSplitByDaypart(splitByDayPartList);
            spotChannels.setPackageDetail(packageDetails);
            
            List<SCEstRating> estRatingList=new ArrayList<>();
            List<SCEstAudience> estAudienceList=new ArrayList<>();
            List<SCEstRates> estCPPRateList=new ArrayList<>();
            List<SCEstRates> estCPTRateList=new ArrayList<>();
            if(affinityChannel1!=null) {
                // estimated rating list
                resultDemoList.forEach(rd -> estRatingList.add(new SCEstRating(rd, 0)));

                // estimated audience list
                resultDemoList.forEach(rd -> estAudienceList.add(new SCEstAudience(rd, 0)));

                // estimated CPP rate list
                resultDemoList.forEach(rd -> estCPPRateList.add(new SCEstRates(rd, 0)));

                // estimated CPT rate list
                resultDemoList.forEach(rd -> estCPTRateList.add(new SCEstRates(rd, 0)));

            }
            spotChannels.setEstRating(estRatingList);
            spotChannels.setEstAudience(estAudienceList);
            spotChannels.setEstCPP(estCPPRateList);
            spotChannels.setEstCPT(estCPTRateList);

            // set demo detail list
            List<SCDemoDetail> demoDetailList;
            demoDetailList = getDemoDetailList(affinityChannel1,affinityChannel2,baseline1,baseline2);
            spotChannels.setDemoDetail(demoDetailList);
            
            spotChannelsList.add(spotChannels);

        });

        SpotChannelResponse spotChannelResponse=new SpotChannelResponse();

        spotChannelResponse.setBaseline1(spotChannelRequest.getBaseline1());
        spotChannelResponse.setBaseline2(spotChannelRequest.getBaseline2());
        spotChannelResponse.setType(spotChannelRequest.getType());
        spotChannelResponse.setQoqRateCard(spotChannelRequest.getQoqRateCard());
        spotChannelResponse.setYoyRateCard(spotChannelRequest.getYoyRateCard());
        spotChannelResponse.setQoqBaseline1(spotChannelRequest.getQoqBaseline1());
        spotChannelResponse.setQoqBaseline2(spotChannelRequest.getQoqBaseline2());
        spotChannelResponse.setYoyBaseline1(spotChannelRequest.getYoyBaseline1());
        spotChannelResponse.setYoyBaseline2(spotChannelRequest.getYoyBaseline2());
        spotChannelResponse.setWeek(spotChannelRequest.getWeek());
        spotChannelResponse.setSpotLength(spotChannelRequest.getSpotLength());
        spotChannelResponse.setComponent(spotChannelRequest.getComponent());
        spotChannelResponse.setChannels(spotChannelsList);
        return  spotChannelResponse;
    }

    /**
     * Method to get the loose spot rate list
     * 
     * @param channelIdList,weekPartType,baseline1,baseline2
     * @return interimSCLooseSpotRateList
     */
    private List<InterimSCLooseSpotRate> getLooseSpotRateList(List<Integer> channelIdList, String weekPartType, Integer baseline1, Integer baseline2) throws InvalidParamException {

        List<InterimSCLooseSpotRate> interimSCLooseSpotRateList = new ArrayList<>();

        PricingBaseline pricingBaseline1=pricingCalculatorService.getLooseSpot(baseline1);

        if(!validateWeekpart(weekPartType,pricingBaseline1.getWeekPart()))
            throw new InvalidParamException("Weekpart doesn't align for the baseline : "+ baseline1);


        pricingBaseline1.getChannels().forEach(validChannel->{
            if (channelIdList.contains(validChannel.getId())) {
                List<CalculatedRate> calculatedRateList=validChannel.getCalculatedRate();
                calculatedRateList.forEach(calculatedRate ->
                        interimSCLooseSpotRateList.add(new InterimSCLooseSpotRate(validChannel.getId()
                                , new SCLooseSpotRate(pricingBaseline1.getWeekPart(), calculatedRate.getId(), calculatedRate.getRate()))));

            }
        });

        PricingBaseline pricingBaseline2;
        if (weekPartType.equalsIgnoreCase(WEEKDAY_END)) {
            pricingBaseline2=pricingCalculatorService.getLooseSpot(baseline2);
            if(!validateWeekpart(weekPartType,pricingBaseline1.getWeekPart(),pricingBaseline2.getWeekPart()))
                throw new InvalidParamException("Weekpart should not be same for baseline1 : "+ baseline1 + " baseline2 : " +baseline2);
            pricingBaseline2.getChannels().forEach(validChannel->{
                if (channelIdList.contains(validChannel.getId())) {
                    List<CalculatedRate> calculatedRateList=validChannel.getCalculatedRate();
                    calculatedRateList.forEach(calculatedRate ->
                            interimSCLooseSpotRateList.add(new InterimSCLooseSpotRate(validChannel.getId()
                                    , new SCLooseSpotRate(pricingBaseline2.getWeekPart(), calculatedRate.getId(), calculatedRate.getRate()))));

                }
            });

        }


        return interimSCLooseSpotRateList;
    }
    
    
    /**
     * Method to get the list of demo detail
     * 
     * @param affinityChannel1,affinityChannel2,baseline1,baseline2
     * @return scDemoDetailList
     */
    private List<SCDemoDetail> getDemoDetailList(AffinityChannel affinityChannel1, AffinityChannel affinityChannel2, BaselineResponse baseline1,
                                                 BaselineResponse baseline2){
        
        List<SCDemoDetail> scDemoDetailList1 = new ArrayList<>();
        List<SCDemoDetail> scDemoDetailList2 = new ArrayList<>();
        List<SCDemoDetail> resultSCDemoDetailList = new ArrayList<>();

        if(affinityChannel1!=null) {
            affinityChannel1.getDemos().forEach(demo -> {
                SCDemoDetail scDemoDetail=new SCDemoDetail();
                scDemoDetail.setId(demo.getId());
                List<SCAudienceInfo> sCAudienceInfoList=new ArrayList<>();
                demo.getDaypart().forEach(dp -> {
                    SCAudienceInfo sCAudienceInfo=new SCAudienceInfo();
                    sCAudienceInfo.setWeekPart(baseline1.getWeekPart());
                    sCAudienceInfo.setDayPart(dp.getId());
                    sCAudienceInfo.setImpact(dp.getImpact());
                    sCAudienceInfo.setRating(dp.getRating());
                    sCAudienceInfoList.add(sCAudienceInfo);
                    scDemoDetail.setAudienceInfo(sCAudienceInfoList);
                });

                scDemoDetailList1.add(scDemoDetail);
            });

        }
        if(baseline2.getAffinityResponse()!=null) {
                
                if(affinityChannel2!=null) {
                    affinityChannel2.getDemos().forEach(demo-> {
                        SCDemoDetail scDemoDetail = new SCDemoDetail();
                        scDemoDetail.setId(demo.getId());
                        List<SCAudienceInfo> sCAudienceInfoList = new ArrayList<>();
                        demo.getDaypart().forEach(dp-> {
                            SCAudienceInfo sCAudienceInfo = new SCAudienceInfo();
                            sCAudienceInfo.setWeekPart(baseline2.getWeekPart());
                            sCAudienceInfo.setDayPart(dp.getId());
                            sCAudienceInfo.setImpact(dp.getImpact());
                            sCAudienceInfo.setRating(dp.getRating());
                            sCAudienceInfoList.add(sCAudienceInfo);
                            scDemoDetail.setAudienceInfo(sCAudienceInfoList);
                        });
                        scDemoDetailList2.add(scDemoDetail);
                    });

                    List<SCDemoDetail> scDemoDetailList2Copy = new ArrayList<>(scDemoDetailList2);
                    //concatenation for same demoId
                    scDemoDetailList1.forEach(scDemoDetail -> {
                        scDemoDetailList2Copy.forEach(scDemoDetail2->{
                            if(scDemoDetail.getId()==scDemoDetail2.getId()){
                                scDemoDetail.getAudienceInfo().addAll(scDemoDetail2.getAudienceInfo());
                                scDemoDetailList2.remove(scDemoDetail2);
                            }
                        });
                    });
            }

            if(scDemoDetailList2.size()>0)
                resultSCDemoDetailList = Stream.of(scDemoDetailList1, scDemoDetailList2)
                    .flatMap(x -> x.stream())
                    .collect(Collectors.toList());
        }
        
        if(resultSCDemoDetailList.size()>0) {
            
            return resultSCDemoDetailList;
        } else {
            
            return scDemoDetailList1;
        }
    }
    
    /**
     * Method to save spot channel response
     *
     * @param sCCreateRequest
     * @return SaveResponse
     */
    @Override
    public SaveResponse saveSpotChannel(SCCreateRequest sCCreateRequest) {
        
        SC sc = new SC();
        sc.setBaseline1(sCCreateRequest.getBaseline1());
        sc.setBaseline2(sCCreateRequest.getBaseline2());
        sc.setComponent(sCCreateRequest.getComponent());
        sc.setQoqBaseline1(sCCreateRequest.getQoqBaseline1());
        sc.setQoqBaseline2(sCCreateRequest.getQoqBaseline2());
        sc.setYoyBaseline1(sCCreateRequest.getYoyBaseline1());
        sc.setYoyBaseline2(sCCreateRequest.getYoyBaseline2());
        sc.setQoqRateCard(sCCreateRequest.getQoqRateCard());
        sc.setYoyRateCard(sCCreateRequest.getYoyRateCard());
        sc.setRateCardId(sCCreateRequest.getRateCardId());
        sc.setWeek(sCCreateRequest.getWeek());
        sc.setType(sCCreateRequest.getType());
        sc.setCreatedBy(1);
        sc.setLastModifiedBy(1);
        sc.setSpotLength(sCCreateRequest.getSpotLength());
        
        List<SpotChannels> spotChannelList = sCCreateRequest.getChannels();
        List<SCChannels> scChannelList = new ArrayList<SCChannels>();
        
        for(SpotChannels spotChannel : spotChannelList) {
            SCChannels sCChannel = new SCChannels();
            if(spotChannel.getAffinity()!=null) {
            sCChannel.setAffinity(spotChannel.getAffinity());
            }
            sCChannel.setBouquet(spotChannel.getBouquet());
            sCChannel.setName(spotChannel.getName());
            sCChannel.setChannelId(spotChannel.getChannelId());
            sCChannel.setCatalogId(spotChannel.getCatalogId());
            
            // set loose spot  rates
            setLooseSpotRates(spotChannel, sCChannel);
            
            // set demo details
            setDemoDetails(spotChannel, sCChannel);
            
            // set package details
            setPackageDetails(spotChannel, sCChannel);
            
            // set est audience data
            setEstAudienceData(spotChannel, sCChannel);
            
            scChannelList.add(sCChannel);
        }
        
        sc.setScChannels(scChannelList);
        
        SC savedSC = scRepository.save(sc);
        
        return new SaveResponse(savedSC.getId(), "Created Successfully");
        
    }
    
    private void setLooseSpotRates(SpotChannels spotChannel, SCChannels sCChannel) {
        
        List<SCLooseSpotRate> lsYOYRateList = spotChannel.getLooseSpotYOY();
        List<SCLooseSpotRate> lsQOQRateList = spotChannel.getLooseSpotQOQ();
        List<SCLooseSpotRate> lsNewRateList = spotChannel.getLooseSpotNew();
        
        List<SCYOYRate> scYOYRateList = new ArrayList<>();
        for(SCLooseSpotRate lsYOYRate : lsYOYRateList) {
            SCYOYRate scYOYRate = new SCYOYRate();
            scYOYRate.setDayPart(lsYOYRate.getDayPart());
            scYOYRate.setRate(lsYOYRate.getRate());
            scYOYRate.setWeekPart(lsYOYRate.getWeekPart());
            scYOYRateList.add(scYOYRate);
        }
        
        List<SCQOQRate> scQOQRateList = new ArrayList<>();
        for(SCLooseSpotRate lsYOYRate : lsQOQRateList) {
            SCQOQRate scQOQRate = new SCQOQRate();
            scQOQRate.setDayPart(lsYOYRate.getDayPart());
            scQOQRate.setRate(lsYOYRate.getRate());
            scQOQRate.setWeekPart(lsYOYRate.getWeekPart());
            scQOQRateList.add(scQOQRate);
        }
        
        List<SCNewRate> scNewRateList = new ArrayList<>();
        for(SCLooseSpotRate lsYOYRate : lsNewRateList) {
            SCNewRate scNewRate = new SCNewRate();
            scNewRate.setDayPart(lsYOYRate.getDayPart());
            scNewRate.setRate(lsYOYRate.getRate());
            scNewRate.setWeekPart(lsYOYRate.getWeekPart());
            scNewRateList.add(scNewRate);
        }
        
        sCChannel.setScYOYRates(scYOYRateList);
        sCChannel.setScQOQRates(scQOQRateList);
        sCChannel.setScNewRates(scNewRateList);
    }
    
    
    private void setDemoDetails(SpotChannels spotChannel, SCChannels sCChannel) {
        
       List<SCDemoDetail> demoDetailList = spotChannel.getDemoDetail();
       
       List<SCDemo> scDemosList = new ArrayList<>();
       for(SCDemoDetail demoDetail : demoDetailList) {
           SCDemo scDemo = new SCDemo();
           scDemo.setDemoId(demoDetail.getId());
           List<SCDemoAudience> scDemoAudDataList = new ArrayList<>();
           for(SCAudienceInfo scAudInfo : demoDetail.getAudienceInfo()) {
               SCDemoAudience scDemoAud = new SCDemoAudience();
               scDemoAud.setDayPart(scAudInfo.getDayPart());
               scDemoAud.setWeekPart(scAudInfo.getWeekPart());
               scDemoAud.setImpact(scAudInfo.getImpact());
               scDemoAud.setRating(scAudInfo.getRating());
               scDemoAudDataList.add(scDemoAud);
           }
           scDemo.setScDemoAudData(scDemoAudDataList);
           scDemosList.add(scDemo);
       }
       sCChannel.setScDemos(scDemosList);
    }
    
    
    private void setPackageDetails(SpotChannels spotChannel, SCChannels sCChannel) {
        
        SCPackageDetail pkgDetails = spotChannel.getPackageDetail();
        SCPackage scPackage = new SCPackage();
        scPackage.setCost(pkgDetails.getCost());
        scPackage.setFreq(pkgDetails.getFreq());
        scPackage.setTotalSpots(pkgDetails.getTotalSpots());
        scPackage.setSpotRate(pkgDetails.getSpotRate());
        scPackage.setDiscount(pkgDetails.getDiscount());
        scPackage.setEstValue(pkgDetails.getEstValue());
        
        List<com.dms.ptp.entity.SCSpotsByWeekpart> scSpotsByWeekPartList = new ArrayList<>();
        for(SCSpotsByWeekpart spotByWeekPart : pkgDetails.getSpotsByWeekpart()) {
            com.dms.ptp.entity.SCSpotsByWeekpart scSpotByWeekPartEntity = new com.dms.ptp.entity.SCSpotsByWeekpart();
            scSpotByWeekPartEntity.setDayPart(spotByWeekPart.getDayPart());
            scSpotByWeekPartEntity.setSpots(spotByWeekPart.getSpots());
            scSpotByWeekPartEntity.setWeekPart(spotByWeekPart.getWeekPart());
            scSpotsByWeekPartList.add(scSpotByWeekPartEntity);
        }
        
        List<com.dms.ptp.entity.SCSplitByWeekPart> scSplitByWeekPartList = new ArrayList<>();
        for(SCSplitByWeekpart splitByWeekPart : pkgDetails.getSplitByWeekpart()) {
            com.dms.ptp.entity.SCSplitByWeekPart sCSplitByWeekpartEntity = new com.dms.ptp.entity.SCSplitByWeekPart();
            sCSplitByWeekpartEntity.setWeekPart(splitByWeekPart.getWeekPart());
            sCSplitByWeekpartEntity.setPercentage(splitByWeekPart.getPercentage());
            scSplitByWeekPartList.add(sCSplitByWeekpartEntity);
        }
        
        List<com.dms.ptp.entity.SCSplitByDaypart> scSplitByDaypartList = new ArrayList<>();
        for(SCSplitByDaypart splitByDayPart : pkgDetails.getSplitByDaypart()) {
            com.dms.ptp.entity.SCSplitByDaypart sCSplitByDaypartEntity = new com.dms.ptp.entity.SCSplitByDaypart();
            sCSplitByDaypartEntity.setDayPart(splitByDayPart.getDayPart());
            sCSplitByDaypartEntity.setPercentage(splitByDayPart.getPercentage());
            scSplitByDaypartList.add(sCSplitByDaypartEntity);
        }
        
        scPackage.setScSpotsByWeekPart(scSpotsByWeekPartList);
        scPackage.setScSplitByWeekPart(scSplitByWeekPartList);
        scPackage.setScSplitByDayPart(scSplitByDaypartList);
        
        sCChannel.setScPackage(scPackage);
    }
    
    
    
    private void setEstAudienceData(SpotChannels spotChannel, SCChannels sCChannel) {
        
        List<SCEstAudData> scEstAudDataList = new ArrayList<>();
        
        List<SCEstAudience> estAudList = spotChannel.getEstAudience();
        List<SCEstRating> estRatingList = spotChannel.getEstRating();
        List<SCEstRates> estCPPRateList = spotChannel.getEstCPP();
        List<SCEstRates> estCPTRateList = spotChannel.getEstCPT();
        
       
        IntStream.range(0, estAudList.size()).forEach(i -> {

            SCEstAudData scEstAudData = new SCEstAudData();
            scEstAudData.setDemoId(estAudList.get(i).getDemoId());
            scEstAudData.setAudience(estAudList.get(i).getValue());
            scEstAudData.setRating(estRatingList.get(i).getValue());
            scEstAudData.setCpp(estCPPRateList.get(i).getValue());
            scEstAudData.setCpt(estCPTRateList.get(i).getValue());
            scEstAudDataList.add(scEstAudData);

            sCChannel.setScEstAudData(scEstAudDataList);
        });
        
        sCChannel.setScEstAudData(scEstAudDataList);
    }
    
    
    /**
     * Method to update spot channel response
     *
     * @param scUpdateRequest
     * @return SaveResponse
     */
    @Override
    public SaveResponse updateSpotChannel(SCUpdateRequest scUpdateRequest) {
        
        SC existingSC = scRepository.findById(scUpdateRequest.getId()).orElseThrow(
                () -> new BaselineNotFoundException("Spot channel details not found for id : " + scUpdateRequest.getId()));

        deleteSCChannels(existingSC.getId());
        
        existingSC.setBaseline1(scUpdateRequest.getBaseline1());
        existingSC.setBaseline2(scUpdateRequest.getBaseline2());
        existingSC.setComponent(scUpdateRequest.getComponent());
        existingSC.setQoqBaseline1(scUpdateRequest.getQoqBaseline1());
        existingSC.setQoqBaseline2(scUpdateRequest.getQoqBaseline2());
        existingSC.setYoyBaseline1(scUpdateRequest.getYoyBaseline1());
        existingSC.setYoyBaseline2(scUpdateRequest.getYoyBaseline2());
        existingSC.setQoqRateCard(scUpdateRequest.getQoqRateCard());
        existingSC.setYoyRateCard(scUpdateRequest.getYoyRateCard());
        existingSC.setRateCardId(scUpdateRequest.getRateCardId());
        existingSC.setWeek(scUpdateRequest.getWeek());
        existingSC.setType(scUpdateRequest.getType());
        existingSC.setLastModifiedBy(1);
        
        List<SpotChannels> spotChannelList = scUpdateRequest.getChannels();
        List<SCChannels> scChannelList = new ArrayList<SCChannels>();
        
        for(SpotChannels spotChannel : spotChannelList) {
            SCChannels sCChannel = new SCChannels();
            sCChannel.setAffinity(spotChannel.getAffinity());
            sCChannel.setBouquet(spotChannel.getBouquet());
            sCChannel.setName(spotChannel.getName());
            sCChannel.setChannelId(spotChannel.getChannelId());
            sCChannel.setCatalogId(spotChannel.getCatalogId());
            
            // set loose spot  rates
            setLooseSpotRates(spotChannel, sCChannel);
            
            // set demo details
            setDemoDetails(spotChannel, sCChannel);
            
            // set package details
            setPackageDetails(spotChannel, sCChannel);
            
            // set est audience data
            setEstAudienceData(spotChannel, sCChannel);
            
            scChannelList.add(sCChannel);
        }
        
        existingSC.setScChannels(scChannelList);
        
        SC updatedSC = scRepository.save(existingSC);
        
        return new SaveResponse(updatedSC.getId(), "Updated Successfully");
        
    }
    
    /**
     * Method to delete sc channels
     *
     * @param scId
     * @return void
     */
    @Override
    public void deleteSCChannels(int scId) {
        SC sc = scRepository.getOne(scId);
        List<SCChannels> scChannelList = sc.getScChannels();
        sc.setScChannels(null);
        for (SCChannels scChannel : scChannelList) {
            scChannelRepository.deleteById(scChannel.getId());
        }
    }
    
    
    @Override
    @Transactional
    public SaveResponse deleteSpotChannelinRateCard(String component, int rateCardId) {


           List<Integer> scIdList = scRepository.findByComponentIgnoreCaseAndRateCardId(component,rateCardId);
          if(scIdList.isEmpty()){
              throw new BaselineNotFoundException("No SpotChannel Calculation found for rateCardId :"+rateCardId);
          }
           scRepository.deleteByIdIn(scIdList);

        return new SaveResponse(rateCardId,"SpotChannelCalculations Deleted");

    }

    @Override
    public SpotChannelData getSpotChannelRateCard(String type, int week, String component, int rateCardId) {
        SC scDb = scRepository.findByTypeAndComponentAllIgnoreCaseAndRateCardIdAndWeek(type, component, rateCardId, week);
        if(scDb==null){
            throw new BaselineNotFoundException("SpotChanel Calculation not found for type = "+type +", week = "+week+"" +
                    ",Component = "+component+" and rateCardId = "+rateCardId);
        }
        SpotChannelData spotChannelData = new SpotChannelData();
        spotChannelData.setId(scDb.getId());
        spotChannelData.setRateCardId(scDb.getRateCardId());
        spotChannelData.setComponent(scDb.getComponent());
        spotChannelData.setBaseline1(scDb.getBaseline1());
        spotChannelData.setBaseline2(scDb.getBaseline2());
        spotChannelData.setType(scDb.getType());
        spotChannelData.setQoqRateCard(scDb.getQoqRateCard());
        spotChannelData.setYoyRateCard(scDb.getYoyRateCard());
        spotChannelData.setQoqBaseline1(scDb.getQoqBaseline1());
        spotChannelData.setYoyBaseline1(scDb.getYoyBaseline1());
        spotChannelData.setQoqBaseline2(scDb.getQoqBaseline2());
        spotChannelData.setYoyBaseline2(scDb.getYoyBaseline2());
        spotChannelData.setWeek(scDb.getWeek());
        spotChannelData.setSpotLength(scDb.getSpotLength());

        List<SpotChannels> spotChannelsList = new ArrayList<>();
        List<SCChannels> scChannelsList = scDb.getScChannels();

        scChannelsList.forEach(scChannels->{
            SpotChannels spotChannels = new SpotChannels();
            spotChannels.setChannelId(scChannels.getChannelId());
            spotChannels.setCatalogId(scChannels.getCatalogId());
            if(scChannels.getAffinity()!=null) {
            spotChannels.setAffinity(scChannels.getAffinity());
            }
            spotChannels.setBouquet(scChannels.getBouquet());
            spotChannels.setName(scChannels.getName());

            List<SCLooseSpotRate> looseSpotYOYList= scChannels.getScYOYRates().stream()
                    .map(s->new SCLooseSpotRate(s.getWeekPart(),s.getDayPart(),s.getRate()))
                    .collect(Collectors.toList());
            List<SCLooseSpotRate> looseSpotQOQList= scChannels.getScQOQRates().stream()
                    .map(s->new SCLooseSpotRate(s.getWeekPart(),s.getDayPart(),s.getRate()))
                    .collect(Collectors.toList());
            List<SCLooseSpotRate> looseSpotNewList= scChannels.getScNewRates().stream()
                    .map(s->new SCLooseSpotRate(s.getWeekPart(),s.getDayPart(),s.getRate()))
                    .collect(Collectors.toList());

            spotChannels.setLooseSpotYOY(looseSpotYOYList);
            spotChannels.setLooseSpotQOQ(looseSpotQOQList);
            spotChannels.setLooseSpotNew(looseSpotNewList);

            List<SCDemoDetail> scDemoDetailList = new ArrayList<>();
            scChannels.getScDemos().forEach(scDemo -> {
                List<SCAudienceInfo> scAudienceInfoList = scDemo.getScDemoAudData().stream()
                        .map(scDemoAudience -> new SCAudienceInfo(scDemoAudience.getWeekPart(),
                                scDemoAudience.getDayPart(),scDemoAudience.getImpact(),scDemoAudience.getRating()))
                        .collect(Collectors.toList());
                scDemoDetailList.add(new SCDemoDetail(scDemo.getDemoId(),scAudienceInfoList));
            }
            );
            spotChannels.setDemoDetail(scDemoDetailList);

            SCPackage scPackage = scChannels.getScPackage();
            SCPackageDetail scPackageDetail = new SCPackageDetail();
            scPackageDetail.setCost(scPackage.getCost());
            scPackageDetail.setFreq(scPackage.getFreq());
            scPackageDetail.setTotalSpots(scPackage.getTotalSpots());
            scPackageDetail.setSpotRate(scPackage.getSpotRate());
            scPackageDetail.setEstValue(scPackage.getEstValue());
            scPackageDetail.setDiscount(scPackage.getDiscount());

            List<SCSpotsByWeekpart> spotsByWeekpartList = scPackage.getScSpotsByWeekPart().stream()
                    .map(scSpotsByWeekpart -> new SCSpotsByWeekpart(scSpotsByWeekpart.getWeekPart(),
                            scSpotsByWeekpart.getDayPart(),scSpotsByWeekpart.getSpots())).collect(Collectors.toList());
            List<SCSplitByWeekpart> splitByWeekpartList = scPackage.getScSplitByWeekPart().stream()
                    .map(scSplitByWeekPart -> new SCSplitByWeekpart(scSplitByWeekPart.getWeekPart(),
                            scSplitByWeekPart.getPercentage())).collect(Collectors.toList());
            List<SCSplitByDaypart> splitByDaypartList = scPackage.getScSplitByDayPart().stream()
                    .map(scSplitByDaypart -> new SCSplitByDaypart(scSplitByDaypart.getDayPart(),
                            scSplitByDaypart.getPercentage())).collect(Collectors.toList());

            scPackageDetail.setSpotsByWeekpart(spotsByWeekpartList);
            scPackageDetail.setSplitByWeekpart(splitByWeekpartList);
            scPackageDetail.setSplitByDaypart(splitByDaypartList);

            spotChannels.setPackageDetail(scPackageDetail);

            List<SCEstRating> estRating = new ArrayList<>();
            List<SCEstAudience> estAudience = new ArrayList<>();
            List<SCEstRates> estCPP =  new ArrayList<>();
            List<SCEstRates> estCPT = new ArrayList<>();

            scChannels.getScEstAudData().forEach(scEstAudData -> {
                estRating.add(new SCEstRating(scEstAudData.getDemoId(),scEstAudData.getRating()));
                estAudience.add(new SCEstAudience(scEstAudData.getDemoId(),scEstAudData.getAudience()));
                estCPP.add(new SCEstRates(scEstAudData.getDemoId(),scEstAudData.getCpp()));
                estCPT.add(new SCEstRates(scEstAudData.getDemoId(), scEstAudData.getCpt()));
                    }
            );

            spotChannels.setEstRating(estRating);
            spotChannels.setEstAudience(estAudience);
            spotChannels.setEstCPP(estCPP);
            spotChannels.setEstCPT(estCPT);

            spotChannelsList.add(spotChannels);
        });
        spotChannelData.setChannels(spotChannelsList);

        return spotChannelData;
    }
    
}
