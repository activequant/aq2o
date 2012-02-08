package com.activequant.domainmodel.trade.order;

public enum OrderSide {
    BUY(0), SELL(1);
    private int side;

    private OrderSide(int side) {
        this.side = side;
    }

    public int getSide() {
        return this.side;
    }
    
    public boolean equals(OrderSide other){
        return(this.getSide() == other.getSide());
    }
    

    public String toString() {
        if (side == 0)
            return "BUY";
        else
            return "SELL";
    }
}
