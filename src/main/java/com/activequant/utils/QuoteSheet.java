package com.activequant.utils;

import java.util.HashMap;
import java.util.Map;

import com.activequant.tools.streaming.MarketDataSnapshot;
public class QuoteSheet {
	
	private Map<String, MarketDataSnapshot> map = new HashMap<String, MarketDataSnapshot>();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 
	
	public MarketDataSnapshot getMDS(String instId){
		return map.get(instId);
	}
	
	
	
}
