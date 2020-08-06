package com.dms.ptp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dms.ptp.entity.CampaignDemo;

@Repository
public interface CampaignDemoRepository extends JpaRepository<CampaignDemo, Integer>{

}
