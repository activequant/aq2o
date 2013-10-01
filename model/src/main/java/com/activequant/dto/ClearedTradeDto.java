package com.activequant.dto;

import com.activequant.domainmodel.trade.order.OrderSide;

public class ClearedTradeDto {
    
    public String tradeableId;
    public Double price;
    public Long quantity;
    public String clearedTradeId;
    public Long timeStampInNanos;
    public OrderSide orderSide;
    public String clearingAccountId;
    public TransactionType status; 
    public double clearingFee, brokerFee, exchangeFee; 
    public String clearingFeeCurrency, brokerFeeCurrency, exchangeFeeCurrency; 
    public String cusip;
    public String uniqueId;
    public String subAccountId; 
    public Long date8;
    
    public String toString() {
        return tradeableId + "/" + price + "/" + quantity + "/" + clearedTradeId + "/" + timeStampInNanos + "/" + orderSide + "/" + clearingAccountId;
    }

}
