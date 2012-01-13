package com.activequant.trading.virtual;

import java.util.HashMap;
import java.util.Map;

import com.activequant.domainmodel.Date8Time6;
import com.activequant.domainmodel.trade.event.OrderEvent;
import com.activequant.domainmodel.trade.event.OrderFillEvent;
import com.activequant.domainmodel.trade.event.OrderTerminalEvent;
import com.activequant.domainmodel.trade.order.LimitOrder;
import com.activequant.domainmodel.trade.order.Order;
import com.activequant.domainmodel.trade.order.OrderSide;
import com.activequant.tools.streaming.StreamEvent;
import com.activequant.tools.streaming.TimeStreamEvent;
import com.activequant.trading.IOrderTracker;
import com.activequant.trading.NBBOEvent;
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
		if(order.getOrderId()==null)return; 
		IOrderTracker trck = getOrderTracker(order);
		if(trck==null)return; 
		// can only handle our own virtual trackers. 
		if(trck instanceof VirtualOrderTracker)
		{
			OrderFillEvent ofe = new OrderFillEvent();
			ofe.setCreationTimeStamp(currentExchangeTime());
			ofe.setFillAmount(quantity);
			ofe.setFillPrice(price);
			((VirtualOrderTracker)trck).getEvent().fire(ofe);
			
			// 
			if(order instanceof LimitOrder)
			{
				LimitOrder lo = (LimitOrder) order; 
				if(lo.getOpenQuantity()==0){
					OrderTerminalEvent ote = new OrderTerminalEvent();
					ote.setCreationTimeStamp(currentExchangeTime());
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
		if(streamEvent instanceof NBBOEvent)
		{
			NBBOEvent nbbo = (NBBOEvent)streamEvent; 
			String instId = nbbo.getTradeableInstrumentId();
			
			LimitOrderBook lob = getOrderBook(instId);
			
			// clear out limit order book except our own orders. 			
			lob.weedOutForeignOrders();
			
			LimitOrder[] lobs = new LimitOrder[2];
			
			LimitOrder bestBid = new LimitOrder();
			bestBid.setOrderSide(OrderSide.BUY);
			bestBid.setLimitPrice(nbbo.getBid().getA());
			bestBid.setQuantity(nbbo.getBid().getB());
			lobs[0] = (bestBid);
			
			LimitOrder bestAsk = new LimitOrder();
			bestAsk.setOrderSide(OrderSide.SELL);
			bestAsk.setLimitPrice(nbbo.getAsk().getA());
			bestAsk.setQuantity(nbbo.getAsk().getB());
			lobs[1] = (bestAsk);
			
			lob.addOrders(lobs);
		}		
	}

	public LimitOrderBook getOrderBook(String tradeableInstrumentId){
		if(!lobs.containsKey(tradeableInstrumentId))
			lobs.put(tradeableInstrumentId, new LimitOrderBook(this, tradeableInstrumentId));
		return lobs.get(tradeableInstrumentId);
	}
	
	public VirtualExchange() {

	}

}
