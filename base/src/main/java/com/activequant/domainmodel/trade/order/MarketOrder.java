package com.activequant.domainmodel.trade.order;

public class MarketOrder extends LimitOrder {
	
	public Double getLimitPrice() {
		if(super.getOrderSide()==OrderSide.BUY)
			return Double.POSITIVE_INFINITY;
		else return Double.NEGATIVE_INFINITY; 
			
		
	}


}
