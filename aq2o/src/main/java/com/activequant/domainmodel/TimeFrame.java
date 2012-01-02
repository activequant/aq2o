package com.activequant.domainmodel;

public enum TimeFrame {
    EOD(-1), MINUTES_1(1), MINUTES_5(5), MINUTES_10(10), MINUTES_30(30), HOURS_1(60), RAW(0);
    private int minutes;

    private TimeFrame(int minutes) {
        this.minutes = minutes;
    }

    public int getMinutes() {
        return minutes;
    }
}