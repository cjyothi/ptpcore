package com.ptp.campaign.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ptp.campaign.model.CampaignRevision;

@Repository
public interface CampaignRevisionRepository extends JpaRepository<CampaignRevision, Integer>{

}
