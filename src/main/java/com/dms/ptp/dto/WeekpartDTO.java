package com.dms.ptp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeekpartDTO {
    private String code;
    private String title;
    private double percentage;
    private boolean isAverage;
}
