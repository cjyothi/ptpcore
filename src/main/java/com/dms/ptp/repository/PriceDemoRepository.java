package com.dms.ptp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dms.ptp.entity.PriceDemo;

/**
 * 
 * @author chidananda.p
 *
 */
public interface PriceDemoRepository extends JpaRepository<PriceDemo, Integer> {
    @Query(value = "{call get_universe_demo_two(:demoOne,:demoTwo,:sdate,:edate,:panelId)}", nativeQuery = true)
    public List<PriceDemo> getDemoUniverseTwoParam(String demoOne, String demoTwo, String sdate, String edate,
            int panelId);

    @Query(value = "{call get_universe_demo_three(:demoOne,:demoTwo,:demoThree,:sdate,:edate,:panelId)}", nativeQuery = true)
    public List<PriceDemo> getDemoUniverseThreeParam(String demoOne, String demoTwo, String demoThree, String sdate,
            String edate, int panelId);

    @Query(value = "{call get_universe_demo_four(:demoOne,:demoTwo,:demoThree,:demoFour,:sdate,:edate,:panelId)}", nativeQuery = true)
    public List<PriceDemo> getDemoUniverseFourParam(String demoOne, String demoTwo, String demoThree, String demoFour,
            String sdate, String edate, int panelId);

    @Query(value = "{call get_universe_demo_five(:demoOne,:demoTwo,:demoThree,:demoFour,:demoFive,:sdate,:edate,:panelId)}", nativeQuery = true)
    public List<PriceDemo> getDemoUniverseFiveParam(String demoOne, String demoTwo, String demoThree, String demoFour,
            String demoFive, String sdate, String edate, int panelId);

    @Query(value = "{call get_universe_demo_six(:demoOne,:demoTwo,:demoThree,:demoFour,:demoFive,:demoSix,:sdate,:edate,:panelId)}", nativeQuery = true)
    public List<PriceDemo> getDemoUniverseSixParam(String demoOne, String demoTwo, String demoThree, String demoFour,
            String demoFive, String demoSix, String sdate, String edate, int panelId);

    @Query(value = "{call get_universe_demo_seven(:demoOne,:demoTwo,:demoThree,:demoFour,:demoFive,:demoSix,:demoSeven,:sdate,:edate,:panelId)}", nativeQuery = true)
    public List<PriceDemo> getDemoUniverseSevenParam(String demoOne, String demoTwo, String demoThree, String demoFour,
            String demoFive, String demoSix, String demoSeven, String sdate, String edate, int panelId);

}
