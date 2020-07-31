package com.ptp.campaign.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ptp.campaign.model.CampaignStrikeweight;

@Repository
public interface CampaignStrikeweightRepository extends JpaRepository<CampaignStrikeweight, Integer>{

}
