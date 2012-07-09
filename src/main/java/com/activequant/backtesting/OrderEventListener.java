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
import com.activequant.timeseries.TSContainerMethods;
import com.activequant.timeseries.TypedColumn;
import com.activequant.utils.events.IEventListener;

/**
 * 
 * @author GhostRider
 * 
 */
public class OrderEventListener implements IEventListener<OrderEvent> {

	private final List<OrderFillEvent> fillEvents = new ArrayList<OrderFillEvent>();

	private final TSContainer2 changeOverTime = new TSContainer2("CHANGEOVERTIME", new ArrayList<String>(),
			new ArrayList<TypedColumn>());
	private final TSContainer2 positionOverTime = new TSContainer2("POSITIONSOVERTIME", new ArrayList<String>(),
			new ArrayList<TypedColumn>());
	private final TSContainer2 executionOverTime = new TSContainer2("POSITIONVALUATION", new ArrayList<String>(),
			new ArrayList<TypedColumn>());
	private final Map<String, Double> currentPos = new HashMap<String, Double>();
	private final Map<String, TimeStamp> refTimeStamp = new HashMap<String, TimeStamp>();
	private final Map<String, Double> currentChange = new HashMap<String, Double>();

	private final Map<String, Integer> placed = new HashMap<String, Integer>();
	private final Map<String, Integer> fills = new HashMap<String, Integer>();
	private final Map<String, Integer> cancellations = new HashMap<String, Integer>();
	private final Map<String, Integer> updates = new HashMap<String, Integer>();
	private IFeeCalculator feeCalculator = null; 
	
	public OrderEventListener(){
	}	
	
	@Override
	public void eventFired(OrderEvent event) {
		if (event instanceof OrderFillEvent) {
			OrderFillEvent ofe = (OrderFillEvent) event;
			fillEvents.add(ofe);
			trackFill(ofe.getOptionalInstId(), ofe.getCreationTimeStamp(), ofe.getFillAmount()
					* (ofe.getSide().startsWith("B") ? 1.0 : -1.0), ofe.getFillPrice());
			countFill(ofe.getOptionalInstId());
		} else if (event instanceof OrderCancelledEvent) {
			countCancellation(((OrderCancelledEvent) event).getOptionalInstId());
		} else if (event instanceof OrderAcceptedEvent) {
			countAccepted(((OrderAcceptedEvent) event).getOptionalInstId());
		}
		if(feeCalculator != null)
			feeCalculator.track(event);
	}

	private void trackFill(String tdiId, TimeStamp ts, double change, double price) {
		Double val = currentPos.get(tdiId);
		if (val == null)
			val = 0.0;
		val = val + change;
		currentPos.put(tdiId, val);
		positionOverTime.setValue(tdiId, ts, val);
		executionOverTime.setValue(tdiId, ts, price);
		TimeStamp ref = refTimeStamp.get(tdiId);
		Double cc = 0.0;
		if (ref != null) {
			if(ref.getNanoseconds() !=  ts.getNanoseconds()){
				refTimeStamp.put(tdiId, ts);							
			}
			else
				cc = currentChange.get(tdiId);						
		}
		else
			refTimeStamp.put(tdiId, ts);
		cc+=change; 
		currentChange.put(tdiId, cc);
		changeOverTime.setValue(tdiId, ts, cc);
	}

	private void countAccepted(String id) {
		// count it.
		Integer val = placed.get(id);
		if (val == null)
			val = 0;
		val += 1;
		placed.put(id, val);
	}

	private void countFill(String id) {
		// count it.
		Integer val = fills.get(id);
		if (val == null)
			val = 0;
		val += 1;
		fills.put(id, val);
	}

	private void countCancellation(String id) {
		// count it.
		Integer val = cancellations.get(id);
		if (val == null)
			val = 0;
		val += 1;
		cancellations.put(id, val);
	}

	private void countUpdate(String id) {
		// count it.
		Integer val = updates.get(id);
		if (val == null)
			val = 0;
		val += 1;
		updates.put(id, val);
	}

	public List<OrderFillEvent> getFillEvents() {
		return fillEvents;
	}

	public TSContainer2 getPositionOverTime() {
		// overwrite all nulls with former value.
		TSContainerMethods tcm = new TSContainerMethods();
		return tcm.overwriteNull(positionOverTime);
	}

	public TSContainer2 getExecutionOverTime() {
		// overwrite all nulls with former value.
		TSContainerMethods tcm = new TSContainerMethods();
		return tcm.overwriteNull(executionOverTime, 0.0);
	}

	
	public TSContainer2 getChangeOverTime() {
		// overwrite all nulls with former value.
		TSContainerMethods tcm = new TSContainerMethods();
		return tcm.overwriteNull(changeOverTime, 0.0);
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

	public Map<String, Integer> getPlaced() {
		return placed;
	}

	public IFeeCalculator getFeeCalculator() {
		return feeCalculator;
	}

	public void setFeeCalculator(IFeeCalculator feeCalculator) {
		this.feeCalculator = feeCalculator;
	}

}
