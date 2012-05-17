package com.activequant.backtesting;

import java.util.ArrayList;
import java.util.List;

import com.activequant.domainmodel.trade.event.OrderEvent;
import com.activequant.domainmodel.trade.event.OrderFillEvent;
import com.activequant.utils.events.IEventListener;

/**
 * 
 * @author GhostRider
 *
 */
public class OrderEventListener implements IEventListener<OrderEvent> {
	
	private List<OrderFillEvent> fillEvents = new ArrayList<OrderFillEvent>();
	
	@Override
	public void eventFired(OrderEvent event) {
		if(event instanceof OrderFillEvent){
			OrderFillEvent ofe = (OrderFillEvent) event; 
			fillEvents.add(ofe);
		}
	}
	
	public List<OrderFillEvent> getFillEvents() {
		return fillEvents;
	}
}
