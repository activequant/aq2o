package com.activequant.domainmodel;

/**
 * Enumeration about time frames. 
 * Resolution in minutes. 
 * New timeframes can be added on request, just open a support ticket.  
 * 
 * @author GhostRider
 *
 */
public enum TimeFrame {
    EOD(1440), MINUTES_1(1), MINUTES_5(5), MINUTES_10(10), MINUTES_30(30), HOURS_1(60),  HOURS_2(120),HOURS_4(240), RAW(0), EOM(43200);
    private int minutes;

    private TimeFrame(int minutes) {
        this.minutes = minutes;
    }

    public int getMinutes() {
        return minutes;
    }
    
    public long getNanoseconds(){
    	return minutes * 60l * 1000l * 1000l * 1000l; 
    }
}