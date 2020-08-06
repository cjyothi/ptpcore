package com.dms.ptp.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CatalogRequest {

	private int id;
	private String title1;
	private String title2;
	private String shortDescr;
	private String type;
	private String category;
	private String graphic;
	private String logo;
	private int isAlcohol;
	private List<PlansDTO> plans;
}
