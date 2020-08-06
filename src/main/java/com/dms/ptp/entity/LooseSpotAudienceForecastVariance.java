package com.dms.ptp.entity;

import javax.persistence.*;

@Entity
@Table(name = "lspot_audience_forecast_variance")
public class LooseSpotAudienceForecastVariance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "rating")
	private double rating;

	@Column(name = "audience")
	private double audience;

	@Column(name = "cpt")
	private Double cpt;

	@Column(name = "cpp")
	private Double cpp;

	// @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval =
	// true)
	//@OneToOne(mappedBy = "looseSpotAudienceForecastVariance")
	// @JoinColumn(name = "lspot_channel_id8", referencedColumnName = "id", nullable
	// = false)
	//private LooseSpotChannel looseSpotChannel;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Double getCpt() {
		return cpt;
	}

	public void setCpt(Double cpt) {
		this.cpt = cpt;
	}

	public Double getCpp() {
		return cpp;
	}

	public void setCpp(Double cpp) {
		this.cpp = cpp;
	}

	/*
	 * public LooseSpotChannel getLooseSpotChannel() { return looseSpotChannel; }
	 * 
	 * public void setLooseSpotChannel(LooseSpotChannel looseSpotChannel) {
	 * this.looseSpotChannel = looseSpotChannel; }
	 */
	@Override
	public String toString() {
		return "LooseSpotAudienceForecastVariance [id=" + id + ", rating=" + rating + ", audience=" + audience
				+ ", cpt=" + cpt + ", cpp=" + cpp + "]";
	}

}
