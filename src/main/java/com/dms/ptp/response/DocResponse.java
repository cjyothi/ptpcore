package com.dms.ptp.response;

import java.net.URL;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocResponse {
    
    private String fileName; 
    private String s3FileName;
    private String fileType;
    private String folderName;
    private String message;
    private URL url;
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getS3FileName() {
        return s3FileName;
    }
    public void setS3FileName(String s3FileName) {
        this.s3FileName = s3FileName;
    }
    public String getFileType() {
        return fileType;
    }
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    public String getFolderName() {
        return folderName;
    }
    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public URL getUrl() {
        return url;
    }
    public void setUrl(URL url) {
        this.url = url;
    }
    
}
