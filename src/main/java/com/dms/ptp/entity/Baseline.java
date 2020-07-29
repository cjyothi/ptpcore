package com.dms.ptp.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="baseline")
public class Baseline extends AffinityAuditable<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "rateCardId")
    private int rateCardId;
    
    @Column(name = "title")
    private String title;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "createdBy")
    private int createdBy;
    
    @Column(name = "lastModifiedBy")
    private int lastModifiedBy;
    
    
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "affinity_id", referencedColumnName = "id")
    private Aff aff;
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRateCardId() {
        return rateCardId;
    }

    public void setRateCardId(int rateCardId) {
        this.rateCardId = rateCardId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public Aff getAff() {
        return aff;
    }

    public void setAff(Aff aff) {
        this.aff = aff;
    }
    

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }


    public int getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(int lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }


    @Override
    public String toString() {
        return "Baseline [id=" + id + ", rateCardId=" + rateCardId + ", title=" + title + ", status=" + status
                + ", createdBy=" + createdBy + ", lastModifiedBy=" + lastModifiedBy + ", aff=" + aff + "]";
    }

    public Baseline() {
        super();
    }
    
    
}
