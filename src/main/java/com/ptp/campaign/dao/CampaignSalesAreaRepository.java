package com.ptp.campaign.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ptp.campaign.model.CampaignSalesArea;

@Repository
public interface CampaignSalesAreaRepository extends JpaRepository<CampaignSalesArea, Integer>{

}
