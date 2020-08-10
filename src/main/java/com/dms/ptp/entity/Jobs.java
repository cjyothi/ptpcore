package com.dms.ptp.entity;

import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Table creation for Jobs
 */
@Entity
@Table(name= "Jobs")
public class Jobs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "job")
    private short job;

    @Column(name="type")
    private String type;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "status")
    private short status;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "started_date")
    private LocalDateTime startedDate;

    @Column(name = "finished_date")
    private LocalDateTime finishedDate;

    @Column(name = "log_file")
    private String logFile;

    public Jobs() {
    }

    public Jobs(short job, String type,String fileName, short status, LocalDateTime createdDate) {
        this.job = job;
        this.type = type;
        this.fileName = fileName;
        this.status = status;
        this.createdDate=createdDate;
    }

    public Jobs(short job, String type,String fileName, short status, LocalDateTime createdDate, LocalDateTime startedDate, LocalDateTime finishedDate, String logFile) {
        this.job = job;
        this.type = type;
        this.fileName = fileName;
        this.status = status;
        this.createdDate = createdDate;
        this.startedDate = startedDate;
        this.finishedDate = finishedDate;
        this.logFile = logFile;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public short getJob() {
        return job;
    }

    public void setJob(short job) {
        this.job = job;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(LocalDateTime startedDate) {
        this.startedDate = startedDate;
    }

    public LocalDateTime getFinishedDate() {
        return finishedDate;
    }

    public void setFinishedDate(LocalDateTime finishedDate) {
        this.finishedDate = finishedDate;
    }

    public String getLogFile() {
        return logFile;
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    @Override
    public String toString() {
        return "Jobs [id=" + id + ", job=" + job + ", fileName=" + fileName + ", status=" + status + ", createdDate="
                + createdDate + ", startedDate=" + startedDate + ", finishedDate=" + finishedDate + ", logFile="
                + logFile + "]";
    }
    
}
