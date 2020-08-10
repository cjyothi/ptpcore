package com.dms.ptp.repository;

import com.dms.ptp.entity.SC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SCRepository extends JpaRepository<SC,Integer>{
    
    @Query(value = "select lc.id from sc lc where lc.component=?1 and lc.rate_card_id=?2", nativeQuery = true)
    List<Integer> findByComponentIgnoreCaseAndRateCardId(String component,int rateCardId);

    SC findByTypeAndComponentAllIgnoreCaseAndRateCardIdAndWeek(String type,String component,int rateCardId,int week);

    void deleteByIdIn(List<Integer> idList);

}
