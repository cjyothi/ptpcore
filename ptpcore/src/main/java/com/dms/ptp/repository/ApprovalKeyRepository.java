package com.dms.ptp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dms.ptp.entity.ApprovalKey;

public interface ApprovalKeyRepository extends JpaRepository<ApprovalKey, Integer>{

    @Query(value = "{call get_id_fromApprovalKey()}", nativeQuery=true)
    public int findByIdFromApprovalKey();

}
