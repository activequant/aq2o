package com.activequant.domainmodel.orderbook;

public abstract class MarketState {
	private String tdiId = null; 
	public void setTdiId(String tdiId) {
		this.tdiId = tdiId;
	}
	private String text; 
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public MarketState(){}
	public MarketState(String tdiId){
		this.tdiId =tdiId; 
	}
	public String getTdiId(){return tdiId;}
	public abstract String getType();
}
