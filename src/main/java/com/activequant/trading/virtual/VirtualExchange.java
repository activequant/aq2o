package com.activequant.trading.virtual;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.activequant.domainmodel.Portfolio;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.trade.event.OrderAcceptedEvent;
import com.activequant.domainmodel.trade.event.OrderCancelSubmittedEvent;
import com.activequant.domainmodel.trade.event.OrderCancelledEvent;
import com.activequant.domainmodel.trade.event.OrderEvent;
import com.activequant.domainmodel.trade.event.OrderFillEvent;
import com.activequant.domainmodel.trade.event.OrderReplacedEvent;
import com.activequant.domainmodel.trade.event.OrderSubmittedEvent;
import com.activequant.domainmodel.trade.event.OrderTerminalEvent;
import com.activequant.domainmodel.trade.event.OrderUpdateSubmittedEvent;
import com.activequant.domainmodel.trade.order.LimitOrder;
import com.activequant.domainmodel.trade.order.Order;
import com.activequant.domainmodel.trade.order.OrderSide;
import com.activequant.exceptions.IncompleteOrderInstructions;
import com.activequant.exceptions.TransportException;
import com.activequant.exceptions.UnsupportedOrderType;
import com.activequant.tools.streaming.BBOEvent;
import com.activequant.tools.streaming.MarketDataSnapshot;
import com.activequant.tools.streaming.OrderStreamEvent;
import com.activequant.tools.streaming.PositionEvent;
import com.activequant.tools.streaming.StreamEvent;
import com.activequant.tools.streaming.TimeStreamEvent;
import com.activequant.trading.IOrderTracker;
import com.activequant.transport.ETransportType;
import com.activequant.transport.ITransportFactory;
import com.activequant.utils.events.Event;
import com.activequant.utils.events.IEventListener;
import com.activequant.utils.events.IEventSource;

public class VirtualExchange implements IExchange {

	private long virtexOrderId = 0L;
	private TimeStamp currentExchangeTime = new TimeStamp(0L);
	private Map<String, IOrderTracker> orderTrackers = new HashMap<String, IOrderTracker>();
	private Map<String, LimitOrderBook> lobs = new HashMap<String, LimitOrderBook>();
	private final Event<OrderEvent> globalOrderEvent = new Event<OrderEvent>();
	private ITransportFactory transport;
	private Logger log = Logger.getLogger(VirtualExchange.class);
	private Portfolio clientPortfolio = new Portfolio();

	public VirtualExchange(ITransportFactory transport) {
		this.transport = transport;
	}

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
		private OrderEvent lastState;

		VirtualOrderTracker(LimitOrder order) throws IncompleteOrderInstructions {
			this.order = order;
			if (order.getTradInstId() == null)
				throw new IncompleteOrderInstructions("TradInstID missing");
			event.addEventListener(new IEventListener<OrderEvent>() {
				@Override
				public void eventFired(OrderEvent event) {
					lastState = event;
				}
			});

		}

		public Event<OrderEvent> getEvent() {
			return event;
		}

		@Override
		public void update(Order newOrder) {
			if (newOrder instanceof LimitOrder) {
				LimitOrder le = (LimitOrder) newOrder;
				le.setWorkingTimeStamp(currentExchangeTime);
				getOrderBook(le.getTradInstId()).updateOrder(le);
				this.order.setQuantity(le.getQuantity());
				this.order.setLimitPrice(le.getLimitPrice());

				// set out an update event.
				OrderEvent oe = new OrderUpdateSubmittedEvent();
				oe.setCreationTimeStamp(currentExchangeTime);
				oe.setRefOrder(le);
				oe.setRefOrderId(le.getOrderId());
				getEvent().fire(oe);
				sendOrderEvent(le.getTradInstId(), oe);

				//
				oe = new OrderReplacedEvent();
				oe.setRefOrder(le);
				oe.setRefOrderId(le.getOrderId());
				oe.setCreationTimeStamp(currentExchangeTime);
				getEvent().fire(oe);
				sendOrderEvent(le.getTradInstId(), oe);

				getOrderBook(le.getTradInstId()).match();
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

			// send out the submit event

			OrderEvent oe = new OrderSubmittedEvent();
			oe.setCreationTimeStamp(currentExchangeTime);
			oe.setRefOrderId(order.getOrderId());
			oe.setRefOrder(order);
			getEvent().fire(oe);
			sendOrderEvent(order.getTradInstId(), oe);

			// ... and the accepted event

			oe = new OrderAcceptedEvent();
			oe.setCreationTimeStamp(currentExchangeTime);
			oe.setRefOrderId(order.getOrderId());
			oe.setRefOrder(order);
			getEvent().fire(oe);
			sendOrderEvent(order.getTradInstId(), oe);

			getOrderBook(order.getTradInstId()).match();
			
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
			OrderEvent oe = new OrderCancelSubmittedEvent();
			oe.setCreationTimeStamp(currentExchangeTime);
			getEvent().fire(oe);
			oe = new OrderCancelledEvent();
			oe.setCreationTimeStamp(currentExchangeTime);
			getEvent().fire(oe);
		}

		public OrderEvent lastState() {
			return lastState;
		}
	}

	private void sendOrderEvent(String tradInstId, OrderEvent oe) {
		try {
			
			transport.getPublisher(ETransportType.TRAD_DATA, tradInstId).send(
					new OrderStreamEvent(tradInstId, oe.getCreationTimeStamp(), oe));
			globalOrderEvent.fire(oe);
		} catch (TransportException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
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
//		if (limitOrder.getLimitPrice() <= 0.0)
//			throw new IncompleteOrderInstructions("Invalid negative limit price given.");
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
			LimitOrder lo = ((LimitOrder)order);
			OrderFillEvent ofe = new OrderFillEvent();
			ofe.setCreationTimeStamp(currentExchangeTime());
			ofe.setRefOrder(order);
			ofe.setRefOrderId(order.getOrderId());
			ofe.setSide(lo.getOrderSide().toString());
			ofe.setOptionalInstId(lo.getTradInstId());
			ofe.setFillAmount(quantity);
			ofe.setFillPrice(price);
			((VirtualOrderTracker) trck).getEvent().fire(ofe);
			sendOrderEvent(lo.getTradInstId(), ofe);
			
			updatePortfolio(lo.getTradInstId(), price, quantity, lo.getOrderSide().getSide());
			
			
			//
			if (order instanceof LimitOrder) {
				LimitOrder lo2 = (LimitOrder) order;
				if (lo.getOpenQuantity() == 0.0) {
					OrderTerminalEvent ote = new OrderTerminalEvent();
					ote.setCreationTimeStamp(currentExchangeTime());
					((VirtualOrderTracker) trck).getEvent().fire(ote);
					// also send it to the internal event layer. 					
					// clean up the order tracker.
					orderTrackers.remove(trck);					
					// ok, we also send out the position event. 					
				}
			}			
		}
	}
	
	
	private void updatePortfolio(String tdiId, Double price, Double lastFill, int side) {
		// update the position.
		//
		double currentPosition = clientPortfolio.getPosition(tdiId);
		log.info("Current position " + currentPosition);
		double openPrice = clientPortfolio.getOpenPrice(tdiId);
		double newPosition = currentPosition + side * lastFill;
		
		double newOpenPrice = price; 
		/// ((currentPosition * openPrice) + (lastFill * side * price));
		
		if(Math.signum(newPosition)==Math.signum(currentPosition)){
			newOpenPrice = ((currentPosition * openPrice) + (lastFill * price)) / newPosition;
		}
		
		
		
		//
		if (newPosition == 0.0)
			newOpenPrice = 0.0;

		setPosition(tdiId, newOpenPrice, newPosition);
		log.info("Order side: " + side + ", " + price + ", " + lastFill);
		log.info("Portfolio updated. New position for " + tdiId + ": " + newPosition + " @ " + newOpenPrice);
		//
	}

	private void setPosition(String tradeableId, Double price, Double newPosition) {
		clientPortfolio.setPosition(tradeableId, price, newPosition);
		// send out a position event.
		PositionEvent pe = new PositionEvent(tradeableId, new TimeStamp(), price, newPosition);
		pe.setTimeStamp(currentExchangeTime);
		pe.setCreationTime(currentExchangeTime.getNanoseconds());
		try {
			transport.getPublisher(ETransportType.TRAD_DATA, tradeableId).send(pe);
		} catch (TransportException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public void processStreamEvent(StreamEvent streamEvent) {
		if (streamEvent instanceof TimeStreamEvent) {
			currentExchangeTime = ((TimeStreamEvent) streamEvent).getTimeStamp();
		}
		if (streamEvent instanceof MarketDataSnapshot) {
			MarketDataSnapshot mds = (MarketDataSnapshot) streamEvent;
			String tdiId = mds.getTdiId();
			// weed out non-tradeable market data.
			if (tdiId == null)
				return;

			// lookup the tdi for this mdi id. 			
			LimitOrderBook lob = getOrderBook(tdiId);
			
			// clear out limit order book except our own orders.
			lob.weedOutForeignOrders();
			if (mds.getBidPrices() != null && mds.getBidPrices().length > 0) {
				LimitOrder bestBid = new LimitOrder();
				bestBid.setWorkingTimeStamp(currentExchangeTime);
				bestBid.setOrderSide(OrderSide.BUY);
				bestBid.setLimitPrice(mds.getBidPrices()[0]);
				bestBid.setQuantity(mds.getBidSizes()[0]);
				bestBid.setOpenQuantity(mds.getBidSizes()[0]);
				lob.addOrder(bestBid);
			}
			if (mds.getAskPrices() != null && mds.getAskPrices().length > 0) {
				LimitOrder bestAsk = new LimitOrder();
				bestAsk.setOrderSide(OrderSide.SELL);
				bestAsk.setWorkingTimeStamp(currentExchangeTime);
				bestAsk.setLimitPrice(mds.getAskPrices()[0]);
				bestAsk.setQuantity(mds.getAskSizes()[0]);
				bestAsk.setOpenQuantity(mds.getAskSizes()[0]);
				lob.addOrder(bestAsk);
			}
			// rerun a match.
			lob.match();

		} else if (streamEvent instanceof BBOEvent) {
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

		} else {
			log.info("Dropping unknown event type.");
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

	public Event<OrderEvent> getGlobalOrderEvent() {
		return globalOrderEvent;
	}

}
