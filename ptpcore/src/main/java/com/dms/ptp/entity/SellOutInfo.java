package com.dms.ptp.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class SellOutInfo {
    @Id
    private Integer id;
    private String channel_name;
    private String daypart;
    private Double percentage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public String getDaypart() {
        return daypart;
    }

    public void setDaypart(String daypart) {
        this.daypart = daypart;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((channel_name == null) ? 0 : channel_name.hashCode());
        result = prime * result + ((daypart == null) ? 0 : daypart.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((percentage == null) ? 0 : percentage.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SellOutInfo other = (SellOutInfo) obj;
        if (channel_name == null) {
            if (other.channel_name != null)
                return false;
        } else if (!channel_name.equals(other.channel_name))
            return false;
        if (daypart == null) {
            if (other.daypart != null)
                return false;
        } else if (!daypart.equals(other.daypart))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (percentage == null) {
            if (other.percentage != null)
                return false;
        } else if (!percentage.equals(other.percentage))
            return false;
        return true;
    }

}
