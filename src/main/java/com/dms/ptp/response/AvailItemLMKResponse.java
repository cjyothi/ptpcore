package com.dms.ptp.response;

import java.util.List;

import com.dms.ptp.dto.TargetMarketValues;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AvailItemLMKResponse {

    private int sales_area_no;
    private String sales_area_name;
  //  private int target_market;
    private int price;
    private int program_no;
    private String program_name;
    private int start_time;
    private int end_time;
    private long rating;
    private long impact;
    private long cpp;
    private long cpt;
    private String scheduled_on;
    private int scheduled_time;
    private String day;
    private int universe;
    private List<TargetMarketValues> demos;
    public int getSales_area_no() {
        return sales_area_no;
    }
    public void setSales_area_no(int sales_area_no) {
        this.sales_area_no = sales_area_no;
    }
    public String getSales_area_name() {
        return sales_area_name;
    }
    public void setSales_area_name(String sales_area_name) {
        this.sales_area_name = sales_area_name;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public int getProgram_no() {
        return program_no;
    }
    public void setProgram_no(int program_no) {
        this.program_no = program_no;
    }
    public String getProgram_name() {
        return program_name;
    }
    public void setProgram_name(String program_name) {
        this.program_name = program_name;
    }
    public int getStart_time() {
        return start_time;
    }
    public void setStart_time(int start_time) {
        this.start_time = start_time;
    }
    public int getEnd_time() {
        return end_time;
    }
    public void setEnd_time(int end_time) {
        this.end_time = end_time;
    }
    public long getRating() {
        return rating;
    }
    public void setRating(long rating) {
        this.rating = rating;
    }
    public long getImpact() {
        return impact;
    }
    public void setImpact(long impact) {
        this.impact = impact;
    }
    public long getCpp() {
        return cpp;
    }
    public void setCpp(long cpp) {
        this.cpp = cpp;
    }
    public long getCpt() {
        return cpt;
    }
    public void setCpt(long cpt) {
        this.cpt = cpt;
    }
    public String getScheduled_on() {
        return scheduled_on;
    }
    public void setScheduled_on(String scheduled_on) {
        this.scheduled_on = scheduled_on;
    }
    public int getScheduled_time() {
        return scheduled_time;
    }
    public void setScheduled_time(int scheduled_time) {
        this.scheduled_time = scheduled_time;
    }
    public String getDay() {
        return day;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public int getUniverse() {
        return universe;
    }
    public void setUniverse(int universe) {
        this.universe = universe;
    }
    public List<TargetMarketValues> getDemos() {
        return demos;
    }
    public void setDemos(List<TargetMarketValues> demos) {
        this.demos = demos;
    }
    
    
}
