package com.dms.ptp.dto;

public class RateDayPart {
    private String daypart;
    private Integer rate_actual;
    private Integer rate_forecast;

    public String getDaypart() {
        return daypart;
    }

    public void setDaypart(String daypart) {
        this.daypart = daypart;
    }

    public Integer getRate_actual() {
        return rate_actual;
    }

    public void setRate_actual(Integer rate_actual) {
        this.rate_actual = rate_actual;
    }

    public Integer getRate_forecast() {
        return rate_forecast;
    }

    public void setRate_forecast(Integer rate_forecast) {
        this.rate_forecast = rate_forecast;
    }
}
