package com.activequant.domainmodel.trade.order;

public class StopOrder extends SingleLegOrder {
	
	private Double stop;

	public Double getStopPrice() {
		return stop;
	}

	public void setStopPrice(Double stop) {
		this.stop = stop;
	}
	
	public StopOrder(){
		
	}
	
	/**
	 * will not clone everything, but just limit price, order side, quantity and tradinstid.
	 */
	public StopOrder clone(){
		StopOrder ret = new StopOrder();
		ret.setCreationTimeStamp(this.getCreationTimeStamp());
		ret.setOpenQuantity(getOpenQuantity());
		ret.setOrderId(getOrderId());
		ret.setStopPrice(stop);
		ret.setOrderSide(getOrderSide());
		ret.setQuantity(getQuantity());
		ret.setTradInstId(getTradInstId());
		return ret; 
	}
}
