package com.dms.ptp.serviceimplementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dms.ptp.dto.UnivrseDemoDto;
import com.dms.ptp.entity.PriceDemo;
import com.dms.ptp.repository.PriceDemoRepository;
import com.dms.ptp.response.UniverseDemoResponse;
import com.dms.ptp.service.PriceDemoService;

/**
 * 
 * @author chidananda.p
 *
 */
@Service
public class PriceDemoServiceImpl implements PriceDemoService {
    @Autowired
    private PriceDemoRepository demoRepo;
    UniverseDemoResponse response = null;

    @Override
    public UniverseDemoResponse getDemoInsightsTwoParam(String demoOne, String demoTwo, String sdate, String edate,
            int panelId) {
        response = new UniverseDemoResponse();
        List<UnivrseDemoDto> dtoList = new ArrayList<UnivrseDemoDto>();
        List<PriceDemo> demo = demoRepo.getDemoUniverseTwoParam(demoOne, demoTwo, sdate, edate, panelId);
        for (int i = 0; i < demo.size(); i++) {
            UnivrseDemoDto dto = new UnivrseDemoDto();
            dto.setId(demo.get(i).getId());
            dto.setName(demo.get(i).getName());
            // dto.setSegment(demo.get(i).getSegment());
            // dto.setNetwork(demo.get(i).getNetwork());
            // dto.setTier(demo.get(i).getTier());
            // dto.setGenre(demo.get(i).getGenre_name());
            dto.setD_1(demo.get(i).getUniverse_avg1());
            dto.setD_2(demo.get(i).getUniverse_avg2());
            dto.setD_3(demo.get(i).getUniverse_avg3());
            dto.setD_4(demo.get(i).getUniverse_avg4());
            dto.setD_5(demo.get(i).getUniverse_avg5());
            dto.setD_6(demo.get(i).getUniverse_avg6());
            dto.setD_7(demo.get(i).getUniverse_avg7());
            dtoList.add(dto);
        }
        response.setItems(dtoList);
        response.setTotal(dtoList.size());
        return response;
    }

    @Override
    public UniverseDemoResponse getDemoInsightsThreeParam(String demoOne, String demoTwo, String demoThree,
            String sdate, String edate, int panelId) {
        response = new UniverseDemoResponse();
        List<UnivrseDemoDto> dtoList = new ArrayList<UnivrseDemoDto>();
        List<PriceDemo> demo = demoRepo.getDemoUniverseThreeParam(demoOne, demoTwo, demoThree, sdate, edate, panelId);
        for (int i = 0; i < demo.size(); i++) {
            UnivrseDemoDto dto = new UnivrseDemoDto();
            dto.setId(demo.get(i).getId());
            dto.setName(demo.get(i).getName());
            // dto.setSegment(demo.get(i).getSegment());
            // dto.setNetwork(demo.get(i).getNetwork());
            // dto.setTier(demo.get(i).getTier());
            // dto.setGenre(demo.get(i).getGenre_name());
            dto.setD_1(demo.get(i).getUniverse_avg1());
            dto.setD_2(demo.get(i).getUniverse_avg2());
            dto.setD_3(demo.get(i).getUniverse_avg3());
            dto.setD_4(demo.get(i).getUniverse_avg4());
            dto.setD_5(demo.get(i).getUniverse_avg5());
            dto.setD_6(demo.get(i).getUniverse_avg6());
            dto.setD_7(demo.get(i).getUniverse_avg7());
            dtoList.add(dto);
        }
        response.setItems(dtoList);
        response.setTotal(dtoList.size());
        return response;
    }

    @Override
    public UniverseDemoResponse getDemoInsightsFourParam(String demoOne, String demoTwo, String demoThree,
            String demoFour, String sdate, String edate, int panelId) {
        response = new UniverseDemoResponse();
        List<UnivrseDemoDto> dtoList = new ArrayList<UnivrseDemoDto>();
        List<PriceDemo> demo = demoRepo.getDemoUniverseFourParam(demoOne, demoTwo, demoThree, demoFour, sdate, edate,
                panelId);
        demo.forEach(demoObject -> {
            UnivrseDemoDto dto = new UnivrseDemoDto();
            dto.setId(demoObject.getId());
            dto.setName(demoObject.getName());
            // dto.setSegment(demo.get(i).getSegment());
            // dto.setNetwork(demo.get(i).getNetwork());
            // dto.setTier(demo.get(i).getTier());
            // dto.setGenre(demo.get(i).getGenre_name());
            dto.setD_1(demoObject.getUniverse_avg1());
            dto.setD_2(demoObject.getUniverse_avg2());
            dto.setD_3(demoObject.getUniverse_avg3());
            dto.setD_4(demoObject.getUniverse_avg4());
            dto.setD_5(demoObject.getUniverse_avg5());
            dto.setD_6(demoObject.getUniverse_avg6());
            dto.setD_7(demoObject.getUniverse_avg7());
            dtoList.add(dto);
        });
//	    for (int i = 0; i < demo.size(); i++) {
//	       UnivrseDemoDto dto = new UnivrseDemoDto();
//	       dto.setId(demo.get(i).getId());
//	       dto.setName(demo.get(i).getName());
//	       // dto.setSegment(demo.get(i).getSegment());
//	       // dto.setNetwork(demo.get(i).getNetwork());
//	       // dto.setTier(demo.get(i).getTier());
//	       // dto.setGenre(demo.get(i).getGenre_name());
//	       dto.setD_1(demo.get(i).getUniverse_avg1());
//	       dto.setD_2(demo.get(i).getUniverse_avg2());
//	       dto.setD_3(demo.get(i).getUniverse_avg3());
//	       dto.setD_4(demo.get(i).getUniverse_avg4());
//	       dto.setD_5(demo.get(i).getUniverse_avg5());
//	       dto.setD_6(demo.get(i).getUniverse_avg6());
//	       dto.setD_7(demo.get(i).getUniverse_avg7());
//	       dtoList.add(dto);
//	    }
        response.setItems(dtoList);
        response.setTotal(dtoList.size());
        return response;
    }

    @Override
    public UniverseDemoResponse getDemoInsightsFiveParam(String demoOne, String demoTwo, String demoThree,
            String demoFour, String demoFive, String sdate, String edate, int panelId) {
        response = new UniverseDemoResponse();
        List<UnivrseDemoDto> dtoList = new ArrayList<UnivrseDemoDto>();
        List<PriceDemo> demo = demoRepo.getDemoUniverseFiveParam(demoOne, demoTwo, demoThree, demoFour, demoFive, sdate,
                edate, panelId);
        for (int i = 0; i < demo.size(); i++) {
            UnivrseDemoDto dto = new UnivrseDemoDto();
            dto.setId(demo.get(i).getId());
            dto.setName(demo.get(i).getName());
            // dto.setSegment(demo.get(i).getSegment());
            // dto.setNetwork(demo.get(i).getNetwork());
            // dto.setTier(demo.get(i).getTier());
            // dto.setGenre(demo.get(i).getGenre_name());
            dto.setD_1(demo.get(i).getUniverse_avg1());
            dto.setD_2(demo.get(i).getUniverse_avg2());
            dto.setD_3(demo.get(i).getUniverse_avg3());
            dto.setD_4(demo.get(i).getUniverse_avg4());
            dto.setD_5(demo.get(i).getUniverse_avg5());
            dto.setD_6(demo.get(i).getUniverse_avg6());
            dto.setD_7(demo.get(i).getUniverse_avg7());
            dtoList.add(dto);
        }
        response.setItems(dtoList);
        response.setTotal(dtoList.size());
        return response;

    }

    @Override
    public UniverseDemoResponse getDemoInsightsSixParam(String demoOne, String demoTwo, String demoThree,
            String demoFour, String demoFive, String demoSix, String sdate, String edate, int panelId) {
        response = new UniverseDemoResponse();
        List<UnivrseDemoDto> dtoList = new ArrayList<UnivrseDemoDto>();
        List<PriceDemo> demo = demoRepo.getDemoUniverseSixParam(demoOne, demoTwo, demoThree, demoFour, demoFive,
                demoSix, sdate, edate, panelId);
        for (int i = 0; i < demo.size(); i++) {
            UnivrseDemoDto dto = new UnivrseDemoDto();
            dto.setId(demo.get(i).getId());
            dto.setName(demo.get(i).getName());
            // dto.setSegment(demo.get(i).getSegment());
            // dto.setNetwork(demo.get(i).getNetwork());
            // dto.setTier(demo.get(i).getTier());
            dto.setD_1(demo.get(i).getUniverse_avg1());
            dto.setD_2(demo.get(i).getUniverse_avg2());
            dto.setD_3(demo.get(i).getUniverse_avg3());
            dto.setD_4(demo.get(i).getUniverse_avg4());
            dto.setD_5(demo.get(i).getUniverse_avg5());
            dto.setD_6(demo.get(i).getUniverse_avg6());
            dto.setD_7(demo.get(i).getUniverse_avg7());
            dtoList.add(dto);
        }
        response.setItems(dtoList);
        response.setTotal(dtoList.size());
        return response;

    }

    @Override
    public UniverseDemoResponse getDemoInsightsSevenParam(String demoOne, String demoTwo, String demoThree,
            String demoFour, String demoFive, String demoSix, String demoSeven, String sdate, String edate,
            int panelId) {
        response = new UniverseDemoResponse();
        List<UnivrseDemoDto> dtoList = new ArrayList<UnivrseDemoDto>();
        List<PriceDemo> demo = demoRepo.getDemoUniverseSevenParam(demoOne, demoTwo, demoThree, demoFour, demoFive,
                demoSix, demoSeven, sdate, edate, panelId);
        for (int i = 0; i < demo.size(); i++) {
            UnivrseDemoDto dto = new UnivrseDemoDto();
            dto.setId(demo.get(i).getId());
            dto.setName(demo.get(i).getName());
            // dto.setSegment(demo.get(i).getSegment());
            // dto.setNetwork(demo.get(i).getNetwork());
            // dto.setTier(demo.get(i).getTier());
            // dto.setGenre(demo.get(i).getGenre_name());
            dto.setD_1(demo.get(i).getUniverse_avg1());
            dto.setD_2(demo.get(i).getUniverse_avg2());
            dto.setD_3(demo.get(i).getUniverse_avg3());
            dto.setD_4(demo.get(i).getUniverse_avg4());
            dto.setD_5(demo.get(i).getUniverse_avg5());
            dto.setD_6(demo.get(i).getUniverse_avg6());
            dto.setD_7(demo.get(i).getUniverse_avg7());
            dtoList.add(dto);
        }
        response.setItems(dtoList);
        response.setTotal(dtoList.size());
        return response;

    }

}
