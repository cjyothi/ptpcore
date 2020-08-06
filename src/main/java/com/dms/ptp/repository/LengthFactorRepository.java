package com.dms.ptp.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.dms.ptp.entity.LengthFactor;

@Repository
public interface LengthFactorRepository extends JpaRepository<LengthFactor, Integer>, JpaSpecificationExecutor<LengthFactor>{

    Page<LengthFactor> findAll(Pageable pageable);
}
