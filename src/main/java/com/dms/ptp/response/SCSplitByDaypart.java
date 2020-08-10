package com.dms.ptp.response;

public class SCSplitByDaypart {

    
    private String dayPart;
    private int percentage;
    
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
    
    public SCSplitByDaypart() {
        super();
    }

    public SCSplitByDaypart(String dayPart, int percentage) {
        this.dayPart=dayPart;
        this.percentage=percentage;
    }

    @Override
    public String toString() {
        return "SCSplitByDaypart [dayPart=" + dayPart + ", percentage=" + percentage + "]";
    }
    
    
}
