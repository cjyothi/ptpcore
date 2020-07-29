package com.dms.ptp.dto;

public class ResultDemo {
    private int id;
    private String segment;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public ResultDemo(int id, String segment) {
        super();
        this.id = id;
        this.segment = segment;
    }

    public ResultDemo() {
        super();
    }
    
}
