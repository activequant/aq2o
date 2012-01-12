package com.activequant.trading.virtual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.activequant.domainmodel.trade.order.LimitOrder;
import com.activequant.domainmodel.trade.order.OrderSide;
import com.activequant.domainmodel.trade.order.SingleLegOrder;
import com.activequant.trading.AbstractOrderBook;

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

	public LimitOrderBook(String tradeableInstrumentId){
		super(tradeableInstrumentId);
		this.instrumentId = tradeableInstrumentId; 
	}
	
	public void addOrder(LimitOrder order) {
		if (order.getOrderSide().equals(OrderSide.BUY)) {
			buySide.add(order);
			resortBuySide();
		} else if (order.getOrderSide().equals(OrderSide.SELL)) {
			sellSide.add(order);
			resortSellSide();
		}
	}

	public void cancelOrder(SingleLegOrder order) {
		if (order.getOrderSide().equals(OrderSide.BUY)) {
			buySide.remove(order);
			resortBuySide();
		} else if (order.getOrderSide().equals(OrderSide.SELL)) {
			sellSide.remove(order);
			resortSellSide();
		}
	}

	private void resortBuySide() {
		Collections.sort(buySide, new Comparator<LimitOrder>() {
			@Override
			public int compare(LimitOrder o1, LimitOrder o2) {
				return (int) (o2.getLimitPrice() - o1.getLimitPrice());
			}
		});
	}

	private void resortSellSide() {
		Collections.sort(sellSide, new Comparator<LimitOrder>() {
			@Override
			public int compare(LimitOrder o1, LimitOrder o2) {
				return (int) (o1.getLimitPrice() - o2.getLimitPrice());
			}
		});
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
