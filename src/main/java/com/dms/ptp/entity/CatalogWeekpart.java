package com.dms.ptp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="catalog_weekpart")
@Getter
@Setter
public class CatalogWeekpart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int id;
    private String code;
    private String title;
    private double percentage;
    @Column(name="is_average")
    private boolean isAverage;
}
