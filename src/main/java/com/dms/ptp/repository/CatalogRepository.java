package com.dms.ptp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.dms.ptp.entity.Catalog;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, Integer>,JpaSpecificationExecutor<Catalog>{
//Need to write the query to get rows only which has certain conditions
//	@Query(value = "SELECT * FROM catalog c, catalog_plans cp, catalog_pricing cpr where c.id=cp.catalog_id AND cp.id=cpr.plans_id AND CURRENT_TIMESTAMP between cpr.validity_start and cpr.validity_end and cpr.is_active=1",
//			countQuery = "SELECT count(*) FROM catalog c, catalog_plans cp, catalog_pricing cpr where c.id=cp.catalog_id AND cp.id=cpr.plans_id AND CURRENT_TIMESTAMP between cpr.validity_start and cpr.validity_end and cpr.is_active=1",nativeQuery = true)
	Page<Catalog> findAll(Pageable pageable);

}
