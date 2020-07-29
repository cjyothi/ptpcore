package com.dms.ptp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dms.ptp.entity.Aff;

@Repository
public interface AffinityRepository extends JpaRepository<Aff,Integer>{

}
