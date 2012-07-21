package com.activequant.utils;

/**
 * Very dumb request id generator. 
 * Thread safe through synchronized, spits out the next id. 
 * 
 * @author ustaudinger
 *
 */
public class RequestIdGenerator {

	private static long id = 1L; 
	public static synchronized long next(){
		return id++;
	}
	
}
