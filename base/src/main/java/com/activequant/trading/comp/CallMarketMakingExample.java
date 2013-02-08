package com.activequant.trading.comp;

import com.activequant.component.ComponentBase;
import com.activequant.domainmodel.exceptions.UnsupportedOrderType;
import com.activequant.domainmodel.streaming.MarketDataSnapshot;
import com.activequant.domainmodel.trade.event.OrderEvent;
import com.activequant.domainmodel.trade.order.LimitOrder;
import com.activequant.domainmodel.trade.order.OrderSide;
import com.activequant.interfaces.trading.IOrderTracker;
import com.activequant.interfaces.transport.ITransportFactory;
import com.activequant.interfaces.utils.IEventListener;
import com.activequant.trading.DefaultTransportExchange;
import com.activequant.trading.MarketDataFeedAdapter;
import com.activequant.transport.activemq.ActiveMQTransportFactory;

public class CallMarketMakingExample extends ComponentBase {

	private String quotee = "TT.SFE.APU3.SFE_AP";
	private String[] quotees = new String[] {"TT.SFE.APH324000C.SFE_AP",
			"TT.SFE.APH324250C.SFE_AP",
			"TT.SFE.APH324500C.SFE_AP",
			"TT.SFE.APH324750C.SFE_AP",
			"TT.SFE.APH325000C.SFE_AP",
			"TT.SFE.APH325250C.SFE_AP",
			"TT.SFE.APH325500C.SFE_AP",
			"TT.SFE.APH325750C.SFE_AP",
			"TT.SFE.APH326000C.SFE_AP",
			"TT.SFE.APH326250C.SFE_AP",
			"TT.SFE.APH326500C.SFE_AP",
			"TT.SFE.APH326750C.SFE_AP",
			"TT.SFE.APH327000C.SFE_AP",
			"TT.SFE.APH327250C.SFE_AP",
			"TT.SFE.APH327500C.SFE_AP",
			"TT.SFE.APH327750C.SFE_AP",
			"TT.SFE.APH328000C.SFE_AP",
			"TT.SFE.APH328250C.SFE_AP",
			"TT.SFE.APH328500C.SFE_AP",
			"TT.SFE.APH328750C.SFE_AP",
			"TT.SFE.APH329000C.SFE_AP",
			"TT.SFE.APH329250C.SFE_AP",
			"TT.SFE.APH329500C.SFE_AP",
			"TT.SFE.APH329750C.SFE_AP",
			"TT.SFE.APH330000C.SFE_AP",
			"TT.SFE.APH330250C.SFE_AP",
			"TT.SFE.APH330500C.SFE_AP",
			"TT.SFE.APH330750C.SFE_AP",
			"TT.SFE.APH331000C.SFE_AP",
			"TT.SFE.APH331250C.SFE_AP",
			"TT.SFE.APH331500C.SFE_AP",
			"TT.SFE.APH331750C.SFE_AP"
	
	
	};
	private IOrderTracker[] trackers = new IOrderTracker[quotees.length];
	private double mp = 0.0;
	private boolean initialized = false;
	private DefaultTransportExchange dex;

	public CallMarketMakingExample(ITransportFactory transFac) throws Exception {
		super("CallMarkerMaker", transFac);
		dex = new DefaultTransportExchange(transFac);
		MarketDataFeedAdapter mdfa = new MarketDataFeedAdapter(quotee, transFac) {
			public void processMarketDataSnapshot(MarketDataSnapshot mds) {
				process(mds);
			}
		};

		mdfa.start();

	}

	private void init() {

		Runnable r = new Runnable() {
			public void run() {
				IEventListener<OrderEvent> listener = new IEventListener<OrderEvent>() {
					@Override
					public void eventFired(OrderEvent event) {
						System.out.println("*************** " + event);
					}
				};
				for (int i = 0; i < quotees.length; i++) {
					//
					final int idx = i;
					try {

						LimitOrder lo = new LimitOrder();
						lo.setLimitPrice((double) Math.round(mp - 2000));
						lo.setQuantity(1.0);
						lo.setTradInstId(quotees[idx]);
						lo.setOrderSide(OrderSide.BUY);
						IOrderTracker tracker;
						//
						tracker = dex.prepareOrder(lo);
						tracker.getOrderEventSource()
								.addEventListener(listener);
						//
						trackers[idx] = tracker;
						tracker.submit();
					} catch (UnsupportedOrderType e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				initialized = true; 

			}
		};
		Thread t = new Thread(r);
		t.start();
		
	}

	private void reprice() {
		if (mp != 0.0) {
			for (int i = 0; i < quotees.length; i++) {
				//
				final int idx = i;
				LimitOrder lo = new LimitOrder();
				lo.setLimitPrice((double) Math.round(mp - 2000));
				lo.setQuantity(1.0);
				lo.setTradInstId(quotees[idx]);
				lo.setOrderSide(OrderSide.BUY);
				IOrderTracker t = trackers[i];
				t.update(lo);
			}
		}
	}

	private void process(MarketDataSnapshot mds) {
		int divs = 0;
		mp = 0;
		if (!Double.isNaN(mds.getBidPrices()[0])) {
			mp += mds.getBidPrices()[0];
			divs++;
		}
		if (!Double.isNaN(mds.getAskPrices()[0])) {
			mp += mds.getAskPrices()[0];
			divs++;
		}
		mp /= (double) divs;
		//
		System.out.println("Midpoint: " + mp);

		if (!initialized) {

			init();
		} else {
			// reprice.
			reprice();
		}
		//
	}

	@Override
	public String getDescription() {
		return "a very trivial multi-instrument quoter";
	}

	public static void main(String[] args) throws Exception {
		new CallMarketMakingExample(
				new ActiveMQTransportFactory("27.50.89.125", 61616));
	}
}
