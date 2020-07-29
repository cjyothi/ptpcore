package com.dms.ptp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="campaign_demo")
public class CampaignDemo {
	
	@Id
	@Column(name = "demo_id")
	private int demoId;

    public int getDemoId() {
        return demoId;
    }

    public void setDemoId(int demoId) {
        this.demoId = demoId;
    }
	
    /*
     * //@OneToOne
     * 
     * @JoinColumn(name = "campaign_id", referencedColumnName = "portal_id") private
     * Campaign campaign_id;
     */
	
	
	
}
