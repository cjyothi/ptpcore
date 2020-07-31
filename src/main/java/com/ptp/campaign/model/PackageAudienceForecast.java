package com.ptp.campaign.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "package_audience_forecast")
public class PackageAudienceForecast {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name = "cpp")
	private float cpp;

	@Column(name = "cpt")
	private float cpt;

	@Column(name = "tvr")
	private float tvr;

	@Column(name = "views")
	private float views;

	@Column(name = "demo_id")
	private float demo_id;


	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "item_id", referencedColumnName = "id") 
	private Items items;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public float getCpp() {
		return cpp;
	}

	public void setCpp(float cpp) {
		this.cpp = cpp;
	}

	public float getCpt() {
		return cpt;
	}

	public void setCpt(float cpt) {
		this.cpt = cpt;
	}

	public float getTvr() {
		return tvr;
	}

	public void setTvr(float tvr) {
		this.tvr = tvr;
	}

	public float getViews() {
		return views;
	}

	public void setViews(float views) {
		this.views = views;
	}

	public float getDemo_id() {
		return demo_id;
	}

	public void setDemo_id(float demo_id) {
		this.demo_id = demo_id;
	}

	public Items getItems() {
		return items;
	}

	public void setItems(Items items) {
		this.items = items;
	}

	public void setDemo_id(int demo_id) {
		this.demo_id = demo_id;
	}


}
