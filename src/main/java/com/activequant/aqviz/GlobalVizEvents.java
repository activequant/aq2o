package com.activequant.aqviz;

import com.activequant.utils.events.Event;

/**
 * Can and should be used to signal things throughout the application. 
 * Such as a QUIT or EXIT command.   
 * 
 * @author ustaudinger
 *
 */
public class GlobalVizEvents {

	private static GlobalVizEvents inst = null; 
	private Event<String> event = new Event<String>();
	
	private GlobalVizEvents() {
		// 
	}
	public static GlobalVizEvents getInstance(){
		if(inst==null)
			inst = new GlobalVizEvents();
		return inst; 
	}
	
	public Event<String> getEvent(){
		return event; 
	}
	
	
}
