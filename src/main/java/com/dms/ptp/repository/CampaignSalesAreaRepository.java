package com.dms.ptp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dms.ptp.entity.CampaignSalesArea;

@Repository
public interface CampaignSalesAreaRepository extends JpaRepository<CampaignSalesArea, Integer>{

}
