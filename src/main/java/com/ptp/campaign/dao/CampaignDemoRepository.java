package com.ptp.campaign.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ptp.campaign.model.CampaignDemo;

@Repository
public interface CampaignDemoRepository extends JpaRepository<CampaignDemo, Integer>{

}
