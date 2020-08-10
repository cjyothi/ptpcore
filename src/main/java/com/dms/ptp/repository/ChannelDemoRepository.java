package com.dms.ptp.repository;

import com.dms.ptp.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for ChannelDemo
 */
@Repository
public interface ChannelDemoRepository extends JpaRepository<Channel, Integer> {

	List<Channel> findByTerritory_Id(int territory);

	List<Channel> findByPlatform_Id(int platform);

	List<Channel> findByTerritory_IdAndPlatform_Id(int territory, int platform);

	@Query(value = "select c.id from channel c where c.lmkrefno=?1", nativeQuery = true)
	int findByChannelId(int lref);
	
	@Query(value = "select c.lmkrefno from channel c where c.id=?1", nativeQuery = true)
	int findByChId(int lref);

}