package com.dms.ptp.entity;

import javax.persistence.*;

@Entity
@Table(name = "lspot_audience_yoy")
public class LooseSpotAudienceYOY {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@Column(name = "rating")
	private double rating;

	@Column(name = "audience")
	private double audience;

	@Column(name = "cpt")
	private double cpt;

	@Column(name = "cpp")
	private double cpp;

	//@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
	//@OneToOne(mappedBy = "looseSpotAudienceYOY")
	//@JoinColumn(name = "lspot_channel_id7", referencedColumnName = "id", nullable = false)
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

	/*
	 * public LooseSpotChannel getLooseSpotChannel() { return looseSpotChannel; }
	 * 
	 * public void setLooseSpotChannel(LooseSpotChannel looseSpotChannel) {
	 * this.looseSpotChannel = looseSpotChannel; }
	 * 
	 */	@Override
	public String toString() {
		return "LooseSpotAudienceYOY [id=" + id + ", rating=" + rating + ", audience=" + audience + ", cpt=" + cpt
				+ ", cpp=" + cpp  + "]";
	}
	
}
