package com.activequant.trading.virtual;

import java.util.HashMap;
import java.util.Map;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.trade.event.OrderEvent;
import com.activequant.domainmodel.trade.event.OrderFillEvent;
import com.activequant.domainmodel.trade.event.OrderTerminalEvent;
import com.activequant.domainmodel.trade.order.LimitOrder;
import com.activequant.domainmodel.trade.order.Order;
import com.activequant.domainmodel.trade.order.OrderSide;
import com.activequant.exceptions.IncompleteOrderInstructions;
import com.activequant.exceptions.UnsupportedOrderType;
import com.activequant.tools.streaming.BBOEvent;
import com.activequant.tools.streaming.StreamEvent;
import com.activequant.tools.streaming.TimeStreamEvent;
import com.activequant.trading.IOrderTracker;
import com.activequant.utils.events.Event;
import com.activequant.utils.events.IEventSource;

public class VirtualExchange implements IExchange {

	private long virtexOrderId = 0L;
	private TimeStamp currentExchangeTime = new TimeStamp(0L);
	private Map<String, IOrderTracker> orderTrackers = new HashMap<String, IOrderTracker>();
	private Map<String, LimitOrderBook> lobs = new HashMap<String, LimitOrderBook>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.activequant.trading.virtual.IExchange#currentExchangeTime()
	 */
	@Override
	public TimeStamp currentExchangeTime() {
		return currentExchangeTime;
	}

	class VirtualOrderTracker implements IOrderTracker {
		private Event<OrderEvent> event = new Event<OrderEvent>();
		private LimitOrder order;

		VirtualOrderTracker(LimitOrder order) throws IncompleteOrderInstructions {
			this.order = order;
			if (order.getTradInstId() == null)
				throw new IncompleteOrderInstructions("TradInstID missing");

		}

		public Event<OrderEvent> getEvent() {
			return event;
		}

		@Override
		public void update(Order newOrder) {
			if (newOrder instanceof LimitOrder) {
				LimitOrder le = (LimitOrder)newOrder;
				getOrderBook(le.getTradInstId()).updateOrder(le);
				this.order.setQuantity(le.getQuantity());
				this.order.setLimitPrice(le.getLimitPrice());
			}
		}

		@Override
		public void submit() {
			getOrderBook(order.getTradInstId()).addOrder(order);
			order.setOpenQuantity(order.getQuantity());
			order.setWorkingTimeStamp(currentExchangeTime());
			if (order.getOrderId() == null)
				order.setOrderId("OID" + virtexOrderId++);
			// add it to the list of local order trackers.
			orderTrackers.put(order.getOrderId(), this);

		}

		@Override
		public String getVenueAssignedId() {
			return "VIRTEXOID" + (virtexOrderId++);
		}

		@Override
		public IEventSource<OrderEvent> getOrderEventSource() {
			return event;
		}

		@Override
		public Order getOrder() {
			return order;
		}

		@Override
		public void cancel() {
			getOrderBook(order.getTradInstId()).cancelOrder(order);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.activequant.trading.virtual.IExchange#prepareOrder()
	 */
	@Override
	public IOrderTracker prepareOrder(Order order) throws UnsupportedOrderType, IncompleteOrderInstructions {
		if (!(order instanceof LimitOrder))
			throw new UnsupportedOrderType("Order type not supported by exchange: " + order);
		LimitOrder limitOrder = (LimitOrder) order;
		if (limitOrder.getQuantity() <= 0.0)
			throw new IncompleteOrderInstructions("Invalid quantity given: " + limitOrder.getQuantity());
		if (limitOrder.getLimitPrice() == null)
			throw new IncompleteOrderInstructions("No limit price given.");
		if (limitOrder.getLimitPrice() <= 0.0)
			throw new IncompleteOrderInstructions("Invalid negative limit price given.");
		VirtualOrderTracker tracker = new VirtualOrderTracker(limitOrder);
		return tracker;
	}

	public IOrderTracker getOrderTracker(Order order) {
		IOrderTracker tracker = orderTrackers.get(order.getOrderId());
		return tracker;
	}

	public void execution(Order order, double price, double quantity) {
		if (order.getOrderId() == null)
			return;
		IOrderTracker trck = getOrderTracker(order);
		if (trck == null)
			return;
		// can only handle our own virtual trackers.
		if (trck instanceof VirtualOrderTracker) {
			OrderFillEvent ofe = new OrderFillEvent();
			ofe.setCreationTimeStamp(currentExchangeTime());
			ofe.setFillAmount(quantity);
			ofe.setFillPrice(price);
			((VirtualOrderTracker) trck).getEvent().fire(ofe);

			//
			if (order instanceof LimitOrder) {
				LimitOrder lo = (LimitOrder) order;
				if (lo.getOpenQuantity() == 0) {
					OrderTerminalEvent ote = new OrderTerminalEvent();
					ote.setCreationTimeStamp(currentExchangeTime());
					((VirtualOrderTracker) trck).getEvent().fire(ote);
					// clean up the order tracker.
					orderTrackers.remove(trck);
				}
			}
		}
	}

	public void processStreamEvent(StreamEvent streamEvent) {
		if (streamEvent instanceof TimeStreamEvent) {
			currentExchangeTime = ((TimeStreamEvent) streamEvent).getTimeStamp();
		}
		if (streamEvent instanceof BBOEvent) {
			BBOEvent nbbo = (BBOEvent) streamEvent;
			String instId = nbbo.getTradeableInstrumentId();
			// weed out non-tradeable market data.
			if (instId == null)
				return;

			LimitOrderBook lob = getOrderBook(instId);

			// clear out limit order book except our own orders.
			lob.weedOutForeignOrders();

			if (nbbo.getBid() != null) {

				LimitOrder bestBid = new LimitOrder();
				bestBid.setOrderSide(OrderSide.BUY);
				bestBid.setLimitPrice(nbbo.getBid());
				bestBid.setQuantity(nbbo.getBidQuantity());
				bestBid.setOpenQuantity(bestBid.getQuantity());
				lob.addOrder(bestBid);
			}

			if (nbbo.getAsk() != null) {
				LimitOrder bestAsk = new LimitOrder();
				bestAsk.setOrderSide(OrderSide.SELL);
				bestAsk.setLimitPrice(nbbo.getAsk());
				bestAsk.setQuantity(nbbo.getAskQuantity());
				bestAsk.setOpenQuantity(bestAsk.getQuantity());
				lob.addOrder(bestAsk);

			}
			// rerun a match.
			lob.match();

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.activequant.trading.virtual.IExchange#getOrderBook(java.lang.String)
	 */
	@Override
	public LimitOrderBook getOrderBook(String tradeableInstrumentId) {
		if (!lobs.containsKey(tradeableInstrumentId))
			lobs.put(tradeableInstrumentId, new LimitOrderBook(this, tradeableInstrumentId));
		return lobs.get(tradeableInstrumentId);
	}

	@Override
	public IOrderTracker getOrderTracker(String orderId) {
		return orderTrackers.get(orderId);
	}

}
