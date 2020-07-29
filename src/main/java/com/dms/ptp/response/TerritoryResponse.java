package com.dms.ptp.response;

public class TerritoryResponse {
    private int id;
    private String name;

    public TerritoryResponse(int id, String name) {
        this.id=id;
        this.name=name;
    }

    public TerritoryResponse() {
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
