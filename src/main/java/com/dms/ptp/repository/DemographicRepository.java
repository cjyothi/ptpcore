package com.dms.ptp.repository;

import com.dms.ptp.entity.Demo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Demographic
 */
@Repository
public interface DemographicRepository extends JpaRepository<Demo, Integer> {

    List<Demo> findByPanelId(int id);


}
