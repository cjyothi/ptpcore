package com.ptp.campaign.model;

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
}
