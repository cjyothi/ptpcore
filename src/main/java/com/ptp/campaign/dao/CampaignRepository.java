package com.ptp.campaign.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ptp.campaign.model.Campaign;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Integer>{


    @Query(value = "select * from campaign c order by c.portal_id desc", nativeQuery = true)
    Page<Campaign> gelAllCampaignList(Pageable pageable);
    
    @Query(value = "select * from campaign c where c.user_id =?1 order by c.portal_id desc", nativeQuery = true)
    Page<Campaign> gelAllCampaignList(int userId, Pageable pageable);
    //changes starts - changing query for frontend changes
    @Query(value = "select * from campaign c where c.status in(?1 , ?2) order by c.portal_id asc", nativeQuery = true)
    Page<Campaign> getCampaignByStatus(int status1,int status2, Pageable pageable);
    
    @Query(value = "select * from campaign c where c.status in(?1 , ?2) and c.user_id =?3 order by c.portal_id asc", nativeQuery = true)
    Page<Campaign> getCampaignByStatus(int status1,int status2, int userId, Pageable pageable);

    @Query(value = "select * from campaign c where c.status in(?1 , ?2) order by c.portal_id desc", nativeQuery = true)
    Page<Campaign> getCampaignByStatusDesc(int status1,int status2,Pageable pageable);
    
    @Query(value = "select * from campaign c where c.status in(?1 , ?2) and c.user_id =?3 order by c.portal_id desc", nativeQuery = true)
    Page<Campaign> getCampaignByStatusDesc(int status1,int status2,int userId,Pageable pageable);

    Page<Campaign> findAllByUserId(int userId, Pageable pageable);

    @Query(value = "select c.folder_name from campaign c where c.portal_id=?1", nativeQuery = true)
    String findFolderNameByPortalId(int portalId);

    @Query(value = "select * from campaign c where c.status =?1  and c.start >CURRENT_TIMESTAMP order by c.portal_id asc", nativeQuery = true)
    Page<Campaign> getCampaignByFlightDates(int status, Pageable pageable);

    @Query(value = "select * from campaign c where c.status =?1 and c.user_id =?2 and c.start >CURRENT_TIMESTAMP order by c.portal_id asc", nativeQuery = true)
    Page<Campaign> getCampaignByFlightDates(int status, int userId, Pageable pageable);

    @Query(value = "select * from campaign c where c.status =?1 and c.start >CURRENT_TIMESTAMP order by c.portal_id desc", nativeQuery = true)
    Page<Campaign> getCampaignByFlightDatesDesc(int status,Pageable pageable);

    @Query(value = "select * from campaign c where c.status =?1 and c.user_id =?2 and c.start >CURRENT_TIMESTAMP order by c.portal_id desc", nativeQuery = true)
    Page<Campaign> getCampaignByFlightDatesDesc(int status,int userId,Pageable pageable);

    @Query(value = "select * from campaign c where c.status =?1 and CURRENT_TIMESTAMP between c.start and c.end order by c.portal_id asc", nativeQuery = true)
    Page<Campaign> getApprovedCampaign(int status, Pageable pageable);

    @Query(value = "select * from campaign c where c.status =?1 and c.user_id =?2 and CURRENT_TIMESTAMP between c.start and c.end order by c.portal_id asc", nativeQuery = true)
    Page<Campaign> getApprovedCampaign(int status, int userId, Pageable pageable);

    @Query(value = "select * from campaign c where c.status =?1 and CURRENT_TIMESTAMP between c.start and c.end order by c.portal_id desc", nativeQuery = true)
    Page<Campaign> getApprovedCampaignDesc(int status,Pageable pageable);

    @Query(value = "select * from campaign c where c.status =?1 and c.user_id =?2 and CURRENT_TIMESTAMP between c.start and c.end order by c.portal_id desc", nativeQuery = true)
    Page<Campaign> getApprovedCampaignDesc(int status,int userId,Pageable pageable);
    //changes ends - changing query for frontend changes
}
