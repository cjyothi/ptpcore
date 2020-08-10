package com.dms.ptp.repository;

import com.dms.ptp.entity.SCChannels;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SCChannelRepository extends JpaRepository<SCChannels,Integer>{

}
