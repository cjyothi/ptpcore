package com.dms.ptp.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class RateInfo {
    @Id
    private Integer id;
    private String channel_name;
    private String day_part;
    private Integer rate_actual;
    private Integer rate_forecast;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public String getDay_part() {
        return day_part;
    }

    public void setDay_part(String day_part) {
        this.day_part = day_part;
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
