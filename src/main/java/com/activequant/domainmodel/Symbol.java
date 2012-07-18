package com.activequant.domainmodel;

import com.activequant.domainmodel.annotations.Property;

public class Symbol extends Instrument {

	private String symbol, exchange; 
	
    public Symbol() {
        super(Symbol.class.getCanonicalName());
    }

    public Symbol(String symbol, String exchange) {
        super(Symbol.class.getCanonicalName());
    }

    public String getId() {
        return "SYM." + nullSafe(symbol)+"."+nullSafe(exchange);
    }

    @Property
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

    @Property
	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

}
