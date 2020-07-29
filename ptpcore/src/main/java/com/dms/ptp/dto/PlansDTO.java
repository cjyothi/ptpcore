package com.dms.ptp.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlansDTO {

	private int weeks;
	private double spotRate;
	private int spots;
	private int  spotLength;
	private double rate;
	private int businessType;
	private List<SalesAreaDTO> salesAreaList;

}
