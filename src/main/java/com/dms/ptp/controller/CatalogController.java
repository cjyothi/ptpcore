package com.dms.ptp.controller;

import java.util.Map;

import com.dms.ptp.exception.CatalogRequestInvalidException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dms.ptp.dto.CatalogRequest;
import com.dms.ptp.response.CatalogResponse;
import com.dms.ptp.service.CatalogService;

import io.github.perplexhub.rsql.RSQLJPASupport;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin
@RestController
@RequestMapping("/catalog")
@Slf4j
public class CatalogController {

    @Autowired
    private CatalogService catalogService;
    
    static Logger logger = LoggerFactory.getLogger(CatalogController.class);

    @CrossOrigin
    @ApiOperation(value = " This api is used to create catalog")
    @PostMapping("/")
    public ResponseEntity<CatalogResponse> createCatalog(@RequestBody CatalogRequest catalogRequest) {
        logger.info("Inside CatalogController : createCatalog ");
        if (0 != catalogRequest.getId()) {
            throw new CatalogRequestInvalidException("Invalid input field : 'id' ");
        }
        CatalogResponse response = null;
        try {
            response = catalogService.createCatalog(catalogRequest);
        } catch (Exception e) {
            logger.error("Exception in CatalogController : createCatalog {}", e.getMessage());
        }
        if (response == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } else {
            return ResponseEntity.ok(response);
        }
    }

    @CrossOrigin
    @ApiOperation(value = " This api is used to fetch catalog by providing catalog id")
    @GetMapping("/{id}")
    public ResponseEntity<CatalogResponse> getCatalogById(@PathVariable(required = true) int id) {
        logger.info("Inside CatalogController : getCatalogById ");
        CatalogResponse response;

            response = catalogService.getCatalogById(id);
            return ResponseEntity.ok(response);

    }

    @CrossOrigin
    @ApiOperation(value = " This api is used to fetch catalog List")
    @GetMapping("/")
    public ResponseEntity<CatalogResponse> getAllCatalog(@RequestParam(name = "filter", required = false) String filter, Pageable pageable) {
        logger.info("Inside CatalogController : getAllCatalog ");
        CatalogResponse response;
        response = catalogService.getAllCatalog(RSQLJPASupport.toSpecification(filter),
                PageRequest.of(pageable.getPageNumber(),
                        pageable.getPageSize() == 20 ? Integer.MAX_VALUE : pageable.getPageSize(),
                        pageable.getSort() == Sort.unsorted() ? Sort.by("id") : pageable.getSort()));
        
        if (response == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } else {
            return ResponseEntity.ok(response);
        }
    }

    @CrossOrigin
    @ApiOperation(value = " This api is used to remove catalog")
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteCatalogById(@PathVariable(required = true) int id) {
        logger.info("Inside CatalogController : deleteCatalogById ");
        Map<String, Object> response;
        response = catalogService.deleteCatalogById(id);
        return response;
    }

    @CrossOrigin
    @ApiOperation(value = " This api is used to update catalog")
    @PutMapping("/")
    public ResponseEntity<CatalogResponse> updateCatalog(@RequestBody CatalogRequest catalogRequest) {
        logger.info("Inside CatalogController : updateCatalog ");
        if (0 == catalogRequest.getId()) {
            throw new CatalogRequestInvalidException(" Expected Request or field missing");
        }
        CatalogResponse response;
        response = catalogService.updateCatalog(catalogRequest);
        
        if (response == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } else {
            return ResponseEntity.ok(response);
        }
    }
}
