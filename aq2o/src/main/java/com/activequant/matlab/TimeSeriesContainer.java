package com.activequant.matlab;

/**
 * return object for matlab.
 * 
 * @author ustaudinger
 * 
 */
public class TimeSeriesContainer {

    private double[] timeStamps;
    private double[][][] values;
    private String[][] marketDataInstruments;

    public String[][] marketDataInstruments() {
        return marketDataInstruments;
    }

    public void marketDataInstruments(String[][] marketDataInstruments) {
        this.marketDataInstruments = marketDataInstruments;
    }

    public double[] timeStamps() {
        return timeStamps;
    }

    public void timeStamps(double[] timeStamps) {
        this.timeStamps = timeStamps;
    }

    public double[][][] values() {
        return values;
    }

    public void values(double[][][] values) {
        this.values = values;
    }
}
