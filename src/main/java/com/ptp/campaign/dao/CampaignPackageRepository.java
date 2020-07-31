package com.ptp.campaign.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ptp.campaign.model.Items;

@Repository
public interface CampaignPackageRepository extends JpaRepository<Items, Integer>{

}
