package com.dms.ptp.repository;

import com.dms.ptp.entity.LooseSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LooseSpotRepository extends JpaRepository<LooseSpot,Integer> {

    LooseSpot findByBaselineId(int baselineId);

}

