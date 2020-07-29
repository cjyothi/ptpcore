package com.dms.ptp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dms.ptp.entity.SellOutInfo;

public interface SelloutRepository extends JpaRepository<SellOutInfo, Integer> {

    @Query(value = "{call sellout_priceing(:sdate,:edate)}", nativeQuery = true)
    public List<SellOutInfo> getRatesInfo(String sdate, String edate);

}
