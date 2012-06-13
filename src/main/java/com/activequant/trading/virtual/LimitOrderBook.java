package com.activequant.trading.virtual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.activequant.domainmodel.trade.order.LimitOrder;
import com.activequant.domainmodel.trade.order.OrderSide;
import com.activequant.domainmodel.trade.order.SingleLegOrder;
import com.activequant.trading.AbstractOrderBook;
import com.activequant.trading.orderbook.OrderBookUpdated;

/**
 * See
 * http://journal.r-project.org/archive/2011-1/RJournal_2011-1_Kane~et~al.pdf
 * see http://dl.dropbox.com/u/3001534/engine.c
 * see http://www.mathworks.com/matlabcentral/fileexchange/28156-limit-order-book-simulation
 * 
 * @author ustaudinger
 * 
 */
public class LimitOrderBook extends AbstractOrderBook<LimitOrder> {

	private final String instrumentId;

	private List<LimitOrder> buySide = new ArrayList<LimitOrder>();

	private List<LimitOrder> sellSide = new ArrayList<LimitOrder>();
	
	private final LimitOrderBookMatcher matcher; 
	private final VirtualExchange vex; 
	private Logger log = Logger.getLogger(LimitOrderBook.class);

	public LimitOrderBook(VirtualExchange vex, String tradeableInstrumentId){
		super(tradeableInstrumentId);
		this.instrumentId = tradeableInstrumentId;
		this.matcher = new LimitOrderBookMatcher(vex, this);
		this.vex = vex; 
	}
	
	public void match(){
	    matcher.match();
	}
	
	void weedOutForeignOrders(){
		Iterator<LimitOrder> it = buySide.iterator();
		while(it.hasNext()){
			LimitOrder lo = it.next();
			if(lo.getOrderId()==null)it.remove();
		}
		it = sellSide.iterator();
		while(it.hasNext()){
			LimitOrder lo = it.next();
			if(lo.getOrderId()==null)it.remove();
		}
		
	}
	
	public void addOrders(LimitOrder[] orders) {
		for(LimitOrder order : orders){
			if (order.getOrderSide().equals(OrderSide.BUY)) {
				buySide.add(order);
			} else if (order.getOrderSide().equals(OrderSide.SELL)) {
				sellSide.add(order);
			}
		}
		resortBuySide();
		resortSellSide();
		// signal that there was an update. 
		super.orderBookEvent(new OrderBookUpdated());
	}		
	
	public void addOrder(LimitOrder order) {
		if (order.getOrderSide().equals(OrderSide.BUY)) {
			buySide.add(order);
			resortBuySide();
		} else if (order.getOrderSide().equals(OrderSide.SELL)) {
			sellSide.add(order);
			resortSellSide();
		}
		if(order.getWorkingTimeStamp()==null){
		    // set the working timestamp. 
		    order.setWorkingTimeStamp(vex.currentExchangeTime());
		}
		
		// signal that there was an update. 
		super.orderBookEvent(new OrderBookUpdated());

	}

	public void cancelOrder(SingleLegOrder order) {
		if (order.getOrderSide().equals(OrderSide.BUY)) {
			buySide.remove(order);
			resortBuySide();
		} else if (order.getOrderSide().equals(OrderSide.SELL)) {
			sellSide.remove(order);
			resortSellSide();
		}
		// signal that there was an update. 
		super.orderBookEvent(new OrderBookUpdated());

	}

	private void resortBuySide() {
		 
		
		Collections.sort(buySide, new Comparator<LimitOrder>() {
			@Override
			public int compare(LimitOrder o1, LimitOrder o2) {
				int diff = (int) Math.signum(o2.getLimitPrice() - o1.getLimitPrice());
				return diff; 
			}
		});
		 
//		// dump the buy side orders
//		if(log.isDebugEnabled()){
//			for(LimitOrder l : buySide){
//				System.out.println(l.getOrderId()+" - " + l.getLimitPrice());
//			}
//		}
	}

	private void resortSellSide() {
	
		Collections.sort(sellSide, new Comparator<LimitOrder>() {
			@Override
			public int compare(LimitOrder o1, LimitOrder o2) {
				int diff = (int) Math.signum(o1.getLimitPrice() - o2.getLimitPrice());
				return diff; 
			}
		});
		
//		// dump the sell side orders. 
//		if(log.isDebugEnabled()){
//			for(LimitOrder l : sellSide){
//				System.out.println(l.getOrderId()+" - " + l.getLimitPrice());
//			}
//		}
	}

	public void updateOrder(LimitOrder newOrder) {
		if (newOrder.getOrderSide().equals(OrderSide.BUY)) {
			int index = -1;
			for (int i = 0; i < buySide.size(); i++) {
				if (buySide.get(i).getOrderId() == newOrder.getOrderId()) {
					index = i;
					break;
				}
			}
			if (index != -1) {
				buySide.set(index, newOrder);
				resortBuySide();
			}
		} else if (newOrder.getOrderSide().equals(OrderSide.SELL)) {
			int index = -1;
			for (int i = 0; i < sellSide.size(); i++) {
				if (sellSide.get(i).getOrderId() == newOrder.getOrderId()) {
					index = i;
					break;
				}
			}
			if (index != -1) {
				sellSide.set(index, newOrder);
				resortSellSide();
			}
		}
		// signal that there was an update. 
		super.orderBookEvent(new OrderBookUpdated());

	}

	public List<LimitOrder> buySide() {
		return buySide;
	}

	public void buySide(List<LimitOrder> buySide) {
		this.buySide = buySide;
	}

	public List<LimitOrder> sellSide() {
		return sellSide;
	}

	public void sellSide(List<LimitOrder> sellSide) {
		this.sellSide = sellSide;
	}


}
