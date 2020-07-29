package com.dms.ptp.repository;


import com.dms.ptp.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for ChannelDemo
 */
@Repository
public interface ChannelDemoRepository extends JpaRepository<Channel,Integer> {

    List<Channel> findByTerritory_Id(int territory);

    List<Channel> findByPlatform_Id(int platform);

    List<Channel> findByTerritory_IdAndPlatform_Id(int territory, int platform);
}