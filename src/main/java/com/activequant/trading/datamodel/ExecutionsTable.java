package com.activequant.trading.datamodel;

import java.util.List;

import com.activequant.trading.AbstractTSBase;


@SuppressWarnings("serial")
public class ExecutionsTable extends AQTableDataBase {

	public enum Columns{
		ORDERID(0),
		INSTRUMENTID(1),		
		SIDE(2), 
		PRICE(3),
		QUANTITY(4);
		// 
		int colIdx;		
		private Columns(int pos){
			this.colIdx = pos; 
		}		
		public int colIdx(){return colIdx;}
	}
	
	
	private Object[][] data = new Object[0][];
	
	private String[] header;
	
	
	public ExecutionsTable(){
		super();

		header = new String[Columns.values().length];
		for(int i=0;i<header.length;i++){
			header[i] = Columns.values()[i].name();
		}
	}
	

	public ExecutionsTable(AbstractTSBase abstractTSBase) {
		super(abstractTSBase);
		header = new String[Columns.values().length];
		for(int i=0;i<header.length;i++){
			header[i] = Columns.values()[i].name();
		}
	}
	
	public void addExecution(String orderId, String instrumentId, String side, double price, double quantity){		
		// convert data to list. 
		List<Object[]> l = c(data);		
		Object[] row = new Object[header.length];
		row[Columns.ORDERID.colIdx] = orderId; 
		row[Columns.INSTRUMENTID.colIdx] = instrumentId;
		row[Columns.SIDE.colIdx] = side;
		row[Columns.PRICE.colIdx] = price;
		row[Columns.QUANTITY.colIdx] = quantity;		
		l.add(row);		
		data = c(l);
	}
	
	@Override
	public Object[][] getData() {
		return data;
	}

	@Override
	public String[] getHeader() {
		return header;
	}
	
}
