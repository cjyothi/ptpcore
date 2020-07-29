package com.dms.ptp.service;

import com.dms.ptp.dto.CatalogRequest;
import com.dms.ptp.response.CatalogResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

public interface CatalogService {

	CatalogResponse getCatalogById(int id);

	CatalogResponse getAllCatalog(Specification<String> specification, Pageable of);

	Map<String, Object> deleteCatalogById(int id);

	CatalogResponse createCatalog(CatalogRequest catalogRequest);

	CatalogResponse updateCatalog(CatalogRequest catalogRequest);
}
