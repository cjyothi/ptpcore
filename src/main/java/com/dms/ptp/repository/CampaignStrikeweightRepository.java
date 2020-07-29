package com.dms.ptp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dms.ptp.entity.CampaignStrikeweight;

@Repository
public interface CampaignStrikeweightRepository extends JpaRepository<CampaignStrikeweight, Integer>{

}
