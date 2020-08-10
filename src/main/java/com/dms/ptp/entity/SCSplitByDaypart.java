package com.dms.ptp.entity;

import javax.persistence.*;

@Entity
@Table(name="sc_split_by_daypart")
public class SCSplitByDaypart {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    
    @Column(name = "dayPart")
    private String dayPart;
    
    @Column(name = "percentage")
    private int percentage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDayPart() {
        return dayPart;
    }

    public void setDayPart(String dayPart) {
        this.dayPart = dayPart;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    @Override
    public String toString() {
        return "SCSplitByDaypart [id=" + id + ", dayPart=" + dayPart + ", percentage=" + percentage + "]";
    }
    

}
