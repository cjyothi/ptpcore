package com.dms.ptp.response;

import java.util.List;

public class SCDemoDetail {
    
    private int id;
    private List<SCAudienceInfo> audienceInfo;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public List<SCAudienceInfo> getAudienceInfo() {
        return audienceInfo;
    }
    public void setAudienceInfo(List<SCAudienceInfo> audienceInfo) {
        this.audienceInfo = audienceInfo;
    }
    
    public SCDemoDetail() {
        super();
    }
    
    
    
    public SCDemoDetail(int id, List<SCAudienceInfo> audienceInfo) {
        super();
        this.id = id;
        this.audienceInfo = audienceInfo;
    }
    
    @Override
    public String toString() {
        return "SCDemoDetail [id=" + id + ", audienceInfo=" + audienceInfo + "]";
    }
    

}
