package com.dms.ptp.repository;

import com.dms.ptp.entity.LooseSpotDeliveryInputs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LooseSpotDeliveryInputsRepository extends JpaRepository<LooseSpotDeliveryInputs, Integer> {

	@Query(value = "select lc.id from lspot_delivery_input lc where lc.lspot_id=?1", nativeQuery = true)
	List<Integer> findByDeliveryId(int looseSpotId);

	void deleteByIdIn(List<Integer> ids);

}
