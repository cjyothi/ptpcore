package com.dms.ptp.entity;
import javax.persistence.*;

@Entity
@Table(name="lspot_so")
public class LooseSpotSellout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name="daypart")
    private String daypart;

    @Column(name="so")
    private double so;

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

    public double getSo() {
        return so;
    }

    public void setSo(double so) {
        this.so=so;
    }

	@Override
	public String toString() {
		return "LooseSpotSellout [id=" + id + ", daypart=" + daypart + ", so=" + so + "]";
	}
    
}
