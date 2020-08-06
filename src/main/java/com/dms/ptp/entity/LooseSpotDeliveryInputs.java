package com.dms.ptp.entity;
import javax.persistence.*;

@Entity
@Table(name="lspot_delivery_input")
public class LooseSpotDeliveryInputs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "tier")
    private String tier;

    @Column(name = "demo_rate")
    private int demoRate;

    @Column(name = "tms_rate")
    private int tmsRate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier=tier;
    }

    public int getDemoRate() {
        return demoRate;
    }

    public void setDemoRate(int demoRate) {
        this.demoRate=demoRate;
    }

    public int getTmsRate() {
        return tmsRate;
    }

    public void setTmsRate(int tmsRate) {
        this.tmsRate=tmsRate;
    }

	@Override
	public String toString() {
		return "LooseSpotDeliveryInputs [id=" + id + ", tier=" + tier + ", demoRate=" + demoRate + ", tmsRate="
				+ tmsRate + "]";
	}
    
}
