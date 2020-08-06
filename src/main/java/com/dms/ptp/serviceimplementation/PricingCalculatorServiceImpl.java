package com.dms.ptp.serviceimplementation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.dms.ptp.dto.AffinityChannel;
import com.dms.ptp.dto.AffinityDemo;
import com.dms.ptp.dto.DaypartData;
import com.dms.ptp.dto.InterimAudienceData;
import com.dms.ptp.dto.InterimChannel;
import com.dms.ptp.dto.LMKRateRequest;
import com.dms.ptp.dto.LmkAudienceRequest;
import com.dms.ptp.dto.LmkAudienceResponse;
import com.dms.ptp.dto.LmkRateResponse;
import com.dms.ptp.dto.PricingCreateRequest;
import com.dms.ptp.dto.PricingRequest;
import com.dms.ptp.dto.PricingUpdateRequest;
import com.dms.ptp.dto.RateAudienceData;
import com.dms.ptp.dto.RateS1;
import com.dms.ptp.dto.SecondryDemo;
import com.dms.ptp.entity.Baseline;
import com.dms.ptp.entity.Channel;
import com.dms.ptp.entity.LooseSpot;
import com.dms.ptp.entity.LooseSpotAudienceForecast;
import com.dms.ptp.entity.LooseSpotAudienceForecastVariance;
import com.dms.ptp.entity.LooseSpotAudienceYOY;
import com.dms.ptp.entity.LooseSpotChannel;
import com.dms.ptp.entity.LooseSpotDeliveryInputs;
import com.dms.ptp.entity.LooseSpotRate;
import com.dms.ptp.entity.LooseSpotRateQOQ;
import com.dms.ptp.entity.LooseSpotRateYOY;
import com.dms.ptp.entity.LooseSpotSellout;
import com.dms.ptp.entity.LooseSpotSource;
import com.dms.ptp.exception.BaselineNotFoundException;
import com.dms.ptp.exception.InvalidLooseSpotIdException;
import com.dms.ptp.repository.BaselineRepository;
import com.dms.ptp.repository.ChannelDemoRepository;
import com.dms.ptp.repository.LooseSpotChannelRepository;
import com.dms.ptp.repository.LooseSpotDeliveryInputsRepository;
import com.dms.ptp.repository.LooseSpotRepository;
import com.dms.ptp.response.AudienceDaypart;
import com.dms.ptp.response.AudienceForecast;
import com.dms.ptp.response.AudienceYOY;
import com.dms.ptp.response.CalculatedRate;
import com.dms.ptp.response.DeliveryCurrencyInput;
import com.dms.ptp.response.LooseSpotResponse;
import com.dms.ptp.response.PricingBaseline;
import com.dms.ptp.response.PricingChannel;
import com.dms.ptp.response.PricingResponse;
import com.dms.ptp.response.RateQOQ;
import com.dms.ptp.response.RateYOY;
import com.dms.ptp.response.SellOutData;
import com.dms.ptp.response.SourceData;
import com.dms.ptp.response.VarianceYOY;
import com.dms.ptp.service.PricingCalculatorService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PricingCalculatorServiceImpl implements PricingCalculatorService {

	RestTemplate restTemplate = new RestTemplate();

	static Logger logger = LoggerFactory.getLogger(PricingCalculatorServiceImpl.class);

	@Autowired
	ChannelDemoRepository channelDemoRepository;

	@Autowired
	LooseSpotRepository looseSpotRepo;

	@Autowired
	LooseSpotChannelRepository looseSpotChannelRepo;

	@Autowired
	LooseSpotDeliveryInputsRepository deliveryInputRepo;

	@Autowired
	BaselineRepository baselineRepository;

	@Override
	public PricingResponse getPricingCalculation(PricingRequest pricingRequest) {

		PricingResponse priceResponse = new PricingResponse();

		/*
		 * 1. Get the channel Data based on territory and platform from Affinity Reach
		 * calculator. Get list of lmkrefno to pass to LMK API
		 */

		List<Channel> channelList = channelDemoRepository.findByTerritory_IdAndPlatform_Id(
				pricingRequest.getArResult().getTerritory(), pricingRequest.getArResult().getPlatform());
		List<InterimChannel> interimChannels = new ArrayList<>();
		List<Integer> lmkRefNoList = new ArrayList<>();
		channelList.forEach(channel -> {
			List<String> genreNames = new ArrayList<>();
			channel.getGenres().forEach(genre -> genreNames.add(genre.getName()));
			interimChannels.add(new InterimChannel(channel.getId(), channel.getName(), channel.getPackageName(),
					channel.getNetwork(), genreNames, channel.getLmkRefNo()));
			lmkRefNoList.add(channel.getLmkRefNo());
		});

		/*
		 * 2. Get Sellout and Rates of YOY from LMK API for each channel.
		 */

		List<LmkRateResponse> rateYOYList = getRateData(lmkRefNoList, pricingRequest.getYoyStart(),
				pricingRequest.getYoyEnd());

		/*
		 * 3. Get Sellout and Rates of QOQ from LMK API for each channel.
		 */

		List<LmkRateResponse> rateQOQList = getRateData(lmkRefNoList, pricingRequest.getQoqStart(),
				pricingRequest.getQoqEnd());

		logger.info("****************Calculation Started**********************");

		/*
		 * Get SecondryDemo of each channel
		 */
		List<SecondryDemo> secDemoList = getSecDemo(pricingRequest, interimChannels);

		/*
		 * 4. Calculate Rate S1 (primaryDemo)
		 */

		List<RateS1> s1ResultList = getS1Result(pricingRequest, interimChannels);

		/*
		 * 5. Calculate Reat S2 (Result SecondryDemo)
		 */

		List<RateS1> s2ResultList = getS2Result(pricingRequest, interimChannels);

		/*
		 * Calculate Rate S3 (Based on Demand)
		 */

		List<RateS1> s3ResultList = getS3Result(rateYOYList, pricingRequest, s2ResultList);

		/*
		 * Calulate PT Rate : RoundOf(S3)
		 */

		List<RateS1> roundOfPTRate = getPTRate(s3ResultList);

		/*
		 * Calculate OP Rate
		 */

		List<RateS1> opResultList = getOPResult(pricingRequest, interimChannels);

		/*
		 * Calculate RateInflaction (YOY)
		 */

		List<RateS1> inflatYOYList = geRatetInflation(rateYOYList, roundOfPTRate);

		/*
		 * Calculate RateInflaction (QOQ)
		 */

		List<RateS1> inflatQOQList = geRatetInflation(rateQOQList, roundOfPTRate);

		/*
		 * Calculate primaryDemo YOY audienceData
		 */

		List<LmkAudienceResponse> lmkAudienceResponse = getLmkAudienceData(lmkRefNoList, pricingRequest);
		List<LmkAudienceResponse> completeList = getCompleteLmkAudience(lmkAudienceResponse);
		List<RateAudienceData> rateAudienceList = getPrimDemoYOYAudience(completeList, rateYOYList, "PT");

		/*
		 * forecasted Rating and audience Calculate primaryDemo QOQ audienceData
		 */

		List<RateAudienceData> forcastRateAudiencePTList = getPrimDemoYOYAudience(completeList, rateQOQList, "PT");
		List<RateAudienceData> forcastRateAudienceOPList = getPrimDemoYOYAudience(completeList, rateQOQList, "OP");

		/*
		 * Percentage change between primaryDemo YOY audienceData and forecasted Rating
		 * and audience Calculate primaryDemo QOQ audienceData
		 */
		List<RateAudienceData> percentChangeList = changeOfForcastNprimDemo(rateAudienceList,
				forcastRateAudiencePTList);

		/*
		 * Calculate all primaryDemo _Secndr from ARResult QOQ audienceData (PT data)
		 */

		logger.info("*********Caluclation to get Source Data**********");
		ArrayList<Integer> targetMarket = new ArrayList<>();
		for (Integer pdemo : pricingRequest.getArResult().getPriDemos()) {
			if (pdemo != pricingRequest.getDemo())
				targetMarket.add(pdemo);
		}
		if (!pricingRequest.getArResult().getSecDemos().isEmpty()) {
			targetMarket.addAll(pricingRequest.getArResult().getSecDemos());
		}
		List<LmkAudienceResponse> lmkAudienceResponseForARDemo = getLmkAudienceDataForARDemo(lmkRefNoList,
				pricingRequest, targetMarket);
		List<RateAudienceData> sourceDataPTList = getSourceData(lmkAudienceResponseForARDemo, targetMarket, rateQOQList,
				"PT");

		logger.info("****************Calculation Completed**********************");
		logger.info("****************Forming Response **************************");

		priceResponse.setBaseRate(pricingRequest.getBaseRate());
		priceResponse.setDelivery(pricingRequest.getDelivery());
		priceResponse.setDeliveryInputs(pricingRequest.getDeliveryInputs());
		priceResponse.setDemo(pricingRequest.getDemo());
		priceResponse.setQoqEnd(pricingRequest.getQoqEnd());
		priceResponse.setQoqStart(pricingRequest.getQoqStart());
		priceResponse.setYoyEnd(pricingRequest.getYoyEnd());
		priceResponse.setYoyStart(pricingRequest.getYoyStart());
		priceResponse.setSoHigh(pricingRequest.getSoHigh());
		priceResponse.setSoLow(pricingRequest.getSoLow());
		priceResponse.setRateDecr(pricingRequest.getRateDecr());
		priceResponse.setRateIncr(pricingRequest.getRateIncr());

		List<PricingChannel> priceChannelList = new ArrayList<>();
		interimChannels.forEach(channel -> {
			PricingChannel channelDetail = new PricingChannel();
			channelDetail.setId(channel.getId());
			channelDetail.setName(channel.getName());
			channelDetail.setTier(channel.getTier());
			channelDetail.setGenre(channel.getGenre());
			channelDetail.setNetwork(channel.getNetwork());
			/*
			 * set RateYOY
			 */
			List<RateYOY> ryList = new ArrayList<>();
			rateYOYList.forEach(rateYOY -> {
				if (rateYOY.getSalesAreaNo() == channel.getLmkRefNo()) {
					RateYOY ry = new RateYOY();
					ry.setId(rateYOY.getDaypart());
					ry.setRate(rateYOY.getBreakPrice());
					ryList.add(ry);
				}
				channelDetail.setRatesYOY(ryList);
			});
			/*
			 * set RateQOQ
			 */
			List<RateQOQ> rqList = new ArrayList<>();
			rateQOQList.forEach(rateQOQ -> {
				if (rateQOQ.getSalesAreaNo() == channel.getLmkRefNo()) {
					RateQOQ rq = new RateQOQ();
					rq.setId(rateQOQ.getDaypart());
					rq.setRate(rateQOQ.getBreakPrice());
					rqList.add(rq);
				}
				channelDetail.setRatesQOQ(rqList);
			});

			/*
			 * Set S1
			 */
			s1ResultList.forEach(s1 -> {
				if (s1.getLmkRefNo() == channel.getLmkRefNo()) {
					channelDetail.setS1(s1.getResult());
				}
			});

			/*
			 * Set S2
			 */

			s2ResultList.forEach(s2 -> {
				if (s2.getLmkRefNo() == channel.getLmkRefNo()) {
					channelDetail.setS2(s2.getResult());
				}
			});

			/*
			 * Set S3
			 */

			s3ResultList.forEach(s3 -> {
				if (s3.getLmkRefNo() == channel.getLmkRefNo()) {
					channelDetail.setS3(s3.getResult());
				}
			});

			/*
			 * set secDemoId
			 */
			secDemoList.forEach(secDemo -> {
				if (secDemo.getLmkRefNo() == channel.getLmkRefNo()) {
					channelDetail.setSecDemoId(secDemo.getResult());
				}
			});

			/*
			 * Set rateInflactionQOQ
			 */
			inflatQOQList.forEach(iq -> {
				if (iq.getLmkRefNo() == channel.getLmkRefNo()) {
					channelDetail.setRateInflationQOQ(iq.getResult());
				}
			});

			/*
			 * Set rateInflactionYOY
			 */
			inflatYOYList.forEach(iy -> {
				if (iy.getLmkRefNo() == channel.getLmkRefNo()) {
					channelDetail.setRateInflationQOQ(iy.getResult());
				}
			});

			/*
			 * Set Calculated Rate
			 */

			List<CalculatedRate> calculatedRateList = new ArrayList<>();

			roundOfPTRate.forEach(ptList -> {
				if (ptList.getLmkRefNo() == channel.getLmkRefNo()) {
					CalculatedRate cr = new CalculatedRate();
					cr.setId("PT");
					cr.setRate(ptList.getResult());
					calculatedRateList.add(cr);
				}

			});

			opResultList.forEach(opList -> {
				if (opList.getLmkRefNo() == channel.getLmkRefNo()) {
					CalculatedRate cr = new CalculatedRate();
					cr.setId("OP");
					cr.setRate(opList.getResult());
					calculatedRateList.add(cr);
				}

			});
			channelDetail.setCalculatedRate(calculatedRateList);

			/*
			 * set AudienceYOY
			 */
			rateAudienceList.forEach(ral -> {
				AudienceYOY ay = new AudienceYOY();
				if (ral.getChannelId() == channel.getId()) {
					ay.setAudinece(ral.getAudience());
					ay.setCpp(ral.getCpp());
					ay.setCpt(ral.getCpt());
					ay.setRating(ral.getRating());
					channelDetail.setAudienceYOY(ay);
				}

			});

			/*
			 * set Sellout
			 */
			List<SellOutData> sdList = new ArrayList<>();
			rateQOQList.forEach(rateQOQ -> {
				if (rateQOQ.getSalesAreaNo() == channel.getLmkRefNo()) {
					SellOutData rq = new SellOutData();
					rq.setId(rateQOQ.getDaypart());
					rq.setSellout(rateQOQ.getSellOut());
					sdList.add(rq);
				}
				channelDetail.setSelloutData(sdList);
			});

			/*
			 * set audiance forcaste
			 */
			AudienceForecast forCast = new AudienceForecast();
			List<AudienceDaypart> adList = new ArrayList<>();
			forcastRateAudiencePTList.forEach(fral -> {
				if (fral.getSalesAreaNo() == channel.getLmkRefNo()) {
					AudienceDaypart ad = new AudienceDaypart();
					ad.setId(fral.getDayPart());
					ad.setAudinece(fral.getAudience());
					ad.setRating(fral.getRating());
					ad.setCpp(fral.getCpp());
					ad.setCpt(fral.getCpt());
					adList.add(ad);
				}
			});
			forCast.setAudienceDaypart(adList);
			VarianceYOY vy = new VarianceYOY();
			percentChangeList.forEach(pcl -> {
				if (pcl.getSalesAreaNo() == channel.getLmkRefNo()) {
					vy.setAudinece(pcl.getAudience());
					if (!(Double.isNaN(pcl.getCpp())) && !(Double.isNaN(pcl.getCpt()))) {
						vy.setCpp(pcl.getCpp());
						vy.setCpt(pcl.getCpt());
					}
					vy.setRating(pcl.getRating());
				}
			});
			forCast.setVarianceYOY(vy);
			channelDetail.setAudienceForecast(forCast);
			priceChannelList.add(channelDetail);

			/*
			 * set Source Data
			 */

			List<SourceData> sDataList = new ArrayList<>();
			sourceDataPTList.forEach(sdpt -> {
				if (sdpt.getSalesAreaNo() == channel.getLmkRefNo()) {
					SourceData sd = new SourceData();
					sd.setAudience(sdpt.getAudience());
					sd.setRating(sdpt.getRating());
					sd.setCpp(sdpt.getCpp());
					sd.setCpt(sdpt.getCpt());
					sd.setDemoId(sdpt.getDemoId());
					sDataList.add(sd);
				}
			});
			channelDetail.setSourceData(sDataList);

		});

		priceResponse.setChannels(priceChannelList);
		return priceResponse;

	}

	List<LmkAudienceResponse> getCompleteLmkAudience(List<LmkAudienceResponse> lmkAudienceResponse) {

		List<LmkAudienceResponse> list1 = lmkAudienceResponse.stream().filter(lar -> lar.getWeekPart() != null)
				.collect(Collectors.toList());

		List<LmkAudienceResponse> list2 = new ArrayList<>();
		for (LmkAudienceResponse lar : lmkAudienceResponse) {
			if (lar.getWeekPart() == null) {
				list2.add(new LmkAudienceResponse(lar.getChannelNo(), lar.getDemoNo(), null, "PT", 0.0, 0.0, 0.0));
				list2.add(new LmkAudienceResponse(lar.getChannelNo(), lar.getDemoNo(), null, "OP", 0.0, 0.0, 0.0));
			}

		}
		List<LmkAudienceResponse> list3 = new ArrayList<>();
		list3.addAll(list1);
		list3.addAll(list2);
		return list3;
	}

	/**
	 * Find the %Change varaince between primaryDemo YOY audienceData and forecasted
	 * Rating and audience (forecast-primarydemo/forecast)
	 * 
	 * @param rateAudienceList
	 * @param forcastRateAudiencePTList
	 * @return
	 */

	List<RateAudienceData> changeOfForcastNprimDemo(List<RateAudienceData> rateAudienceList,
			List<RateAudienceData> forcastRateAudiencePTList) {

		List<RateAudienceData> rateAuDataList = new ArrayList<>();
		forcastRateAudiencePTList.forEach((frdl) -> {
			rateAudienceList.forEach((ral) -> {
				RateAudienceData rateAudienceData = new RateAudienceData();
				if (frdl.getSalesAreaNo() == ral.getSalesAreaNo()) {
					if (frdl.getAudience() == 0.0 && frdl.getRating() == 0.0 && frdl.getCpp() == 0.0
							&& frdl.getCpt() == 0.0) {
						rateAudienceData.setChannelId(frdl.getChannelId());
						rateAudienceData.setSalesAreaNo(frdl.getSalesAreaNo());
						rateAudienceData.setAudience(0.0);
						rateAudienceData.setRating(0.0);
						rateAudienceData.setCpp(0.0);
						rateAudienceData.setCpt(0.0);
					} else {
						double audience = (frdl.getAudience() - ral.getAudience()) / frdl.getAudience();
						double rating = (frdl.getRating() - ral.getRating()) / frdl.getRating();
						double cpp = (frdl.getCpp() - ral.getCpp()) / frdl.getCpp();
						double cpt = (frdl.getCpt() - ral.getCpt()) / frdl.getCpt();

						rateAudienceData.setChannelId(ral.getChannelId());
						rateAudienceData.setSalesAreaNo(frdl.getSalesAreaNo());
						rateAudienceData.setAudience(audience);
						rateAudienceData.setRating(rating);
						rateAudienceData.setCpp(cpp);
						rateAudienceData.setCpt(cpt);
					}
					rateAuDataList.add(rateAudienceData);
				}

			});
		});

		return rateAuDataList;

	}

	public List<RateAudienceData> getSourceData(List<LmkAudienceResponse> lmkAudienceResponseList,
			List<Integer> demoList, List<LmkRateResponse> rateQOQList, String dayPart) {

		// Filtered LMKAudienceResponse List for valid weekpart and daypart values
		List<LmkAudienceResponse> validAudienceDataList = lmkAudienceResponseList.stream()
				.filter(l -> l.getWeekPart() != null && l.getDayPart() != null).collect(Collectors.toList());

		List<LmkAudienceResponse> audiencePT = validAudienceDataList.stream()
				.filter(al -> al.getDayPart().equalsIgnoreCase("PT")).collect(Collectors.toList());

		List<LmkRateResponse> qoqPT = rateQOQList.stream().filter(al -> al.getDaypart().equalsIgnoreCase("PT"))
				.collect(Collectors.toList());

		List<LmkAudienceResponse> channelAudiencePT = new ArrayList<>();

		for (LmkAudienceResponse c : audiencePT) {
			for (LmkRateResponse lr : qoqPT) {
				for (Integer demoId : demoList) {
					if (c.getChannelNo() == lr.getSalesAreaNo()) {
						if (c.getDemoNo() == demoId) {
							if (c.getDayPart().equalsIgnoreCase(dayPart)) {
								channelAudiencePT.add(c);
							}
						}
					}

				}
			}
		}

		List<RateAudienceData> rateAuDataList = new ArrayList<>();
		for (LmkRateResponse lmkRate : qoqPT) {
			for (LmkAudienceResponse lmkaudience : audiencePT) {
				RateAudienceData rateAudienceData = new RateAudienceData();
				int lmkrefNo = lmkaudience.getChannelNo();
				if (lmkrefNo == lmkRate.getSalesAreaNo()) {
					rateAudienceData.setChannelId(lmkrefNo);
					rateAudienceData.setSalesAreaNo(lmkRate.getSalesAreaNo());
					rateAudienceData.setAudience(lmkaudience.getAvgImpacts());
					rateAudienceData.setRating(lmkaudience.getAvgRatings());
					rateAudienceData.setDayPart(lmkaudience.getDayPart());
					rateAudienceData.setDemoId(lmkaudience.getDemoNo());
					if (lmkaudience.getAvgRatings() == 0.0 && lmkaudience.getAvgImpacts() == 0.0) {
						rateAudienceData.setCpp(0.0);
						rateAudienceData.setCpt(0.0);
					} else {
						rateAudienceData.setCpp(lmkRate.getBreakPrice() / lmkaudience.getAvgRatings());
						rateAudienceData.setCpt(lmkRate.getBreakPrice() / lmkaudience.getAvgImpacts());
					}
					rateAuDataList.add(rateAudienceData);
				}

			}

		}
		return rateAuDataList;
	}

	/**
	 * This method returns the channelId based on lmkrefno.
	 * 
	 * @param lmkref
	 * @return
	 */
	int getchannel(int lmkref) {
		int refNo = channelDemoRepository.findByChannelId(lmkref);
		return refNo;
	}

	/**
	 * Calculate primaryDemo YOY audienceData -- Get audinece and rating from LMK
	 * ingress/audinece API. -- CPP = YOY PT rate/rating from LMK ingress/audinece
	 * API -- CPT = YOY PT rate/audience from LMK ingress/audinece API *1000
	 * 
	 * @param lmkAudienceResponse
	 * @param rateYOYList
	 * @return
	 */
	private List<RateAudienceData> getPrimDemoYOYAudience(List<LmkAudienceResponse> lmkAudienceResponse,
			List<LmkRateResponse> rateYOYList, String dayPart) {

		List<RateAudienceData> rateAuDataList = new ArrayList<>();

		List<LmkRateResponse> ptListformRateY = rateYOYList.stream()
				.filter(rl -> rl.getDaypart() != null && rl.getDaypart().equalsIgnoreCase(dayPart))
				.collect(Collectors.toList());

		List<LmkAudienceResponse> ptListformLmkAudience = lmkAudienceResponse.stream()
				.filter(rl -> rl.getDayPart() != null && rl.getDayPart().equalsIgnoreCase(dayPart))
				.collect(Collectors.toList());

		for (LmkRateResponse lmkRate : ptListformRateY) {
			for (LmkAudienceResponse lmkaudience : ptListformLmkAudience) {
				RateAudienceData rateAudienceData = new RateAudienceData();
				if (lmkaudience.getChannelNo() == lmkRate.getSalesAreaNo()) {
					rateAudienceData.setChannelId(getchannel(lmkaudience.getChannelNo()));
					rateAudienceData.setSalesAreaNo(lmkRate.getSalesAreaNo());
					rateAudienceData.setAudience(lmkaudience.getAvgImpacts());
					rateAudienceData.setRating(lmkaudience.getAvgRatings());
					rateAudienceData.setDayPart(lmkaudience.getDayPart());
					if (lmkaudience.getAvgRatings() == 0.0 && lmkaudience.getAvgImpacts() == 0.0) {
						rateAudienceData.setCpp(0.0);
						rateAudienceData.setCpt(0.0);
					} else {
						rateAudienceData.setCpp(lmkRate.getBreakPrice() / lmkaudience.getAvgRatings());
						rateAudienceData.setCpt(lmkRate.getBreakPrice() / lmkaudience.getAvgImpacts());
					}
					rateAuDataList.add(rateAudienceData);
				}

			}
		}
		return rateAuDataList;
	}

	/**
	 * Calculate RateInflaction (YOY) -- (value of PT Rate - YOY rate from LMK)/YOY
	 * rate from LMK (Based on channelId) Calculate RateInflaction (QOQ) -- (value
	 * of PT Rate - QOQ rate from LMK)/QOQ rate from LMK (Based on channelId)
	 * 
	 * @param rateYOList
	 * @param ptRateList
	 * @return
	 */
	private List<RateS1> geRatetInflation(List<LmkRateResponse> rateYOList, List<RateS1> ptRateList) {
		List<RateS1> inflatList = new ArrayList<>();
		for (LmkRateResponse rateYOY : rateYOList) {
			double inflatYOYdiff = 0.0;
			for (RateS1 ptRate : ptRateList) {
				if (rateYOY.getSalesAreaNo() == ptRate.getLmkRefNo() && rateYOY.getDaypart().equalsIgnoreCase("PT")) {
					RateS1 rate = new RateS1();
					if (rateYOY.getBreakPrice() != 0.0) {
						inflatYOYdiff = (ptRate.getResult() - rateYOY.getBreakPrice()) / rateYOY.getBreakPrice();
					}

					rate.setChannelId(ptRate.getChannelId());
					rate.setLmkRefNo(ptRate.getLmkRefNo());
					rate.setResult(inflatYOYdiff);
					inflatList.add(rate);
				}
			}
		}
		return inflatList;
	}

	/**
	 * Calculate OP Rate : -- Get Impact : Compare the channleId of Pricing with
	 * channelId of afResponse and Get the ID of resultDemo from afResponse and Get
	 * the OP inside the daypart of demo from afResponse -- Impact *
	 * deliveryInputs->tmsRate/1000(based on channel tier)
	 * 
	 * @param pricingRequest
	 * @param interimChannels
	 * @return
	 */
	private List<RateS1> getOPResult(PricingRequest pricingRequest, List<InterimChannel> interimChannels) {

		List<AffinityChannel> channelsFromAr = pricingRequest.getArResult().getChannels();
		List<InterimAudienceData> interimAudienceData1 = new ArrayList<>();

		for (AffinityChannel chd : channelsFromAr) {
			for (AffinityDemo dmd : chd.getDemos()) {
				if (chd.getResultDemo() != null) {
					if (dmd.getId() == chd.getResultDemo().getId()) {
						List<DaypartData> dayPartList = dmd.getDaypart();
						if (dayPartList.isEmpty()) {
							InterimAudienceData idd = new InterimAudienceData();
							idd.setDemoNo(dmd.getId());
							idd.setChannelNo(chd.getId());
							idd.setAvgImpact(0);
							interimAudienceData1.add(idd);

						} else {
							for (DaypartData dayPart : dayPartList) {
								InterimAudienceData idd = new InterimAudienceData();
								if (dayPart.getId().equalsIgnoreCase("OP")) {
									idd.setDemoNo(dmd.getId());
									idd.setChannelNo(chd.getId());
									idd.setAvgImpact(dayPart.getImpact());
									interimAudienceData1.add(idd);
								}

							}
						}
					}
				}
				/*
				 * else { InterimAudienceData idd = new InterimAudienceData(); idd.setDemoNo(0);
				 * idd.setChannelNo(chd.getId()); idd.setAvgImpact(0);
				 * interimAudienceData1.add(idd);
				 * 
				 * }
				 */
			}
		}

		List<RateS1> opResultList = new ArrayList<RateS1>();

		for (InterimChannel chd : interimChannels) {
			for (InterimAudienceData idd1 : interimAudienceData1) {
				RateS1 s1data = new RateS1();
				if (idd1.getChannelNo() == chd.getId()) {
					s1data.setChannelId(chd.getId());
					s1data.setLmkRefNo(chd.getLmkRefNo());
					for (DeliveryCurrencyInput deliveryInput : pricingRequest.getDeliveryInputs()) {
						if (deliveryInput.getTier().equalsIgnoreCase(chd.getTier())) {
							s1data.setResult((idd1.getAvgImpact() * deliveryInput.getTmsRate()) * 0.001);
						}
					}
					opResultList.add(s1data);
				}

			}
		}

		return opResultList;

	}

	/**
	 * Calulate PT Rate : RoundOf(S3)
	 * 
	 * @param s3
	 * @return
	 */
	private List<RateS1> getPTRate(List<RateS1> s3) {
		List<RateS1> ptRateList = new ArrayList<>();
		for (RateS1 rates : s3) {
			RateS1 ptRate = new RateS1();
			ptRate.setChannelId(rates.getChannelId());
			ptRate.setLmkRefNo(rates.getLmkRefNo());
			ptRate.setResult(Math.round(rates.getResult()));
			ptRateList.add(ptRate);
		}
		return ptRateList;
	}

	/**
	 * Calculate Rate S3 (Based on Demand) -- Get the sellout percentage based on
	 * channelId from LMK(Refer 2) and compare with soHigh and soLow -- if LMK
	 * sellout > soHigh then S3= S2 + S2 of rateIncr -- if LMK sellout < soLow then
	 * S3= S2 - S2 of rateDecr -- if LMK sellout in(soHigh,soLow) then S3 = S2
	 * 
	 * @param rateYList
	 * @param pricingRequest
	 * @param s2ResultList
	 * @return
	 */
	private List<RateS1> getS3Result(List<LmkRateResponse> rateYList, PricingRequest pricingRequest,
			List<RateS1> s2ResultList) {

		List<RateS1> s3List = new ArrayList<>();
		for (LmkRateResponse sellout : rateYList)// 60 records
		{
			for (RateS1 s2Result : s2ResultList)// 30 records
			{
				if (sellout.getSalesAreaNo() == s2Result.getLmkRefNo()) {
					RateS1 rates = new RateS1();
					double s3 = 0.0;
					if (sellout.getDaypart().equalsIgnoreCase("PT")) {
						if (sellout.getSellOut() >= pricingRequest.getSoHigh()) {
							s3 = s2Result.getResult() + s2Result.getResult() * pricingRequest.getRateIncr() * 0.01;
						} else if (sellout.getSellOut() <= pricingRequest.getSoLow()) {
							s3 = s2Result.getResult() - s2Result.getResult() * pricingRequest.getRateDecr() * 0.01;
						} else {
							s3 = s2Result.getResult();
						}
						rates.setResult(s3);
						rates.setChannelId(s2Result.getChannelId());
						rates.setLmkRefNo(s2Result.getLmkRefNo());
						s3List.add(rates);
					}

				}

			}
		}
		return s3List;
	}

	/**
	 * Get the secDemo based on channelId which is the id of ResultDemo
	 * 
	 * @param pricingRequest
	 * @param interimChannels
	 * @return
	 */
	private List<SecondryDemo> getSecDemo(PricingRequest pricingRequest, List<InterimChannel> interimChannels) {
		List<AffinityChannel> channelsFromAr = pricingRequest.getArResult().getChannels();

		List<SecondryDemo> secDemoList = new ArrayList<SecondryDemo>();
		for (AffinityChannel chd : channelsFromAr) {
			for (InterimChannel idd1 : interimChannels) {
				if (idd1.getId() == chd.getId()) {
					SecondryDemo secDemo = new SecondryDemo();
					secDemo.setChannelId(chd.getId());
					secDemo.setLmkRefNo(idd1.getLmkRefNo());
					if (chd.getResultDemo() != null) {
						secDemo.setResult(chd.getResultDemo().getId());
					} else {
						secDemo.setResult(null);
					}
					secDemoList.add(secDemo);
				}
			}
		}

		return secDemoList;
	}

	/**
	 * Calculate Rate S1 (primaryDemo) -- Get Impact : Compare the channleId of
	 * Pricing with channelId of afResponse and map the demoId of primaryDemo in
	 * pricing with demoId of afResponse -- Impact *
	 * deliveryInputs->demoRate/1000(based on channel tier)
	 * 
	 * @param pricingRequest
	 * @param interimChannels
	 * @return
	 */
	private List<RateS1> getS1Result(PricingRequest pricingRequest, List<InterimChannel> interimChannels) {
		List<AffinityChannel> channelsFromAr = pricingRequest.getArResult().getChannels();
		List<InterimAudienceData> interimAudienceData1 = new ArrayList<>();

		channelsFromAr.forEach((chd) -> {
			chd.getDemos().forEach((dmd) -> {
				if (dmd.getId() == pricingRequest.getDemo()) {
					InterimAudienceData idd = new InterimAudienceData();
					idd.setDemoNo(dmd.getId());
					idd.setChannelNo(chd.getId());
					idd.setAvgImpact(dmd.getImpact());
					interimAudienceData1.add(idd);
				}
			});
		});

		List<RateS1> s1DataList = new ArrayList<RateS1>();
		for (InterimChannel chd : interimChannels) {
			for (InterimAudienceData idd1 : interimAudienceData1) {
				if (idd1.getChannelNo() == chd.getId()) {
					RateS1 s1data = new RateS1();
					s1data.setChannelId(chd.getId());
					s1data.setLmkRefNo(chd.getLmkRefNo());
					for (DeliveryCurrencyInput deliveryInput : pricingRequest.getDeliveryInputs()) {
						if (deliveryInput.getTier().equalsIgnoreCase(chd.getTier())) {
							s1data.setResult((idd1.getAvgImpact() * deliveryInput.getDemoRate()) * 0.001);
						}
					}
					s1DataList.add(s1data);
				}
			}
		}

		return s1DataList;
	}

	/**
	 * Calculate Rate S2 (Result SecondryDemo) -- Get Impact : Compare the channleId
	 * of Pricing with channelId of afResponse and Get the ID of resultDemo from
	 * afResponse and fetch the Impact -- Impact *
	 * deliveryInputs->tmsRate/1000(based on channel tier)
	 * 
	 * @param pricingRequest
	 * @param interimChannels
	 * @return
	 */
	private List<RateS1> getS2Result(PricingRequest pricingRequest, List<InterimChannel> interimChannels) {
		List<AffinityChannel> channelsFromAr = pricingRequest.getArResult().getChannels();
		List<InterimAudienceData> interimAudienceData1 = new ArrayList<>();
		channelsFromAr.forEach((chd) -> {
			chd.getDemos().forEach((dmd) -> {
				InterimAudienceData idd = new InterimAudienceData();
				if (chd.getResultDemo() == null) {
					idd.setDemoNo(dmd.getId());
					idd.setChannelNo(chd.getId());
					idd.setAvgImpact(0);
					interimAudienceData1.add(idd);
				} else {
					if (dmd.getId() == chd.getResultDemo().getId()) {

						idd.setDemoNo(dmd.getId());
						idd.setChannelNo(chd.getId());
						idd.setAvgImpact(dmd.getImpact());
						interimAudienceData1.add(idd);
					}
				}
			});
		});
		List<RateS1> s2DataList = new ArrayList<RateS1>();

		for (InterimChannel chd : interimChannels) {
			for (InterimAudienceData idd1 : interimAudienceData1) {
				if (idd1.getChannelNo() == chd.getId()) {
					RateS1 s1data = new RateS1();
					s1data.setChannelId(chd.getId());
					s1data.setLmkRefNo(chd.getLmkRefNo());
					for (DeliveryCurrencyInput deliveryInput : pricingRequest.getDeliveryInputs()) {
						if (deliveryInput.getTier().equalsIgnoreCase(chd.getTier())) {
							s1data.setResult((idd1.getAvgImpact() * deliveryInput.getTmsRate()) * 0.001);
						}
					}
					s2DataList.add(s1data);
				}
			}
		}
		return s2DataList;
	}

	/**
	 * This method get the Rates from LMK /Ingres/rates API
	 * 
	 * @param lmkList
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<LmkRateResponse> getRateData(List<Integer> lmkList, String startDate, String endDate) {
		logger.info("PricingCalculatorServiceImpl ::getRateData method begin.....");
		LmkRateResponse[] lmkRateResponses = new LmkRateResponse[0];
		String url = "https://dmsbookingportaluat.multichoice.co.za/Ingres/rates";
		LMKRateRequest lmkRatesObject = new LMKRateRequest();
		lmkRatesObject.setSalesAreaNo(lmkList);
		lmkRatesObject.setStartDate(startDate);
		lmkRatesObject.setEndDate(endDate);
		logger.info("Payload for /Ingres/rates {}", lmkRatesObject);
		try {
			logger.info("request to lmk: " + new ObjectMapper().writeValueAsString(lmkRatesObject));
		} catch (JsonProcessingException e) {
			logger.info(e.getMessage());
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<LMKRateRequest> requestBody = new HttpEntity<>(lmkRatesObject, headers);

		try {
			lmkRateResponses = restTemplate.postForObject(url, requestBody, LmkRateResponse[].class);
		} catch (Exception e) {
			logger.error("Error calling Lmk:", e);
		}
		try {
			logger.info("response from lmk: " + new ObjectMapper().writeValueAsString(lmkRateResponses));
		} catch (JsonProcessingException e) {
			logger.info(e.getMessage());

		}
		logger.info("PricingCalculatorServiceImpl ::getRateData() ended.....");
		assert lmkRateResponses != null;
		return Arrays.asList(lmkRateResponses);
	}

	/**
	 * This method returns audience data from LMK (/Ingres/audience)
	 * 
	 * @param lmkList
	 * @param pricRequest
	 * @param pricRequest
	 * @return
	 */
	public List<LmkAudienceResponse> getLmkAudienceData(List<Integer> lmkList, PricingRequest pricRequest) {
		LmkAudienceResponse[] lmkAudienceResponses = new LmkAudienceResponse[0];

		String url = "https://dmsbookingportaluat.multichoice.co.za/Ingres/audience";

		ArrayList<Integer> targetMarket = new ArrayList<>();
		targetMarket.add(pricRequest.getDemo());
		LmkAudienceRequest lmkAudienceRequest = new LmkAudienceRequest(lmkList, targetMarket, pricRequest.getYoyStart(),
				pricRequest.getYoyEnd(), pricRequest.getWeekPart(), pricRequest.getArResult().getDayparts());
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
		} catch (Exception e) {
			logger.error("Error calling Lmk:", e);
		}
		try {
			logger.info("response from lmk: " + new ObjectMapper().writeValueAsString(lmkAudienceResponses));
		} catch (JsonProcessingException e) {
			logger.info(e.getMessage());

		}
		assert lmkAudienceResponses != null;
		return Arrays.asList(lmkAudienceResponses);
	}

	public List<LmkAudienceResponse> getLmkAudienceDataForARDemo(List<Integer> lmkList, PricingRequest pricRequest,
			List<Integer> targetMarket) {
		LmkAudienceResponse[] lmkAudienceResponses = new LmkAudienceResponse[0];

		String url = "https://dmsbookingportaluat.multichoice.co.za/Ingres/audience";

		LmkAudienceRequest lmkAudienceRequest = new LmkAudienceRequest(lmkList, targetMarket, pricRequest.getYoyStart(),
				pricRequest.getYoyEnd(), pricRequest.getWeekPart(), pricRequest.getArResult().getDayparts());
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
		} catch (Exception e) {
			logger.error("Error calling Lmk:", e);
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
	 * This method persist the pricing details in ptp db
	 */
	@Override
	public LooseSpotResponse savePricing(PricingCreateRequest request) {
		LooseSpotResponse response = new LooseSpotResponse();
		LooseSpot lp = null;
		LooseSpot looseSpot = new LooseSpot();

		looseSpot.setBaselineId(request.getBaselineId());
		looseSpot.setBaseRate(request.getBaseRate());
		looseSpot.setDelivery(request.getDelivery());
		looseSpot.setPriDemo(request.getDemo());
		looseSpot.setQoqEnd(request.getQoqEnd());
		looseSpot.setQoqStart(request.getQoqStart());
		looseSpot.setRateDecr(request.getRateDecr());
		looseSpot.setRateIncr(request.getRateIncr());
		looseSpot.setYoyEnd(request.getYoyEnd());
		looseSpot.setYoyStart(request.getYoyStart());
		looseSpot.setSoHigh(request.getSoHigh());
		looseSpot.setSoLow(request.getSoLow());

		/*
		 * set deliveryInputsList
		 */
		List<LooseSpotDeliveryInputs> deliveryInputsList = setDeliveryInputs(request.getDeliveryInputs());
		looseSpot.setLooseSpotDeliveryInputsList(deliveryInputsList);
		List<PricingChannel> prChannelList = request.getChannels();
		List<LooseSpotChannel> channelList = setValuesToChannelList(prChannelList, looseSpot);
		looseSpot.setLooseSpotChannelList(channelList);
		logger.info("LooseSpot Details before save in DB:->{}", looseSpot);
		try {
			lp = looseSpotRepo.save(looseSpot);
		} catch (Exception exc) {
			response.setBaselineId(lp.getBaselineId());
			response.setPricingId(lp.getId());
			response.setMessage("Failed to persist in db" + exc.getMessage());
			response.setStatus("Failed");
		}
		logger.info("LooseSpot Details saved in DB:->{}", lp.toString());

		if (lp != null) {
			response.setBaselineId(lp.getBaselineId());
			response.setPricingId(lp.getId());
			response.setMessage("Successfully Persisted in DB");
			response.setStatus("Completed");
		}
		return response;
	}

	private List<LooseSpotDeliveryInputs> setDeliveryInputs(List<DeliveryCurrencyInput> request) {
		List<LooseSpotDeliveryInputs> deliveryInputsList = new ArrayList<>();
		request.forEach(di -> {
			LooseSpotDeliveryInputs deliveryInputs = new LooseSpotDeliveryInputs();
			deliveryInputs.setDemoRate(di.getDemoRate());
			deliveryInputs.setTier(di.getTier());
			deliveryInputs.setTmsRate(di.getTmsRate());
			deliveryInputsList.add(deliveryInputs);
		});
		return deliveryInputsList;
	}

	/**
	 * This method set all the values into LooseSpotChannel
	 * 
	 * @param prChannelList
	 * @param looseSpot
	 * @return
	 */

	private List<LooseSpotChannel> setValuesToChannelList(List<PricingChannel> prChannelList, LooseSpot looseSpot) {
		List<LooseSpotChannel> channelList = new ArrayList<>();

		List<LooseSpotRateYOY> lsry = new ArrayList<>();
		List<LooseSpotRateQOQ> lsrq = new ArrayList<>();

		prChannelList.forEach(channel -> {
			LooseSpotChannel lChannel = new LooseSpotChannel();
			lChannel.setChannelId(channel.getId());

			/*
			 * set rateYOY value
			 */
			List<RateYOY> rateYoyList = channel.getRatesYOY();
			rateYoyList.forEach(rateY -> {
				LooseSpotRateYOY ry = new LooseSpotRateYOY();
				ry.setDaypart(rateY.getId());
				ry.setRate(rateY.getRate());
				lsry.add(ry);

			});
			lChannel.setLooseSpotRateYOYList(lsry);
			/*
			 * set rateQOQ value
			 */
			List<RateQOQ> rateQoqList = channel.getRatesQOQ();
			rateQoqList.forEach(rateQ -> {
				LooseSpotRateQOQ rq = new LooseSpotRateQOQ();
				rq.setDaypart(rateQ.getId());
				rq.setRate(rateQ.getRate());
				lsrq.add(rq);

			});
			lChannel.setLooseSpotRateQOQList(lsrq);
			lChannel.setRateS1(channel.getS1());
			lChannel.setRateS2(channel.getS2());
			lChannel.setRateS3(channel.getS3());
			lChannel.setRateInflationQOQ(channel.getRateInflationQOQ());
			lChannel.setRateInflationYOY(channel.getRateInflationYOY());
			if (channel.getSecDemoId() != null) {
				lChannel.setSecDemo(channel.getSecDemoId());
			} else {
				lChannel.setSecDemo(0);
			}
			/*
			 * set calculatedRate
			 */
			List<LooseSpotRate> lsrList = new ArrayList<>();
			List<CalculatedRate> calculatedRateList = channel.getCalculatedRate();
			calculatedRateList.forEach(crl -> {
				LooseSpotRate lsr = new LooseSpotRate();
				lsr.setDaypart(crl.getId());
				lsr.setRate(crl.getRate());
				lsrList.add(lsr);
			});
			lChannel.setLooseSpotRateList(lsrList);
			AudienceYOY ay = channel.getAudienceYOY();
			LooseSpotAudienceYOY audienceYOY = new LooseSpotAudienceYOY();
			audienceYOY.setAudience(ay.getAudinece());
			audienceYOY.setRating(ay.getRating());
			audienceYOY.setCpp(ay.getCpp());
			audienceYOY.setCpt(ay.getCpt());
			lChannel.setLooseSpotAudienceYOY(audienceYOY);

			/*
			 * set audienceForecast
			 */

			List<LooseSpotAudienceForecast> looseSpotAudienceForecastList = new ArrayList<>();

			List<AudienceDaypart> audiList = channel.getAudienceForecast().getAudienceDaypart();
			VarianceYOY vary = channel.getAudienceForecast().getVarianceYOY();
			audiList.forEach(al -> {
				LooseSpotAudienceForecast audienceForecast = new LooseSpotAudienceForecast();
				audienceForecast.setDaypart(al.getId());
				audienceForecast.setAudience(al.getAudinece());
				audienceForecast.setRating(al.getRating());
				audienceForecast.setCpp(al.getCpp());
				audienceForecast.setCpt(al.getCpt());
				looseSpotAudienceForecastList.add(audienceForecast);

			});
			lChannel.setLooseSpotAudienceForecastList(looseSpotAudienceForecastList);
			LooseSpotAudienceForecastVariance lsafv = new LooseSpotAudienceForecastVariance();
			lsafv.setAudience(vary.getAudinece());
			lsafv.setRating(vary.getRating());
			if (!(Double.isNaN(vary.getCpp())) && !(Double.isNaN(vary.getCpt()))) {
				lsafv.setCpp(vary.getCpp());
				lsafv.setCpt(vary.getCpt());
			} else {
				lsafv.setCpp(0.0);
				lsafv.setCpt(0.0);
			}
			lChannel.setLooseSpotAudienceForecastVariance(lsafv);

			/*
			 * set sourceData
			 */

			List<SourceData> sdList = channel.getSourceData();
			List<LooseSpotSource> lssList = new ArrayList<>();

			sdList.forEach(sl -> {
				LooseSpotSource lss = new LooseSpotSource();
				lss.setAudience(sl.getAudience());
				lss.setRating(sl.getRating());
				lss.setCpp(sl.getCpp());
				lss.setCpt(sl.getCpt());
				lss.setDemoId(sl.getDemoId());
				lssList.add(lss);
			});
			lChannel.setLooseSpotSourceList(lssList);

			/*
			 * set Sellout
			 */
			List<SellOutData> sodList = channel.getSelloutData();
			List<LooseSpotSellout> lsddList = new ArrayList<>();

			sodList.forEach(sol -> {
				LooseSpotSellout lss = new LooseSpotSellout();
				lss.setDaypart(sol.getId());
				lss.setSo(sol.getSellout());
				lsddList.add(lss);
			});
			lChannel.setLooseSpotSelloutList(lsddList);
			channelList.add(lChannel);

		});

		return channelList;

	}

	@Override
	@Transactional
	public LooseSpotResponse updatePricing(PricingUpdateRequest request) {
		LooseSpotResponse response = new LooseSpotResponse();
		LooseSpot lp = null;
		/*
		 * check for valid ID
		 */
		LooseSpot looseSpot = looseSpotRepo.findById(request.getId())
				.orElseThrow(() -> new InvalidLooseSpotIdException("PriceId not found for id : " + request.getId()));

		List<Integer> lid = looseSpotChannelRepo.findByLooseSpoChanneltId(looseSpot.getId());
		looseSpotChannelRepo.deleteByIdIn(lid);

		List<Integer> did = deliveryInputRepo.findByDeliveryId(looseSpot.getId());
		deliveryInputRepo.deleteByIdIn(did);

		/*
		 * update looseSpot fields..
		 */
		looseSpot.setBaselineId(request.getBaselineId());
		looseSpot.setBaseRate(request.getBaseRate());
		looseSpot.setDelivery(request.getDelivery());
		looseSpot.setPriDemo(request.getDemo());
		looseSpot.setQoqEnd(request.getQoqEnd());
		looseSpot.setQoqStart(request.getQoqStart());
		looseSpot.setRateDecr(request.getRateDecr());
		looseSpot.setRateIncr(request.getRateIncr());
		looseSpot.setYoyEnd(request.getYoyEnd());
		looseSpot.setYoyStart(request.getYoyStart());
		looseSpot.setSoHigh(request.getSoHigh());
		/*
		 * set deliveryInputsList
		 */
		List<LooseSpotDeliveryInputs> deliveryInputsList = setDeliveryInputs(request.getDeliveryInputs());
		looseSpot.setLooseSpotDeliveryInputsList(deliveryInputsList);

		/*
		 * set channelList
		 */
		List<PricingChannel> prChannelList = request.getChannels();
		List<LooseSpotChannel> channelList = new ArrayList<>();
		channelList = setValuesToChannelList(prChannelList, looseSpot);
		looseSpot.setLooseSpotChannelList(channelList);

		logger.info("LooseSpot Details before update in DB:->{}", looseSpot);
		try {
			lp = looseSpotRepo.save(looseSpot);
		} catch (Exception exc) {
			response.setBaselineId(lp.getBaselineId());
			response.setPricingId(lp.getId());
			response.setMessage("Failed to persist in db" + exc.getMessage());
			response.setStatus("Failed");
		}
		logger.info("LooseSpot Details updated in DB :-> {}", lp.toString());

		if (lp != null) {
			response.setBaselineId(lp.getBaselineId());
			response.setPricingId(lp.getId());
			response.setMessage("Successfully Updated in DB");
			response.setStatus("Completed");
		}
		return response;

	}

	@Override
	public PricingBaseline getLooseSpot(int baseLineId) {
		LooseSpot looseSpot;
		try {
			looseSpot = looseSpotRepo.findByBaselineId(baseLineId);
			if (looseSpot == null)
				throw new BaselineNotFoundException("LooseSpot Pricing Calculation Not Found");
		} catch (Exception e) {
			logger.error("Error finding loosespot Calculation for baselineId : {},{}", baseLineId, e.getMessage());
			throw e;
		}

		Baseline baseline = baselineRepository.getOne(looseSpot.getBaselineId());
		PricingBaseline pricingBaseline = new PricingBaseline();

		pricingBaseline.setId(looseSpot.getId());
		pricingBaseline.setBaselineId(looseSpot.getBaselineId());
		if (baseline.getStatus().equalsIgnoreCase("A"))
			pricingBaseline.setStatus("Approved");
		else
			pricingBaseline.setStatus("Draft");
		if (baseline.getAff().getWeekpart().equalsIgnoreCase("WE"))
			pricingBaseline.setWeekPart("WEEKEND");
		else {
			if (baseline.getAff().getWeekpart().equalsIgnoreCase(("WD")))
				pricingBaseline.setWeekPart("WEEKDAY");
			else
				pricingBaseline.setWeekPart("ALLDAY");
		}
		pricingBaseline.setDemo(looseSpot.getPriDemo());
		pricingBaseline.setBaseRate(looseSpot.getBaseRate());
		pricingBaseline.setDelivery(looseSpot.getDelivery());
		pricingBaseline.setSoHigh(looseSpot.getSoHigh());
		pricingBaseline.setSoLow(looseSpot.getSoLow());
		pricingBaseline.setRateIncr(looseSpot.getRateIncr());
		pricingBaseline.setRateDecr(looseSpot.getRateDecr());
		pricingBaseline.setQoqStart(looseSpot.getQoqStart());
		pricingBaseline.setQoqEnd(looseSpot.getQoqEnd());
		pricingBaseline.setYoyStart(looseSpot.getYoyStart());
		pricingBaseline.setYoyEnd(looseSpot.getYoyEnd());

		List<LooseSpotDeliveryInputs> looseSpotDeliveryInputsList = looseSpot.getLooseSpotDeliveryInputsList();
		List<DeliveryCurrencyInput> deliveryCurrencyInputList = new ArrayList<>();
		looseSpotDeliveryInputsList.forEach(looseSpotDeliveryInputs -> deliveryCurrencyInputList
				.add(new DeliveryCurrencyInput(looseSpotDeliveryInputs.getTier(), looseSpotDeliveryInputs.getDemoRate(),
						looseSpotDeliveryInputs.getTmsRate())));
		pricingBaseline.setDeliveryInputs(deliveryCurrencyInputList);

		List<LooseSpotChannel> looseSpotChannelList = looseSpot.getLooseSpotChannelList();
		List<PricingChannel> pricingChannelList = new ArrayList<>();
		looseSpotChannelList.forEach(looseSpotChannel -> {
			List<LooseSpotRateYOY> looseSpotRateYOYList = looseSpotChannel.getLooseSpotRateYOYList();
			List<RateYOY> rateYOYList = new ArrayList<>();
			looseSpotRateYOYList.forEach(looseSpotRateYOY -> rateYOYList
					.add(new RateYOY(looseSpotRateYOY.getDaypart(), looseSpotRateYOY.getRate())));

			List<LooseSpotRateQOQ> looseSpotRateQOQList = looseSpotChannel.getLooseSpotRateQOQList();
			List<RateQOQ> rateQOQList = new ArrayList<>();
			looseSpotRateQOQList.forEach(looseSpotRateQOQ -> rateQOQList
					.add(new RateQOQ(looseSpotRateQOQ.getDaypart(), looseSpotRateQOQ.getRate())));

			List<LooseSpotRate> looseSpotRateList = looseSpotChannel.getLooseSpotRateList();
			List<CalculatedRate> calculatedRateList = new ArrayList<>();
			looseSpotRateList.forEach(looseSpotRate -> calculatedRateList
					.add(new CalculatedRate(looseSpotRate.getDaypart(), looseSpotRate.getRate())));

			List<LooseSpotAudienceForecast> looseSpotAudienceForecastList = looseSpotChannel
					.getLooseSpotAudienceForecastList();
			List<AudienceDaypart> audienceDaypartList = new ArrayList<>();
			looseSpotAudienceForecastList.forEach(looseSpotAudienceForecast -> audienceDaypartList
					.add(new AudienceDaypart(looseSpotAudienceForecast.getDaypart(),
							looseSpotAudienceForecast.getRating(), looseSpotAudienceForecast.getAudience(),
							looseSpotAudienceForecast.getCpt(), looseSpotAudienceForecast.getCpp())));

			List<LooseSpotSource> looseSpotSourceList = looseSpotChannel.getLooseSpotSourceList();
			List<SourceData> sourceDataList = new ArrayList<>();
			looseSpotSourceList.forEach(looseSpotSource -> sourceDataList
					.add(new SourceData(looseSpotSource.getDemoId(), looseSpotSource.getRating(),
							looseSpotSource.getAudience(), looseSpotSource.getCpt(), looseSpotSource.getCpp())));

			List<LooseSpotSellout> looseSpotSelloutList = looseSpotChannel.getLooseSpotSelloutList();
			List<SellOutData> sellOutDataList = new ArrayList<>();
			looseSpotSelloutList.forEach(looseSpotSellout -> sellOutDataList
					.add(new SellOutData(looseSpotSellout.getDaypart(), looseSpotSellout.getSo())));

			Channel channel = channelDemoRepository.getOne(looseSpotChannel.getChannelId());
			List<String> genres = new ArrayList<>();
			channel.getGenres().forEach(genre -> genres.add(genre.getName()));
			PricingChannel pricingChannel = new PricingChannel();
			pricingChannel.setId(looseSpotChannel.getChannelId());
			pricingChannel.setName(channel.getName());
			pricingChannel.setTier(channel.getPackageName());
			pricingChannel.setNetwork(channel.getNetwork());
			pricingChannel.setGenre(genres);
			pricingChannel.setRatesYOY(rateYOYList);
			pricingChannel.setRatesQOQ(rateQOQList);
			pricingChannel.setSecDemoId(looseSpotChannel.getSecDemo());
			pricingChannel.setS1(looseSpotChannel.getRateS1());
			pricingChannel.setS2(looseSpotChannel.getRateS2());
			pricingChannel.setS3(looseSpotChannel.getRateS3());
			pricingChannel.setCalculatedRate(calculatedRateList);
			pricingChannel.setRateInflationYOY(looseSpotChannel.getRateInflationYOY());
			pricingChannel.setRateInflationQOQ(looseSpotChannel.getRateInflationQOQ());
			LooseSpotAudienceYOY looseSpotAudienceYOY = looseSpotChannel.getLooseSpotAudienceYOY();
			pricingChannel.setAudienceYOY(new AudienceYOY(looseSpotAudienceYOY.getRating(),
					looseSpotAudienceYOY.getAudience(), looseSpotAudienceYOY.getCpt(), looseSpotAudienceYOY.getCpp()));
			LooseSpotAudienceForecastVariance looseSpotAudienceForecastVariance = looseSpotChannel
					.getLooseSpotAudienceForecastVariance();
			VarianceYOY varianceYOY = new VarianceYOY(looseSpotAudienceForecastVariance.getRating(),
					looseSpotAudienceForecastVariance.getAudience(), looseSpotAudienceForecastVariance.getCpt(),
					looseSpotAudienceForecastVariance.getCpp());
			pricingChannel.setAudienceForecast(new AudienceForecast(audienceDaypartList, varianceYOY));
			pricingChannel.setSourceData(sourceDataList);
			pricingChannel.setSelloutData(sellOutDataList);

			pricingChannelList.add(pricingChannel);

		});
		pricingBaseline.setChannels(pricingChannelList);

		return pricingBaseline;
	}
}
