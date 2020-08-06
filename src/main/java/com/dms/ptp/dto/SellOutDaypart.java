package com.dms.ptp.dto;

public class SellOutDaypart {
    private String daypart;
    private Double so;

    public String getDaypart() {
        return daypart;
    }

    public void setDaypart(String daypart) {
        this.daypart = daypart;
    }

    public Double getSo() {
        return so;
    }

    public void setSo(Double so) {
        this.so = so;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((daypart == null) ? 0 : daypart.hashCode());
        result = prime * result + ((so == null) ? 0 : so.hashCode());
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
        SellOutDaypart other = (SellOutDaypart) obj;
        if (daypart == null) {
            if (other.daypart != null)
                return false;
        } else if (!daypart.equals(other.daypart))
            return false;
        if (so == null) {
            if (other.so != null)
                return false;
        } else if (!so.equals(other.so))
            return false;
        return true;
    }

}
