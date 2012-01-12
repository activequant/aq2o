package com.activequant.trading.virtual;

import java.util.List;

import com.activequant.domainmodel.trade.order.LimitOrder;

/**
 * 
 * @author ustaudinger
 * 
 */
public class OrderBookMatcher {

	private LimitOrderBook ob;
	private VirtualExchange exchange;

	public OrderBookMatcher(VirtualExchange exchange, LimitOrderBook ob) {
		this.ob = ob;
		this.exchange = exchange;
	}

	public void match() {
		List<LimitOrder> buySide = ob.buySide();
		List<LimitOrder> sellSide = ob.sellSide();

		if (buySide.size() > 0 && sellSide.size() > 0) {
			while (buySide.get(0).getLimitPrice() <= sellSide.get(0)
					.getLimitPrice()) {
				
				
				LimitOrder buyOrder = buySide.get(0);
				LimitOrder sellOrder = sellSide.get(0);
				
				
				double difference = buyOrder.getOpenQuantity()
						- sellOrder.getOpenQuantity();

				double relevantPrice = sellOrder.getLimitPrice();
				// determine the relevant price.
				if (buyOrder.getWorkingTimeStamp()
						.before(sellOrder.getWorkingTimeStamp()))
					relevantPrice = buyOrder.getLimitPrice();

				if (difference > 0) {
					// buy side larger.
					buyOrder.setOpenQuantity(difference);
					sellOrder.setOpenQuantity(0);
				} else if (difference < 0) {
					// sell side larger.
					buyOrder.setOpenQuantity(0);
					sellOrder.setOpenQuantity(Math.abs(difference));
				} else {
					// both fully executed.
					sellOrder.setOpenQuantity(0);
					buyOrder.setOpenQuantity(0);
				}
				// 
				exchange.execution(buyOrder, relevantPrice, Math.abs(difference));
				exchange.execution(sellOrder, relevantPrice, Math.abs(difference));
				// 
				if(sellOrder.getOpenQuantity()==0)
					sellSide.remove(0);

				if(buyOrder.getOpenQuantity()==0)
					buySide.remove(0);
				
			}
		}

	}
}
