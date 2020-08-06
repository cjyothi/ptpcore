package com.dms.ptp.serviceimplementation;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.dms.ptp.exception.InvalidParamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dms.ptp.dto.AffinityChannel;
import com.dms.ptp.dto.AffinityDemo;
import com.dms.ptp.dto.InterimSCLooseSpotRate;
import com.dms.ptp.dto.SpotChannelRequest;
import com.dms.ptp.response.*;
import com.dms.ptp.service.AffinityReachService;
import com.dms.ptp.service.PricingCalculatorService;
import com.dms.ptp.service.SpotChannelService;

@Service
public class SpotChannelServiceImplementation implements SpotChannelService{
    
    @Autowired
    AffinityReachService affinityReachService;
    
    @Autowired
    PricingCalculatorService pricingCalculatorService;
    
    static Logger logger = LoggerFactory.getLogger(SpotChannelServiceImplementation.class);
    
    /**
     * Method to get the spot channel response
     * 
     * @param spotChannelRequest
     * @return SpotChannelResponse
     */
    @Override
    public SpotChannelResponse getSpotChannel(SpotChannelRequest spotChannelRequest) throws InvalidParamException {
        // Get the Affinity response by baseline id1
        BaselineResponse baseline1  = affinityReachService.getBaselineById(spotChannelRequest.getBaseline1());

       if(!validateWeekpart(spotChannelRequest.getWeekPartType(),baseline1.getWeekPart()))
           throw new InvalidParamException("Weekpart doesn't align for the baseline : "+ baseline1.getId());
        //Get affinity channel list to be used as master channel list
        List<AffinityChannel> affinityChannelList1 = baseline1.getAffinityResponse().getChannels();
        List<Integer> arBaseline1ChannelIdList=affinityChannelList1.stream().map(AffinityChannel::getId)
                .collect(Collectors.toList());
        
        // Get the Affinity response by baseline id2 if there
        List<AffinityChannel> affinityChannelList2 = new ArrayList<AffinityChannel>();
        BaselineResponse baseline2 = new BaselineResponse();
        if(spotChannelRequest.getWeekPartType().equalsIgnoreCase("WEEKDAY_END")) {
        baseline2  = affinityReachService.getBaselineById(spotChannelRequest.getBaseline2());
            if(!validateWeekpart(spotChannelRequest.getWeekPartType(),baseline1.getWeekPart(),baseline2.getWeekPart()))
                throw new InvalidParamException("Weekpart should not be same for baseline1 : "+ baseline1.getId() + " baseline2 : " +baseline2.getId());
        affinityChannelList2 = baseline2.getAffinityResponse().getChannels();
        }
        
        // Get loose spot data with baseline
        List<InterimSCLooseSpotRate> looseSpotYOYMap = getLooseSpotRateList(arBaseline1ChannelIdList,
                spotChannelRequest.getWeekPartType(), spotChannelRequest.getYoyBaseline1(),
                spotChannelRequest.getYoyBaseline2());

        List<InterimSCLooseSpotRate> looseSpotQOQMap = getLooseSpotRateList(arBaseline1ChannelIdList,
                spotChannelRequest.getWeekPartType(), spotChannelRequest.getQoqBaseline1(),
                spotChannelRequest.getQoqBaseline2());

        List<InterimSCLooseSpotRate> looseSpotNewMap = getLooseSpotRateList(arBaseline1ChannelIdList,
                spotChannelRequest.getWeekPartType(), spotChannelRequest.getBaseline1(),
                spotChannelRequest.getBaseline2());
        
        return streamSpotChannelResponse(spotChannelRequest, affinityChannelList1, affinityChannelList2, looseSpotYOYMap, looseSpotQOQMap,
                looseSpotNewMap, baseline1, baseline2);
        
    }

    private boolean validateWeekpart(String weekPartType,String baseLineWeekPart) {
        boolean valid ;
        if(weekPartType.equals("ALLDAY")){
            valid = (weekPartType.equals(baseLineWeekPart));
        }
        else{
            List<String> weekPartList =Arrays.asList("WEEKDAY","WEEKEND");
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
         * @param spotChannelRequest
         * @param affinityChannelList1
         * @param looseSpotYOYMap
         * @param looseSpotQOQMap
         * @param looseSpotNewMap
         * @return
         */
    private SpotChannelResponse streamSpotChannelResponse(SpotChannelRequest spotChannelRequest,
            List<AffinityChannel> affinityChannelList1, List<AffinityChannel> affinityChannelList2,
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
        
        
        
        affinityChannelList1.forEach(affinityChannel -> {
            
            SpotChannels spotChannels=new SpotChannels();
            
            // channel basic info
            spotChannels.setChannelId(affinityChannel.getId());
            if (affinityChannel.getResultDemo() != null)
                spotChannels.setAffinity(affinityChannel.getResultDemo().getSegment());
            spotChannels.setBouquet(affinityChannel.getTier());
            spotChannels.setName(affinityChannel.getName());

            // loose spot rates
            List<SCLooseSpotRate> scLooseSpotRatesYOY = looseSpotYOYMap.stream()
                    .filter(interimSCLooseSpotRate -> interimSCLooseSpotRate.getChannelId()==affinityChannel.getId())
                    .map(i->new SCLooseSpotRate(i.getScLooseSpotRate().getWeekPart(),i.getScLooseSpotRate().getDayPart(),i.getScLooseSpotRate().getRate()))
                    .collect(Collectors.toList());

            List<SCLooseSpotRate> scLooseSpotRatesQOQ = looseSpotQOQMap.stream()
                    .filter(interimSCLooseSpotRate -> interimSCLooseSpotRate.getChannelId()==affinityChannel.getId())
                    .map(i->new SCLooseSpotRate(i.getScLooseSpotRate().getWeekPart(),i.getScLooseSpotRate().getDayPart(),i.getScLooseSpotRate().getRate()))
                    .collect(Collectors.toList());

            List<SCLooseSpotRate> scLooseSpotRatesNew = looseSpotNewMap.stream()
                    .filter(interimSCLooseSpotRate -> interimSCLooseSpotRate.getChannelId()==affinityChannel.getId())
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
            if(spotChannelRequest.getWeekPartType().equalsIgnoreCase("WEEKDAY_END")) {
                SCSpotsByWeekpart sCSpotsByWeekDayOP = new SCSpotsByWeekpart();
                sCSpotsByWeekDayOP.setWeekPart("WEEKDAY");
                sCSpotsByWeekDayOP.setDayPart("OP");
                sCSpotsByWeekDayOP.setSpots(0);
                SCSpotsByWeekpart sCSpotsByWeekDayPT = new SCSpotsByWeekpart();
                sCSpotsByWeekDayPT.setWeekPart("WEEKDAY");
                sCSpotsByWeekDayPT.setDayPart("PT");
                sCSpotsByWeekDayPT.setSpots(0);
                SCSpotsByWeekpart sCSpotsByWeekEndPT = new SCSpotsByWeekpart();
                sCSpotsByWeekEndPT.setWeekPart("WEEKEND");
                sCSpotsByWeekEndPT.setDayPart("PT");
                sCSpotsByWeekEndPT.setSpots(0);
                SCSpotsByWeekpart sCSpotsByWeekEndOP = new SCSpotsByWeekpart();
                sCSpotsByWeekEndOP.setWeekPart("WEEKEND");
                sCSpotsByWeekEndOP.setDayPart("OP");
                sCSpotsByWeekEndOP.setSpots(0);
                spotsByWeekpartList.add(sCSpotsByWeekDayOP);
                spotsByWeekpartList.add(sCSpotsByWeekDayPT);
                spotsByWeekpartList.add(sCSpotsByWeekEndPT);
                spotsByWeekpartList.add(sCSpotsByWeekEndOP);
            }else {
                SCSpotsByWeekpart sCSpotsByAlldayOP = new SCSpotsByWeekpart();
                sCSpotsByAlldayOP.setWeekPart("ALLDAY");
                sCSpotsByAlldayOP.setDayPart("OP");
                sCSpotsByAlldayOP.setSpots(0);
                SCSpotsByWeekpart sCSpotsByAllDayPT = new SCSpotsByWeekpart();
                sCSpotsByAllDayPT.setWeekPart("ALLDAY");
                sCSpotsByAllDayPT.setDayPart("PT");
                sCSpotsByAllDayPT.setSpots(0);
                spotsByWeekpartList.add(sCSpotsByAlldayOP);
                spotsByWeekpartList.add(sCSpotsByAllDayPT);
            }
            packageDetails.setSpotsByWeekpart(spotsByWeekpartList);
            
            // set split by week part
            List<SCSplitByWeekpart> splitByWeekPartList = new ArrayList<>();
            if(spotChannelRequest.getWeekPartType().equalsIgnoreCase("WEEKDAY_END")) {
                SCSplitByWeekpart weekEndShare = new SCSplitByWeekpart();
                weekEndShare.setWeekPart("WEEKEND");
                weekEndShare.setPercentage(0);
                SCSplitByWeekpart weekDayShare = new SCSplitByWeekpart();
                weekDayShare.setWeekPart("WEEKDAY");
                weekDayShare.setPercentage(0);
                splitByWeekPartList.add(weekDayShare);
                splitByWeekPartList.add(weekEndShare);
            }else {
                SCSplitByWeekpart allDayShare = new SCSplitByWeekpart();
                allDayShare.setWeekPart("ALLDAY");
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
            
            // estimated rating list
            List<SCEstRating> estRatingList = new ArrayList<>();
            resultDemoList.forEach(rd -> estRatingList.add(new SCEstRating(rd,0)));
            
            // estimated audience list
            List<SCEstAudience> estAudienceList = new ArrayList<>();
            resultDemoList.forEach(rd -> estAudienceList.add(new SCEstAudience(rd,0)));
            
            // estimated CPP rate list
            List<SCEstRates> estCPPRateList = new ArrayList<>();
            resultDemoList.forEach(rd -> estCPPRateList.add(new SCEstRates(rd,0)));
            
            // estimated CPT rate list
            List<SCEstRates> estCPTRateList = new ArrayList<>();
            resultDemoList.forEach(rd -> estCPTRateList.add(new SCEstRates(rd,0)));
            
            spotChannels.setEstRating(estRatingList);
            spotChannels.setEstAudience(estAudienceList);
            spotChannels.setEstCPP(estCPPRateList);
            spotChannels.setEstCPT(estCPTRateList);
            
            // set demo detail list
            List<SCDemoDetail> demoDetailList = getDemoDetailList(affinityChannel,baseline1,baseline2);
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
        spotChannelResponse.setWeekPartType(spotChannelRequest.getWeekPartType());
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
            throw new InvalidParamException("Weekpart doesn't align for the baseline : "+ pricingBaseline1.getId());


        pricingBaseline1.getChannels().forEach(validChannel->{
            if (channelIdList.contains(validChannel.getId())) {
                List<CalculatedRate> calculatedRateList=validChannel.getCalculatedRate();
                calculatedRateList.forEach(calculatedRate ->
                        interimSCLooseSpotRateList.add(new InterimSCLooseSpotRate(validChannel.getId()
                                , new SCLooseSpotRate(pricingBaseline1.getWeekPart(), calculatedRate.getId(), calculatedRate.getRate()))));

            }
        });

        PricingBaseline pricingBaseline2;
        if (weekPartType.equalsIgnoreCase("WEEKDAY_END")) {
            pricingBaseline2=pricingCalculatorService.getLooseSpot(baseline2);
            if(!validateWeekpart(weekPartType,pricingBaseline1.getWeekPart(),pricingBaseline2.getWeekPart()))
                throw new InvalidParamException("Weekpart should not be same for baseline1 : "+ pricingBaseline1.getId() + " baseline2 : " +pricingBaseline2.getId());
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
     * @param affinityChannel,baseline1,baseline2
     * @return scDemoDetailList
     */
    private List<SCDemoDetail> getDemoDetailList(AffinityChannel affinityChannel, BaselineResponse baseline1,
            BaselineResponse baseline2){
        
        List<SCDemoDetail> scDemoDetailList1 = new ArrayList<>();
        List<SCDemoDetail> scDemoDetailList2 = new ArrayList<>();
        List<SCDemoDetail> resultSCDemoDetailList = new ArrayList<>();

        affinityChannel.getDemos().forEach(demo-> {
            SCDemoDetail scDemoDetail = new SCDemoDetail();
            scDemoDetail.setId(demo.getId());
            List<SCAudienceInfo> sCAudienceInfoList = new ArrayList<>();
            demo.getDaypart().forEach(dp-> {
                SCAudienceInfo sCAudienceInfo = new SCAudienceInfo();
                sCAudienceInfo.setWeekPart(baseline1.getWeekPart());
                sCAudienceInfo.setDayPart(dp.getId());
                sCAudienceInfo.setImpact(dp.getImpact());
                sCAudienceInfo.setRating(dp.getRating());
                sCAudienceInfoList.add(sCAudienceInfo);
                scDemoDetail.setAudienceInfo(sCAudienceInfoList);
            });

            scDemoDetailList1.add(scDemoDetail);
        });


        if(baseline2.getAffinityResponse()!=null) {
            List<AffinityChannel> affinityChannelList2 = baseline2.getAffinityResponse().getChannels();
            for(AffinityChannel affCh : affinityChannelList2) {
                
                if(affCh.getId()==affinityChannel.getId()) {
                    
                    affCh.getDemos().forEach(demo-> {
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
                    //concatination for same demoId
                    scDemoDetailList1.forEach(scDemoDetail -> {
                        scDemoDetailList2Copy.forEach(scDemoDetail2->{
                            if(scDemoDetail.getId()==scDemoDetail2.getId()){
                                scDemoDetail.getAudienceInfo().addAll(scDemoDetail2.getAudienceInfo());
                                scDemoDetailList2.remove(scDemoDetail2);
                            }
                        });
                    });
                }
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
    
}
