package com.activequant.dto;

import com.activequant.domainmodel.trade.order.OrderSide;

public class OrderFillDto {
    
    public String tradeableId;
    public Double price;
    public Long quantity;
    public String brokerId;
    public String providerId;
    public Long timeStampInNanos;
    public OrderSide orderSide;
    public String brokerAccountId;
    public String providerAccountId;
    public String orderFillId; 
    public String orderId; 
    public String routeId; 
   

    public String toString() {
        return tradeableId + "/" + price + "/" + quantity + "/" + brokerId + "/" + timeStampInNanos + "/" + orderSide + "/" ;
    }

}
