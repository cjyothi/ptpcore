package com.dms.ptp.repository;

import com.dms.ptp.entity.Panel;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PanelRepository extends JpaRepository<Panel, Integer> {
    
    @Query(value = "select * from panel d where d.name = :name", nativeQuery = true)
    public Panel findPanelId(@Param("name") String name);
    
    @Query(value = "select p.id, p.name from panel p", nativeQuery = true)
    public List<Object> getPanelIdAndName();
}
