package com.dms.ptp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="catalog_plans")
@Getter
@Setter
public class Plans {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private int id;
	private int weeks;
	@Column(name="spot_rate")
	private double spotRate;
	private int spots;
	@Column(name="spot_length")
	private int spotLength ;
	private double rate;
	@Column(name="busines_type")
	private int businessType;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "plans_id", referencedColumnName = "id")
	@Column(name="sales_area")
	private List<SalesArea> salesAreaList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeeks() {
        return weeks;
    }

    public void setWeeks(int weeks) {
        this.weeks = weeks;
    }

    public double getSpotRate() {
        return spotRate;
    }

    public void setSpotRate(double spotRate) {
        this.spotRate = spotRate;
    }

    public int getSpots() {
        return spots;
    }

    public void setSpots(int spots) {
        this.spots = spots;
    }

    public int getSpotLength() {
        return spotLength;
    }

    public void setSpotLength(int spotLength) {
        this.spotLength = spotLength;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getBusinessType() {
        return businessType;
    }

    public void setBusinessType(int businessType) {
        this.businessType = businessType;
    }

    public List<SalesArea> getSalesAreaList() {
        return salesAreaList;
    }

    public void setSalesAreaList(List<SalesArea> salesAreaList) {
        this.salesAreaList = salesAreaList;
    }
	
	
}
