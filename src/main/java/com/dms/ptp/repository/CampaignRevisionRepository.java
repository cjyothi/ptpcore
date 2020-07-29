package com.dms.ptp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dms.ptp.entity.CampaignRevision;

@Repository
public interface CampaignRevisionRepository extends JpaRepository<CampaignRevision, Integer>{

}
