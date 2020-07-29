package com.dms.ptp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dms.ptp.entity.Items;

@Repository
public interface CampaignPackageRepository extends JpaRepository<Items, Integer>{

}
