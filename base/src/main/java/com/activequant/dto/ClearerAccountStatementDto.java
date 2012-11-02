package com.activequant.dto;

public class ClearerAccountStatementDto {

	public String accountId; 
	public String subAccountId; 
	public String currency; // 
	public long date8;
	public String assumedTargetCurrency; 
	public double crossRate;
	public double initialMargin; 
	public double maintenanceMargin; 
	public double availableMargin; 
	public double beginningAccountBalance;
	public double endingAccountBalance; 
	public double openTradeEquity; 
	public double totalEquity; // = NAV in local currency 
	
}
