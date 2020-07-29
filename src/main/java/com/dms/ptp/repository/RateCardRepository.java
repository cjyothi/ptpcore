package com.dms.ptp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dms.ptp.entity.RateCards;

@Repository
public interface RateCardRepository extends JpaRepository<RateCards, Integer>{

    @Query(value = "select * from rate_cards rc where rc.status =:status", nativeQuery = true)
    public List<RateCards> findByStatus(@Param("status") String status);
}
