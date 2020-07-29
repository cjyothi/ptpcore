package com.dms.ptp.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dms.ptp.response.DocResponse;
import com.dms.ptp.service.DocService;

import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin
@Slf4j
@RequestMapping("/campaign")
public class DocumentController {

    @Autowired
    private DocService docService;
    
    private static final Logger log = LoggerFactory.getLogger(DocumentController.class);

    @PostMapping("/docs")
    public ResponseEntity<DocResponse> upload(@ModelAttribute MultipartFile file,
            @RequestParam(required = false) String folderName, @RequestParam(defaultValue = "Others") String type,
            @RequestHeader(name = "Authorization") String token) throws Exception {
        log.info("inside DocController: upload");
        DocResponse response = new DocResponse();
        response = docService.uploadInS3(file, file.getOriginalFilename(), file.getContentType(), folderName, type,
                token);

        if (response != null) {

            log.info("response: " + response);
            return ResponseEntity.ok().body(response);
        } else {
            return ResponseEntity.status(500).body(response);
        }

    }

    @GetMapping("/doc")
    public ResponseEntity<ByteArrayResource> getDocuments(@RequestParam String folderName,
            @RequestParam String fileName, HttpServletResponse response) throws IOException {

        final byte[] data = docService.getObject(folderName, fileName);
        final ByteArrayResource resource = new ByteArrayResource(data);
        return ResponseEntity.ok().contentLength(data.length).header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"").body(resource);

    }

    @GetMapping("/docs/{portalId}")
    public ResponseEntity<List<DocResponse>> getAllDocuments(@PathVariable("portalId") Integer portalId) {
        List<DocResponse> s3Object = docService.getObjectslistFromFolder(portalId);
        return ResponseEntity.ok().body(s3Object);
    }

    @DeleteMapping("/docs")
    public ResponseEntity<String> deleteDocuments(@RequestParam String folderName, @RequestParam String fileName) {
        if (folderName == null || folderName.isEmpty() || folderName.equals("") || fileName.equals(""))
            return ResponseEntity.badRequest().body("folderName:Invalid inputs ");
        String response = docService.deleteObject(folderName, fileName);
        return ResponseEntity.ok().body(response);
    }
}
