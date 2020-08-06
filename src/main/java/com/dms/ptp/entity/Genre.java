package com.dms.ptp.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.List;

/**
 * Table creation of Genre
 */
@Entity
@Table(name = "genre")
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id",scope = Genre.class)
public class Genre {

    @Id
    @Column(name="id")
    private int id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "genres")
    List<Channel> channels;

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
