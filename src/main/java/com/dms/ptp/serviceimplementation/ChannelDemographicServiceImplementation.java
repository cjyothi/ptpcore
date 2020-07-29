package com.dms.ptp.serviceimplementation;

import com.dms.ptp.entity.Channel;
import com.dms.ptp.entity.Demo;
import com.dms.ptp.entity.Panel;
import com.dms.ptp.repository.ChannelDemoRepository;
import com.dms.ptp.repository.ChannelRepository;
import com.dms.ptp.repository.DemographicRepository;
import com.dms.ptp.repository.PanelRepository;
import com.dms.ptp.response.ChannelData;
import com.dms.ptp.response.ChannelDemographicResponse;
import com.dms.ptp.response.ChannelResponse;
import com.dms.ptp.response.DemoInput;
import com.dms.ptp.response.DemographicData;
import com.dms.ptp.response.FinalChannelResponse;
import com.dms.ptp.service.ChannelDemographicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Method implementation of ChannelDemographicService
 */
@Service
public class ChannelDemographicServiceImplementation implements ChannelDemographicService {

    @Autowired
    DemographicRepository demographicRepository;

    @Autowired
    private PanelRepository panelRepo;


    @Autowired
    ChannelDemoRepository channelDemoRepository;
    
    @Autowired
    ChannelRepository demoRepository;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * @param panel String
     * @return ChannelDemographicResponse
     */
    @Override
    public ChannelDemographicResponse getDemographics(String panel) {
        List<Demo> demographicList;
        List<DemographicData> demographicModels = new ArrayList<>();

        if(panel.equals("all")){
            demographicList = demographicRepository.findAll();
            logger.info("Demographics for all panels are retrieved");
        }
        else
        {
            demographicList = demographicRepository.findByPanelId(Integer.parseInt(panel));
            logger.info("Demographic for panel {} is retrieved",panel);
        }
        demographicList.forEach(demographic -> demographicModels.add(new DemographicData(demographic.getId(),demographic.getPanel().getName(),demographic.getName(),demographic.getSegment())));
        return new ChannelDemographicResponse(demographicModels.size(),demographicModels);
    }

    /**
     * @param platform int
     * @return ChannelDemographicResponse
     */
    @Override
    public ChannelDemographicResponse getChannels(int platform) {
        List<Channel> channelList = channelDemoRepository.findAll() ;
        List<ChannelData> channelData =new ArrayList<>();

        logger.info("Channels for all platforms are retrieved");
        channelList.forEach(channel ->{
             List<String> genreNames = new ArrayList<>();
             channel.getGenres().forEach(genre -> genreNames.add(genre.getName()));
            channelData.add(new ChannelData(channel.getId(),channel.getName(),genreNames,channel.getLogo()));
        });

        return new ChannelDemographicResponse(channelData.size(),channelData);
    }

    /**
     * @param channel Channel
     * @return int
     */
    @Override
    public int addChannel(Channel channel) {

        Channel addedChannel = channelDemoRepository.save(channel);
        return addedChannel.getId();
    }

    /**
     * @param demoInput DemoInput
     * @return int
     */
    @Override
    public int addDemo(DemoInput demoInput) {

        Demo demo = new Demo();
        Optional<Panel> panel = panelRepo.findById(demoInput.getPanel());
        demo.setName(demoInput.getDescription());
        demo.setPanel(panel.get());
        demo.setSegment(demoInput.getSegment());
        Demo addedDemo = demographicRepository.save(demo);
        return addedDemo.getId();
    }
    
    /**
     * @param channel String
     * @param startDate String
     * @param endDate String
     * @param panelId int
     * @return FinalChannelResponse
     */
    public FinalChannelResponse getChannelInsights(String channel, String startDate, String endDate, int panelId) {

        FinalChannelResponse finalChannelResponse = new FinalChannelResponse();

        List<ChannelResponse> channelResponseList = demoRepository.getChannelInfo(channel, startDate, endDate, panelId);

        finalChannelResponse.setChannelResponseList(channelResponseList);
        finalChannelResponse.setListSize(channelResponseList.size());
        return finalChannelResponse;
    }


}
