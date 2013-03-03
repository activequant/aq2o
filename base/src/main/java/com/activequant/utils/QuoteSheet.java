package com.activequant.utils;

import java.util.HashMap;
import java.util.Map;

import com.activequant.domainmodel.streaming.MarketDataSnapshot;
public class QuoteSheet {
	
	private Map<String, MarketDataSnapshot> map = new HashMap<String, MarketDataSnapshot>();
	// 
	
	public MarketDataSnapshot getMDS(String instId){
		return map.get(instId);
	}
	
	
	
}
