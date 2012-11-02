package com.activequant.dto;

import com.activequant.domainmodel.trade.order.OrderSide;

public class PositionDto {

	public String tradeableId;
	public Double valuationPrice; 
	public Double quantity; 
	public Double entryPrice;
	public OrderSide side; 
	public String subAcctId; 
	public String clearerAcctId; 	
	public String clearer; 
	public Long entryDate8; 
	public Long positionDate8;
	public String uniqueId;
	public Double marketValue;
	
	public String toString(){
		return tradeableId+ "/"+valuationPrice+"/"+quantity+"/"+entryPrice+"/"+side+
				"/"+subAcctId+"/"+clearerAcctId+"/"+clearer+"/"+entryDate8+"/"+positionDate8
				+"/"+uniqueId+"/"+marketValue
				;
	}
	
}
