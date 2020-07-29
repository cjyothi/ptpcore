package com.dms.ptp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "lengthFactor")
public class LengthFactor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @Column(name = "spotLength")
    int spotLength;

    @Column(name = "priceFactor")
    float priceFactor;

    @Column(name = "validityStart")
    String validityStart;

    @Column(name = "validtyEnd")
    String validtyEnd;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSpotLength() {
        return spotLength;
    }

    public void setSpotLength(int spotLength) {
        this.spotLength = spotLength;
    }

    public float getPriceFactor() {
        return priceFactor;
    }

    public void setPriceFactor(long priceFactor) {
        this.priceFactor = priceFactor;
    }

    public String getValidityStart() {
        return validityStart;
    }

    public void setValidityStart(String validityStart) {
        this.validityStart = validityStart;
    }

    public String getValidtyEnd() {
        return validtyEnd;
    }

    public void setValidtyEnd(String validtyEnd) {
        this.validtyEnd = validtyEnd;
    }

    @Override
    public String toString() {
        return "LengthFactor [id=" + id + ", spotLength=" + spotLength + ", priceFactor=" + priceFactor
                + ", validityStart=" + validityStart + ", validtyEnd=" + validtyEnd + "]";
    }

    public LengthFactor() {
        super();
    }



}
