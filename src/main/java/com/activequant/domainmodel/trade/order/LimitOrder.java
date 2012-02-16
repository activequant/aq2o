package com.activequant.domainmodel.trade.order;

public class LimitOrder extends SingleLegOrder {
	
	private Double limitPrice;

	public Double getLimitPrice() {
		return limitPrice;
	}

	public void setLimitPrice(Double limitPrice) {
		this.limitPrice = limitPrice;
	}
	
	public LimitOrder(){
		
	}
	
	/**
	 * will not clone everything, but just limit price, order side, quantity and tradinstid.
	 */
	public LimitOrder clone(){
		LimitOrder ret = new LimitOrder();
		ret.setCreationTimeStamp(this.getCreationTimeStamp());
		ret.setOpenQuantity(getOpenQuantity());
		ret.setOrderId(getOrderId());
		ret.setLimitPrice(limitPrice);
		ret.setOrderSide(getOrderSide());
		ret.setQuantity(getQuantity());
		ret.setTradInstId(getTradInstId());
		return ret; 
	}
}
