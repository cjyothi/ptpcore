package com.ptp.campaign.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="spots")
public class Spots {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "sales_area_no")
    private int sales_area_no;
    
    @Column(name = "sales_area_name")
    private String sales_area_name;
    
    @Column(name = "target_market")
    private int target_market;
    
    @Column(name = "price")
    private float price;
    
    @Column(name = "program_no")
    private int program_no;
    
    @Column(name = "program_name")
    private String program_name;
    
    @Column(name = "start_time")
    private float start_time;
    
    @Column(name = "end_time")
    private float end_time;
    
    @Column(name = "rating")
    private float rating;
    
    @Column(name = "impact")
    private float impact;
    
    @Column(name = "cpp")
    private float cpp;
    
    @Column(name = "cpt")
    private float cpt;
    
    @Column(name = "day")
    private String day;
    
    @Column(name = "scheduled_on")
    private String scheduled_on;
    
    @Column(name = "scheduled_time")
    private String scheduled_time;
    
    @Column(name = "universe")
    private String universe;
    
    @Column(name = "duration")
    private float duration;
    
    @Column(name = "spots")
    private float spots;
    
    @Column(name = "comment")
    private String comment;
    
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
    public int getTarget_market() {
        return target_market;
    }
    public void setTarget_market(int target_market) {
        this.target_market = target_market;
    }
    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
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
    public float getStart_time() {
        return start_time;
    }
    public void setStart_time(float start_time) {
        this.start_time = start_time;
    }
    public float getEnd_time() {
        return end_time;
    }
    public void setEnd_time(float end_time) {
        this.end_time = end_time;
    }
    public float getRating() {
        return rating;
    }
    public void setRating(float rating) {
        this.rating = rating;
    }
    public float getImpact() {
        return impact;
    }
    public void setImpact(float impact) {
        this.impact = impact;
    }
    public float getCpp() {
        return cpp;
    }
    public void setCpp(float cpp) {
        this.cpp = cpp;
    }
    public float getCpt() {
        return cpt;
    }
    public void setCpt(float cpt) {
        this.cpt = cpt;
    }
    public String getDay() {
        return day;
    }
    public void setDay(String day) {
        this.day = day;
    }
    public String getScheduled_on() {
        return scheduled_on;
    }
    public void setScheduled_on(String scheduled_on) {
        this.scheduled_on = scheduled_on;
    }
    public String getScheduled_time() {
        return scheduled_time;
    }
    public void setScheduled_time(String scheduled_time) {
        this.scheduled_time = scheduled_time;
    }
    public String getUniverse() {
        return universe;
    }
    public void setUniverse(String universe) {
        this.universe = universe;
    }
    public float getDuration() {
        return duration;
    }
    public void setDuration(float duration) {
        this.duration = duration;
    }
    public float getSpots() {
        return spots;
    }
    public void setSpots(float spots) {
        this.spots = spots;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    
}
