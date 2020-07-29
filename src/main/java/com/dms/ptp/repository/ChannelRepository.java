package com.dms.ptp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dms.ptp.response.ChannelResponse;

/**
 * Repository for Channel
 */
@Repository
public interface ChannelRepository extends JpaRepository<ChannelResponse, Integer> {

    @Query(value = "{call channel_insights(:channel,:sdate,:edate,:panelId)}", nativeQuery = true)
    List<ChannelResponse> getChannelInfo (String channel, String sdate, String edate, int panelId);


}
