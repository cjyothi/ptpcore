package com.dms.ptp.service;

import com.dms.ptp.response.UniverseDemoResponse;

/**
 * 
 * @author chidananda.p
 *
 */
public interface PriceDemoService {
    public UniverseDemoResponse getDemoInsightsTwoParam(String demoOne, String demoTwo, String sdate, String edate,
            int panelId);

    public UniverseDemoResponse getDemoInsightsThreeParam(String demoOne, String demoTwo, String demoThree,
            String sdate, String edate, int panelId);

    public UniverseDemoResponse getDemoInsightsFourParam(String demoOne, String demoTwo, String demoThree,
            String demoFour, String sdate, String edate, int panelId);

    public UniverseDemoResponse getDemoInsightsFiveParam(String demoOne, String demoTwo, String demoThree,
            String demoFour, String demoFive, String sdate, String edate, int panelId);

    public UniverseDemoResponse getDemoInsightsSixParam(String demoOne, String demoTwo, String demoThree,
            String demoFour, String demoFive, String demoSix, String sdate, String edate, int panelId);

    public UniverseDemoResponse getDemoInsightsSevenParam(String demoOne, String demoTwo, String demoThree,
            String demoFour, String demoFive, String demoSix, String demoSeven, String sdate, String edate,
            int panelId);

}
