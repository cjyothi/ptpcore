package com.dms.ptp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dms.ptp.entity.PackageAudienceForecast;

@Repository
public interface PackageAudienceRepository extends JpaRepository<PackageAudienceForecast, Integer>{

}
