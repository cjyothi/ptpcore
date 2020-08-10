package com.dms.ptp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="catalog_sales_area")
@Getter
@Setter
public class SalesArea {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private int id;
	@Column(name="sales_area")
	private int salesArea;
	private String name;
	private int spots;
	@Column(name="channel_num")
	private Integer channelNum;

}
