package com.activequant.backtesting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.trade.event.OrderAcceptedEvent;
import com.activequant.domainmodel.trade.event.OrderCancelledEvent;
import com.activequant.domainmodel.trade.event.OrderEvent;
import com.activequant.domainmodel.trade.event.OrderFillEvent;
import com.activequant.timeseries.TSContainer2;
import com.activequant.timeseries.TypedColumn;
import com.activequant.utils.events.IEventListener;

/**
 * 
 * @author GhostRider
 *
 */
public class OrderEventListener implements IEventListener<OrderEvent> {
	
	private final List<OrderFillEvent> fillEvents = new ArrayList<OrderFillEvent>();
	
	private final TSContainer2 positionOverTime = new TSContainer2("POSITIONSOVERTIME", new ArrayList<String>(), new ArrayList<TypedColumn>());
	private final Map<String, Double> currentPos = new HashMap<String, Double>();
	
	private final Map<String, Integer> fills = new HashMap<String, Integer>();
	private final Map<String, Integer> cancellations = new HashMap<String, Integer>();
	private final Map<String, Integer> updates = new HashMap<String, Integer>();
	
	@Override
	public void eventFired(OrderEvent event) {
		if(event instanceof OrderFillEvent){
			OrderFillEvent ofe = (OrderFillEvent) event; 
			fillEvents.add(ofe);
			trackFill(ofe.getOptionalInstId(),  ofe.getCreationTimeStamp(), ofe.getFillAmount() * (ofe.getSide().startsWith("B")?1.0:-1.0));
			countFill(ofe.getOptionalInstId());
		}
		else if(event instanceof OrderCancelledEvent){
			countCancellation( ((OrderCancelledEvent)event).getOptionalInstId());
		}
		else if(event instanceof OrderAcceptedEvent){
			countUpdate( ((OrderAcceptedEvent)event).getOptionalInstId());
		}
	}

	
	private void trackFill(String id, TimeStamp ts, double change){
		Double val = currentPos.get(id);
		if(val==null)val= 0.0; 
		val = val + change;
		currentPos.put(id, val);
		positionOverTime.setValue(id, ts, val);
		
	}
	private void countFill(String id) {
		// count it. 
		Integer val = fills.get(id);
		if(val==null)val= 0; 
		val += 1;
		fills.put(id, val);
	}
	
	private void countCancellation(String id) {
		// count it. 
		Integer val = cancellations.get(id);
		if(val==null)val= 0; 
		val += 1;
		cancellations.put(id, val);
	}
	private void countUpdate(String id) {
		// count it. 
		Integer val = updates.get(id);
		if(val==null)val= 0; 
		val += 1;
		updates.put(id, val);
	}
	
	
	public List<OrderFillEvent> getFillEvents() {
		return fillEvents;
	}

	public TSContainer2 getPositionOverTime() {
		return positionOverTime;
	}

	public Map<String, Integer> getFills() {
		return fills;
	}


	public Map<String, Integer> getCancellations() {
		return cancellations;
	}


	public Map<String, Integer> getUpdates() {
		return updates;
	}


}
