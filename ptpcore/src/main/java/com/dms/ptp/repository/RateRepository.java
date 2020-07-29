package com.dms.ptp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dms.ptp.entity.RateInfo;

public interface RateRepository extends JpaRepository<RateInfo, Integer> {

    @Query(value = "{call rates_priceing(:sdate,:edate)}", nativeQuery = true)
    public List<RateInfo> getRatesInfo(String sdate, String edate);

}
