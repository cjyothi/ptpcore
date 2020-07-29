package com.dms.ptp.repository;

import com.dms.ptp.entity.Demo;
import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DemoRepository extends JpaRepository<Demo,Integer> {

    List<Demo> findByIdIn(List<Integer> demoList);


}
