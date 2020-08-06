package com.dms.ptp.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "panel")
public class Panel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "panel_id", referencedColumnName = "id")
    private List<Demo> demographicList;

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

    public List<Demo> getDemographicList() {
        return demographicList;
    }

    public void setDemographicList(List<Demo> demographicList) {
        this.demographicList=demographicList;
    }
}
