package com.dms.ptp.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="sc_demo")
public class SCDemo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    
    @Column(name = "demoid")
    private int demoId;
    
    @OneToMany(targetEntity=SCDemoAudience.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "scDemoId", referencedColumnName = "id", nullable=false)
    private List<SCDemoAudience> scDemoAudData;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDemoId() {
        return demoId;
    }

    public void setDemoId(int demoId) {
        this.demoId = demoId;
    }

    public List<SCDemoAudience> getScDemoAudData() {
        return scDemoAudData;
    }

    public void setScDemoAudData(List<SCDemoAudience> scDemoAudData) {
        this.scDemoAudData = scDemoAudData;
    }

    @Override
    public String toString() {
        return "SCDemo [id=" + id + ", demoId=" + demoId + ", scDemoAudData=" + scDemoAudData + "]";
    }
    
    

}
