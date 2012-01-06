package com.activequant.trading;

import java.util.List;

import com.activequant.domainmodel.TradeableInstrument;
import com.activequant.domainmodel.trade.order.Order;

/**
 * See http://journal.r-project.org/archive/2011-1/RJournal_2011-1_Kane~et~al.pdf 
 * see http://dl.dropbox.com/u/3001534/engine.c 
 *
 * @author ustaudinger
 *
 */
public class OrderBook {
	
	private TradeableInstrument instrument;
	
	private List<Order> buySide;
	private List<Order> sellSide; 
	
	
	public void addOrder(Order order){
		
	}
	
	public void cancelOrder(Order order){
		
	}
	
	public void updateOrder(Order newOrder)
	{
		
	}
	
	public double getQuantityAtLevel(double price){
		return 0.0; 
	}
	
}
