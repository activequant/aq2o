package com.activequant.utils;

/**
 *
 * @author GhostRider
 *
 */
public class SmallMath {
	public static double fastDecimalRound(double value, int digits){
		int dec = (int)Math.pow(10,digits); 
		return (double)Math.round(value * dec) / dec;
	}	
}
