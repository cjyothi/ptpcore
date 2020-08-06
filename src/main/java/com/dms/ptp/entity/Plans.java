package com.dms.ptp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="catalog_plans")
@Getter
@Setter
public class Plans {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private int id;
	private int weeks;
	@Column(name="busines_type")
	private int businessType;
	private String extRefCode;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="plans_id",referencedColumnName ="id")
	private List<CatalogPricing> priceInfo;

}
