package com.activequant.trading;

import java.util.ArrayList;
import java.util.List;

import com.activequant.domainmodel.orderbook.MarketState;
import com.activequant.domainmodel.orderbook.OrderBookChange;
import com.activequant.domainmodel.orderbook.TransactionEvent;
import com.activequant.domainmodel.trade.order.Order;
import com.activequant.interfaces.trading.IOrderBook;
import com.activequant.interfaces.trading.IOrderBookListener;
/**
 * 
 * @author ustaudinger
 *
 * @param <T>
 */
public abstract class AbstractOrderBook<T extends Order> implements IOrderBook<T> {

	private final String tradeableId;
	private MarketState marketState; 
	private List<IOrderBookListener> orderBookListeners = new ArrayList<IOrderBookListener>();
	
	public AbstractOrderBook(String tradeableId){
		this.tradeableId = tradeableId; 
	}
	
	@Override
	public String getTradeableInstrumentId() {
		return tradeableId; 
	}

	@Override
	public MarketState getMarketState() {
		return marketState; 
	}

	@Override
	public void attachOrderBookListener(IOrderBookListener listener) {
		orderBookListeners.add(listener);
	}

	@Override
	public void detachOrderBookListener(IOrderBookListener listener) {
		orderBookListeners.remove(listener);
	}

	protected void orderBookEvent(OrderBookChange obc)
	{
		for(IOrderBookListener l : orderBookListeners)
			l.orderBookChange(obc);
	}

	protected void marketStateChange(MarketState newState)
	{
		this.marketState = newState; 
		for(IOrderBookListener l : orderBookListeners)
			l.marketStateChange(newState);
	}
	
	protected void transaction(TransactionEvent te)
	{
		for(IOrderBookListener l : orderBookListeners)
			l.transactionEvent(te);
	}
	
}

