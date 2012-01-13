package com.activequant.trading.virtual;

import com.activequant.domainmodel.trade.order.LimitOrder;
import com.activequant.exceptions.IncompleteOrderInstructions;
import com.activequant.exceptions.UnsupportedOrderType;
import com.activequant.tools.streaming.StreamEvent;
import com.activequant.trading.IOrderTracker;

public class TradingSystemSample {

	private IExchange exchange; 
	public TradingSystemSample(IExchange exchange){
		this.exchange = exchange; 
	}
	
	public void handle(StreamEvent streamEvent){
		if(Math.random()<0.05)
		{
			try {
				IOrderTracker orderTracker = exchange.prepareOrder(new LimitOrder());
				orderTracker.submit();
			} catch (UnsupportedOrderType e) {
				e.printStackTrace();
			} catch (IncompleteOrderInstructions e) {
				e.printStackTrace();
			}
		}
	}
	
}
