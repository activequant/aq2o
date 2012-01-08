package com.activequant.trading.virtual;

import java.util.HashMap;
import java.util.Map;

import com.activequant.domainmodel.Date8Time6;
import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.trade.event.OrderEvent;
import com.activequant.domainmodel.trade.event.OrderFillEvent;
import com.activequant.domainmodel.trade.event.OrderTerminalEvent;
import com.activequant.domainmodel.trade.order.LimitOrder;
import com.activequant.domainmodel.trade.order.Order;
import com.activequant.tools.streaming.StreamEvent;
import com.activequant.trading.IOrderTracker;
import com.activequant.trading.TimeStreamEvent;
import com.activequant.utils.events.Event;
import com.activequant.utils.events.IEventSource;

public class VirtualExchange {

	private Date8Time6 currentExchangeTime;
	private Map<String, IOrderTracker> orderTrackers = new HashMap<String, IOrderTracker>();
	private Map<String, LimitOrderBook> lobs = new HashMap<String, LimitOrderBook>(); 
	
	public Date8Time6 currentExchangeTime() {
		return currentExchangeTime;
	}

	class VirtualOrderTracker implements IOrderTracker {
		private Event<OrderEvent> event = new Event<OrderEvent>();

		public Event<OrderEvent> getEvent() {
			return event;
		}

		@Override
		public void update(Order newOrder) {
			// TODO Auto-generated method stub

		}

		@Override
		public void submit() {
		}

		@Override
		public String getVenueAssignedId() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public IEventSource<OrderEvent> getOrderEventSource() {
			return event;
		}

		@Override
		public Order getOrder() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void cancel() {
			// TODO Auto-generated method stub

		}
	}

	public IOrderTracker prepareOrder() {
		return new VirtualOrderTracker();
	}

	public IOrderTracker getOrderTracker(Order order) {
		IOrderTracker tracker = orderTrackers.get(order.getOrderId());
		return tracker;
	}

	public void execution(Order order, double price, double quantity){
		IOrderTracker trck = getOrderTracker(order);
		if(trck==null)return; 
		if(trck instanceof VirtualOrderTracker)
		{
			OrderFillEvent ofe = new OrderFillEvent();
			ofe.setFillAmount(quantity);
			ofe.setFillPrice(price);
			((VirtualOrderTracker)trck).getEvent().fire(ofe);
			
			// 
			if(order instanceof LimitOrder)
			{
				LimitOrder lo = (LimitOrder) order; 
				if(lo.getOpenQuantity()==0){
					OrderTerminalEvent ote = new OrderTerminalEvent();
					((VirtualOrderTracker)trck).getEvent().fire(ote);
					// clean up the order tracker. 
					orderTrackers.remove(trck);
				}
			}
		}
	}

	public void processStreamEvent(StreamEvent streamEvent) {
		if (streamEvent instanceof TimeStreamEvent) {
			currentExchangeTime = ((TimeStreamEvent) streamEvent)
					.getTimeStamp();
		}
		
	}

	public LimitOrderBook getOrderBook(String tradeableInstrumentId){
		if(lobs.containsKey(tradeableInstrumentId))
			lobs.put(tradeableInstrumentId, new LimitOrderBook(tradeableInstrumentId));
		return lobs.get(tradeableInstrumentId);
	}
	
	public VirtualExchange(MarketDataInstrument[] instruments,
			int timeStreamGranularityInMs) {

	}

}
