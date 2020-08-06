package com.dms.ptp.response;

public class PanelResponse {
    
    private int id;
    private String name;
    
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
    
    @Override
    public String toString() {
        return "PanelResponse [id=" + id + ", name=" + name + "]";
    }
    
    public PanelResponse() {
        super();
    }
    
    public PanelResponse(int id, String name) {
        super();
        this.id = id;
        this.name = name;
    }
    
    

}
