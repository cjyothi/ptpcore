package com.ptp.campaign.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ptp.campaign.model.PackageAudienceForecast;

public interface PackageAudienceRepository extends JpaRepository<PackageAudienceForecast, Integer>{

}
