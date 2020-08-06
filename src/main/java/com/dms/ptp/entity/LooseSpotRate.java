package com.dms.ptp.entity;
import javax.persistence.*;

@Entity
@Table(name = "lspot_rate")
public class LooseSpotRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name="daypart")
    private String daypart;

    @Column(name="rate")
    private double rate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public String getDaypart() {
        return daypart;
    }

    public void setDaypart(String daypart) {
        this.daypart=daypart;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate=rate;
    }

	@Override
	public String toString() {
		return "LooseSpotRate [id=" + id + ", daypart=" + daypart + ", rate=" + rate + "]";
	}
    
}
