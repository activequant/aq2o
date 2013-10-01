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
	EOD(1440), MINUTES_1(1), MINUTES_2(2), MINUTES_3(3), MINUTES_5(5), MINUTES_10(10), MINUTES_15(15), MINUTES_30(30), HOURS_1(60),  HOURS_2(120),HOURS_4(240), RAW(0);
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

    public static TimeFrame getTimeFrame(int minutes) {
      switch(minutes) {
        case 10:
          return TimeFrame.MINUTES_10;
        case 30:
          return TimeFrame.MINUTES_30;
        case 60:
          return TimeFrame.HOURS_1;
        case 5:
          return TimeFrame.MINUTES_5;
        case 3:
          return TimeFrame.MINUTES_3;
        case 2:
          return TimeFrame.MINUTES_2;
      }
      return null;
    }
}
