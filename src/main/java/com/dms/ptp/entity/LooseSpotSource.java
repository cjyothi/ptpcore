package com.dms.ptp.entity;
import javax.persistence.*;

@Entity
@Table(name="lspot_source")
public class LooseSpotSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name="demo_id")
    private int demoId;

    @Column(name="rating")
    private double rating;

    @Column(name="audience")
    private double audience;

    @Column(name="cpt")
    private double cpt;

    @Column(name="cpp")
    private double cpp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public int getDemoId() {
        return demoId;
    }

    public void setDemoId(int demoId) {
        this.demoId=demoId;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating=rating;
    }

    public double getAudience() {
        return audience;
    }

    public void setAudience(double audience) {
        this.audience=audience;
    }

    public double getCpt() {
        return cpt;
    }

    public void setCpt(double cpt) {
        this.cpt=cpt;
    }

    public double getCpp() {
        return cpp;
    }

    public void setCpp(double cpp) {
        this.cpp=cpp;
    }

	@Override
	public String toString() {
		return "LooseSpotSource [id=" + id + ", demoId=" + demoId + ", rating=" + rating + ", audience=" + audience
				+ ", cpt=" + cpt + ", cpp=" + cpp + "]";
	}
    
    
}
