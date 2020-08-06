package com.dms.ptp.dto;

import java.util.Date;
import java.util.List;

public class AffinityResponse {

    private int territory;
    private int platform;
    private int panel;
    private List<Integer> priDemos;
    private List<Integer> secDemos;
    private String sourceStart;
    private String sourceEnd;
    private List<String> dayparts;

    private List<UniverseData> universes;
    private List<DemoAverage> demoAverage;
    private List<AffinityChannel> channels;

    public int getTerritory() {
        return territory;
    }

    public void setTerritory(int territory) {
        this.territory = territory;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public int getPanel() {
        return panel;
    }

    public void setPanel(int panel) {
        this.panel = panel;
    }

    public List<Integer> getPriDemos() {
        return priDemos;
    }

    public void setPriDemos(List<Integer> priDemos) {
        this.priDemos = priDemos;
    }

    public List<Integer> getSecDemos() {
        return secDemos;
    }

    public void setSecDemos(List<Integer> secDemos) {
        this.secDemos = secDemos;
    }

    public String getSourceStart() {
        return sourceStart;
    }

    public void setSourceStart(String sourceStart) {
        this.sourceStart = sourceStart;
    }

    public String getSourceEnd() {
        return sourceEnd;
    }

    public void setSourceEnd(String sourceEnd) {
        this.sourceEnd = sourceEnd;
    }

    public List<String> getDayparts() {
        return dayparts;
    }

    public void setDayparts(List<String> dayparts) {
        this.dayparts = dayparts;
    }

    public List<UniverseData> getUniverses() {
        return universes;
    }

    public void setUniverses(List<UniverseData> universes) {
        this.universes = universes;
    }

    public List<DemoAverage> getDemoAverage() {
        return demoAverage;
    }

    public void setDemoAverage(List<DemoAverage> demoAverage) {
        this.demoAverage = demoAverage;
    }

    public List<AffinityChannel> getChannels() {
        return channels;
    }

    public void setChannels(List<AffinityChannel> channels) {
        this.channels = channels;
    }
}
