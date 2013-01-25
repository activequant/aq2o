package com.activequant.trading.comp;

import com.activequant.component.ComponentBase;
import com.activequant.domainmodel.trade.event.OrderEvent;
import com.activequant.domainmodel.trade.order.LimitOrder;
import com.activequant.domainmodel.trade.order.OrderSide;
import com.activequant.interfaces.trading.IOrderTracker;
import com.activequant.interfaces.transport.ITransportFactory;
import com.activequant.interfaces.utils.IEventListener;
import com.activequant.trading.DefaultTransportExchange;
import com.activequant.transport.activemq.ActiveMQTransportFactory;

public class ComponentExample2 extends ComponentBase {

	private String mdi = "TT.CME.00A0CO00ESZ.ES";
	
	public ComponentExample2(ITransportFactory transFac)
			throws Exception {
		super("component2", transFac);
		
		DefaultTransportExchange dex = new DefaultTransportExchange(transFac);
		LimitOrder lo = new LimitOrder();
		lo.setLimitPrice(143575.0);
		lo.setQuantity(1.0);
		lo.setTradInstId(mdi);
		lo.setOrderSide(OrderSide.BUY);
		
		IOrderTracker tracker = dex.prepareOrder(lo);	
		tracker.getOrderEventSource().addEventListener(new IEventListener<OrderEvent>() {			
			@Override
			public void eventFired(OrderEvent event) {
				System.out.println("*************** " + event);
			}
		});
		//
		tracker.submit();
		
		Thread.sleep(10000);
		
		
		lo.setLimitPrice(143675.0);
		tracker.update(lo);
		
		
		Thread.sleep(10000);
		tracker.cancel();
	}

	@Override
	public String getDescription() {
		return "this is our new componen.";
	}

	

	public static void main(String[] args) throws Exception {
		new ComponentExample2(new ActiveMQTransportFactory("localhost",61616));
	}
}
