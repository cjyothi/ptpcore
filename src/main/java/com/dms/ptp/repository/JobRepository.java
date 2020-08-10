package com.dms.ptp.repository;


import com.dms.ptp.entity.Jobs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Jobs
 */
@Repository
public interface JobRepository extends JpaRepository<Jobs,Integer> {
    Jobs findByFileName(String fileName);

    @Query("SELECT j from Jobs j WHERE j.status = 0 and type=:type")
    public List<Jobs> getNewFileList(String type);

    @Query("SELECT j from Jobs j WHERE j.status != 2 and type=:type")
    List<Jobs> findByType(String type);

}
