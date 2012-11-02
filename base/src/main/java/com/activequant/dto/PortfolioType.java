package com.activequant.dto;

public enum PortfolioType {
	Intended(0), Model(1);
	
	int t; 
	private PortfolioType(int t){
		this.t = t; 
	}
}
