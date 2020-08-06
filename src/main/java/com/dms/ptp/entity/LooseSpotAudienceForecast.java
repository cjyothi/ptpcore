package com.dms.ptp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "lspot_audience_forecast")
public class LooseSpotAudienceForecast {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "daypart")
	private String daypart;

	@Column(name = "rating")
	private double rating;

	@Column(name = "audience")
	private double audience;

	@Column(name = "cpt")
	private double cpt;

	@Column(name = "cpp")
	private double cpp;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDaypart() {
		return daypart;
	}

	public void setDaypart(String daypart) {
		this.daypart = daypart;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public double getAudience() {
		return audience;
	}

	public void setAudience(double audience) {
		this.audience = audience;
	}

	public double getCpt() {
		return cpt;
	}

	public void setCpt(double cpt) {
		this.cpt = cpt;
	}

	public double getCpp() {
		return cpp;
	}

	public void setCpp(double cpp) {
		this.cpp = cpp;
	}

	@Override
	public String toString() {
		return "LooseSpotAudienceForecast [id=" + id + ", daypart=" + daypart + ", rating=" + rating + ", audience="
				+ audience + ", cpt=" + cpt + ", cpp=" + cpp + "]";
	}
	
}
