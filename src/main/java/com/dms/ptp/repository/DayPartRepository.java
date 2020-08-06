package com.dms.ptp.repository;

import com.dms.ptp.entity.DayPart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DayPartRepository extends JpaRepository<DayPart, Integer> {
}
