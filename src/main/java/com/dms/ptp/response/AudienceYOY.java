package com.dms.ptp.response;

public class AudienceYOY {

	private double rating;
	private double audinece;
	private double cpt;
	private double cpp;

	public AudienceYOY() {
	}

	public AudienceYOY(double rating, double audinece, double cpt, double cpp) {
		this.rating=rating;
		this.audinece=audinece;
		this.cpt=cpt;
		this.cpp=cpp;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public double getAudinece() {
		return audinece;
	}

	public void setAudinece(double audinece) {
		this.audinece = audinece;
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
		return "AudianceYOY [rating=" + rating + ", audinece=" + audinece + ", cpt=" + cpt + ", cpp=" + cpp + "]";
	}

}
