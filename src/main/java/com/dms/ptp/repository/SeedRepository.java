package com.dms.ptp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dms.ptp.entity.Seed;

@Repository
public interface SeedRepository extends JpaRepository<Seed, Integer>{

    @Query(value = "{call get_id_fromSeed()}", nativeQuery=true)
    public int findByIdFromSeed();

}
