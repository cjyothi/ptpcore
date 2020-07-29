package com.dms.ptp.serviceimplementation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dms.ptp.repository.CatalogRepository;
import com.dms.ptp.controller.CatalogController;
import com.dms.ptp.dto.CatalogRequest;
import com.dms.ptp.exception.CatalogDBException;
import com.dms.ptp.exception.CatalogNotFoundException;
import com.dms.ptp.entity.Catalog;
import com.dms.ptp.response.CatalogResponse;
import com.dms.ptp.service.CatalogService;
import com.dms.ptp.util.Constant;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    CatalogRepository catalogRepository;
    
    static Logger logger = LoggerFactory.getLogger(CatalogServiceImpl.class);

    @Override
    public CatalogResponse createCatalog(CatalogRequest catalogReq) {
        logger.info("Inside CatalogServiceImpl : createCatalog");
        ModelMapper modelMapper = new ModelMapper();
        Catalog catalog = modelMapper.map(catalogReq, Catalog.class);
        CatalogResponse resp = null;
        try {
            Catalog item = catalogRepository.save(catalog);
            resp = new CatalogResponse();
            List<Catalog> catalogList = new ArrayList<>();
            catalogList.add(item);
            resp.setTotal(1);
            resp.setItems(catalogList);
            return resp;
        } catch (Exception e) {
            logger.error("Exception in CatalogServiceImpl : createCatalog ", e.getMessage());
            throw new CatalogDBException(Constant.DB_EXCEPTION_MSG);
        }
    }

    @Override
    public CatalogResponse getCatalogById(int id) {
        Catalog item = null;
        logger.info("Inside CatalogServiceImpl : getCatalogById");
        item = catalogRepository.findById(id)
                .orElseThrow(() -> new CatalogNotFoundException(Constant.CATALOG_NOT_FOUND));

        CatalogResponse resp = new CatalogResponse();
        List<Catalog> catalogList = new ArrayList<>();
        resp.setTotal(1);
        catalogList.add(item);
        resp.setItems(catalogList);
        return resp;
    }

    @Override
    public CatalogResponse getAllCatalog(Specification specification, Pageable of) {
        logger.info("Inside CatalogServiceImpl : getCatalogById");
        CatalogResponse resp = null;
        Page<Catalog> pg = catalogRepository.findAll(specification, of);
        if (pg.getContent().isEmpty()) {
            throw new CatalogNotFoundException(Constant.CATALOG_NOT_FOUND);
        }

        List<Catalog> catalogList = pg.getContent();
        resp = new CatalogResponse();
        resp.setTotal(catalogList.size());
        resp.setItems(catalogList);
        return resp;
    }

    @Override
    public Map<String, Object> deleteCatalogById(int id) {
        logger.info("Inside CatalogServiceImpl : deleteCatalogById");
        Catalog item = catalogRepository.findById(id)
                .orElseThrow(() -> new CatalogNotFoundException(Constant.CATALOG_NOT_FOUND + id));
        try {
            catalogRepository.delete(item);
        } catch (Exception e) {
            logger.error("Exception in CatalogServiceImpl : deleteCatalogById ", e.getMessage());
            throw new CatalogDBException(Constant.DB_EXCEPTION_MSG);
        }
        Map<String, Object> resp = new HashMap<>();
        resp.put("status", Boolean.TRUE);
        resp.put("Message", Constant.DELETED_SUCCESSFULLY);
        return resp;
    }

    @Override
    public CatalogResponse updateCatalog(CatalogRequest catalogRequest) {
        logger.info("Inside CatalogServiceImpl : updateCatalog");
        CatalogResponse resp = null;
        List<Catalog> catalogList = null;
        Catalog item = catalogRepository.findById(catalogRequest.getId())
                .orElseThrow(() -> new CatalogNotFoundException(Constant.CATALOG_NOT_FOUND + catalogRequest.getId()));
        ModelMapper modelMapper = new ModelMapper();
        Catalog catalogReq = modelMapper.map(catalogRequest, Catalog.class);

        item.setTitle1(catalogReq.getTitle1());
        item.setTitle2(catalogReq.getTitle2());
        item.setShortDescr(catalogReq.getShortDescr());
        item.setBusinessType(catalogReq.getBusinessType());
        item.setType(catalogReq.getType());
        item.setGraphic(catalogReq.getGraphic());
        item.setLogo(catalogReq.getLogo());
        item.setExclude(catalogReq.getExclude());
        item.setRate(catalogReq.getRate());
        item.setCpp(catalogReq.getCpp());
        item.setCpt(catalogReq.getCpt());
        item.setTvr(catalogReq.getTvr());
        item.setViews(catalogReq.getViews());
        //clearing daypart list before adding new daypart
        item.getDaypart().clear();
        item.setDaypart(catalogReq.getDaypart());
        //clearing plans  list before adding new plans
        item.getPlans().clear();
        item.setPlans(catalogReq.getPlans());
        try {
            Catalog itemResult = catalogRepository.save(item);
            resp = new CatalogResponse();
            catalogList = new ArrayList<>();
            catalogList.add(itemResult);
            resp.setTotal(1);
            resp.setItems(catalogList);
            return resp;
        } catch (Exception e) {
            logger.error("Exception in CatalogServiceImpl : updateCatalog ", e.getMessage());
            throw new CatalogDBException(Constant.DB_EXCEPTION_MSG);
        }
    }
}
