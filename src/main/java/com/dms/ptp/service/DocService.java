package com.dms.ptp.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.dms.ptp.response.DocResponse;

public interface DocService {

    public DocResponse uploadInS3(MultipartFile file, String originalFilename, String contentType,String folderName,String type, String token) throws Exception;

    public String deleteObject(String folderName,String fileName);

    public byte[] getObject(String folderName,String awsFileName) throws IOException;

    public List<DocResponse> getObjectslistFromFolder(int portalId);
    
    public String getFolderNameByPortalId(int portalId);
}
