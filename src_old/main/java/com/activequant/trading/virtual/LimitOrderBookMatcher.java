package com.activequant.trading.virtual;

import java.util.List;

import com.activequant.domainmodel.trade.order.LimitOrder;
import com.activequant.domainmodel.trade.order.MarketOrder;

/**
 * 
 * @author ustaudinger
 * 
 */
public class LimitOrderBookMatcher {

    private LimitOrderBook ob;
    private VirtualExchange exchange;

    public LimitOrderBookMatcher(VirtualExchange exchange, LimitOrderBook ob) {
        this.ob = ob;
        this.exchange = exchange;
    }

    public void match() {
        List<LimitOrder> buySide = ob.buySide();
        List<LimitOrder> sellSide = ob.sellSide();

        while (buySide.size() > 0 && sellSide.size() > 0 && buySide.get(0).getLimitPrice() >= sellSide.get(0).getLimitPrice()) {

            LimitOrder buyOrder = buySide.get(0);
            LimitOrder sellOrder = sellSide.get(0);

            double difference = buyOrder.getOpenQuantity() - sellOrder.getOpenQuantity();
            double executed = 0.0;

            double relevantPrice = sellOrder.getLimitPrice();
            // determine the relevant price.
            if (buyOrder.getWorkingTimeStamp().compareTo(sellOrder.getWorkingTimeStamp()) < 0)
                relevantPrice = buyOrder.getLimitPrice();
            // now also checking if the order that we put in is a market order .. 
            if(sellOrder instanceof MarketOrder && sellOrder.getOrderId()!=null)
            	relevantPrice = buyOrder.getLimitPrice();
            if(buyOrder instanceof MarketOrder && buyOrder.getOrderId()!=null)
            	relevantPrice = sellOrder.getLimitPrice();	

            if (difference > 0) {
                // buy side larger.
                executed = buyOrder.getOpenQuantity() - difference;
                buyOrder.setOpenQuantity(difference);
                sellOrder.setOpenQuantity(0);
            } else if (difference < 0) {
                // sell side larger.
                executed = sellOrder.getOpenQuantity() + difference;
                buyOrder.setOpenQuantity(0);
                sellOrder.setOpenQuantity(Math.abs(difference));
            } else {
                // both fully executed.
                executed = buyOrder.getOpenQuantity(); 
                sellOrder.setOpenQuantity(0);
                buyOrder.setOpenQuantity(0);
            }
            //
            exchange.execution(buyOrder, relevantPrice, executed);
            exchange.execution(sellOrder, relevantPrice, executed);
            //
            if (sellOrder.getOpenQuantity() == 0){
            	if(sellSide.size()>0)sellSide.remove(0);
            }

            if (buyOrder.getOpenQuantity() == 0){
                if(buySide.size()>0)buySide.remove(0);
            }
                

        }

    }
}
