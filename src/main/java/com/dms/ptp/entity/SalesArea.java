package com.dms.ptp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="catalog_sales_area")
@Getter
@Setter
public class SalesArea {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private int id;
	@Column(name="sales_area")
	private int salesArea;
	private String name;
	private int spots;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getSalesArea() {
        return salesArea;
    }
    public void setSalesArea(int salesArea) {
        this.salesArea = salesArea;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getSpots() {
        return spots;
    }
    public void setSpots(int spots) {
        this.spots = spots;
    }
	
	
}
