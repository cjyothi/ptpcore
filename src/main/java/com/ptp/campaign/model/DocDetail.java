package com.ptp.campaign.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "doc_detail")
public class DocDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer doc_id;
    
    @Column(name = "file_name")
    private String fileName;
    
    @Column(name = "s3_filename")
    private String s3FileName;
    
    @Column(name = "file_type")
    private String fileType;
    
    @Column(name = "folder_name")
    private String folderName;

}
