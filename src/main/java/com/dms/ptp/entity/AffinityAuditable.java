package com.dms.ptp.entity;


import java.util.Date;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AffinityAuditable<U> {
    
    

        @CreatedDate
        //@Temporal(TemporalType.TIMESTAMP)
        protected Date createdOn;

        @LastModifiedDate
       // @Temporal(TemporalType.TIMESTAMP)
        protected Date lastModifiedOn;


        public Date getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(Date createdOn) {
            this.createdOn = createdOn;
        }

        public Date getLastModifiedOn() {
            return lastModifiedOn;
        }

        public void setLastModifiedOn(Date lastModifiedOn) {
            this.lastModifiedOn = lastModifiedOn;
        }

    }

