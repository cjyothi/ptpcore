package com.dms.ptp.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="catalog")
@Getter
@Setter
public class Catalog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String title1;
	private String title2;
	@Column(name = "short_descr")
	private String shortDescr;
	private String type;
	private String category;
	private String graphic;
	private String logo;
	private int isAlcohol;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "catalog_id",referencedColumnName = "id")
	private List<Plans> plans;

}
