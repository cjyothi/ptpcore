package com.dms.ptp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.dms.ptp.entity.Catalog;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, Integer>,JpaSpecificationExecutor<Catalog>{

	Page<Catalog> findAll(Pageable pageable);

}
