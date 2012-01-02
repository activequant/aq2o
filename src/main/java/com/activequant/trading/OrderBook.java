package com.activequant.trading;

import java.util.List;

import com.activequant.domainmodel.TradeableInstrument;
import com.activequant.domainmodel.trade.Order;

public class OrderBook {
	private TradeableInstrument instrument;
	private List<Order> buyOrders;
	private List<Order> sellOrders; 
}
