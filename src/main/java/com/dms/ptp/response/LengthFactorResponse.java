package com.dms.ptp.response;

import java.util.List;

public class LengthFactorResponse {


    float priceFactor;

    public float getPriceFactor() {
        return priceFactor;
    }

    public void setPriceFactor(float priceFactor) {
        this.priceFactor = priceFactor;
    }

    @Override
    public String toString() {
        return "LengthFactorResponse [priceFactor=" + priceFactor + "]";
    }

    public LengthFactorResponse() {
        super();
    }


}
