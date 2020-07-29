package com.dms.ptp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dms.ptp.entity.Baseline;

@Repository
public interface BaselineRepository extends JpaRepository<Baseline,Integer>{

}
