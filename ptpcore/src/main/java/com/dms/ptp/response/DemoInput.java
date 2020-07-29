package com.dms.ptp.response;

/**
 * Post model for Demographic
 */
public class DemoInput {


    int panel;
    String segment;
    String description;

    public int getPanel() {
        return panel;
    }
    public void setPanel(int panel) {
        this.panel = panel;
    }
    public String getSegment() {
        return segment;
    }
    public void setSegment(String segment) {
        this.segment=segment;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public DemoInput() {
        super();
    }
    @Override
    public String toString() {
        return "DemoInput [panel=" + panel + ", position=" + segment + ", description=" + description + "]";
    }



}
