package com.activequant.trading.datamodel;

import java.util.List;

import com.activequant.trading.AbstractTSBase;

@SuppressWarnings("serial")
public class OrderTable extends AQTableDataBase {

	public enum Columns {
		ORDERID(0), INSTRUMENTID(1), 
		ORDERTYPE(2), SIDE(3), PRICE(4), ORDERQUANTITY(5), 
		FILLEDQUANTITY(6);
		//
		int colIdx;

		private Columns(int pos) {
			this.colIdx = pos;
		}

		public int colIdx() {
			return colIdx;
		}
	}

	private Object[][] data = new Object[0][];

	private String[] header;
	
	public OrderTable(){
		super();

		header = new String[Columns.values().length];
		for(int i=0;i<header.length;i++){
			header[i] = Columns.values()[i].name();
		}
	}
	

	public OrderTable(AbstractTSBase abstractTSBase) {
		super(abstractTSBase);
		header = new String[Columns.values().length];
		for(int i=0;i<header.length;i++){
			header[i] = Columns.values()[i].name();
		}
	}
	public void addOrder(String orderId, String instrumentId, String orderType, String side, double price,
			double totalQuantity, double filledQuantity) {
		Object[] row = new Object[header.length];
		// convert data to list.
		List<Object[]> l = c(data);
		int index = getOrderPos(orderId);
		if(index!=-1){
			// order already known, not re-adding.
			row = l.get(index);
		}		
		row[Columns.ORDERID.colIdx] = orderId;
		row[Columns.INSTRUMENTID.colIdx] = instrumentId;
		row[Columns.ORDERTYPE.colIdx] = orderType;
		row[Columns.SIDE.colIdx] = side;
		row[Columns.PRICE.colIdx] = price;
		row[Columns.ORDERQUANTITY.colIdx] = totalQuantity;
		row[Columns.FILLEDQUANTITY.colIdx] = filledQuantity * 1.0;
		if(index==-1)l.add(row);
		data = c(l);
	}
	
	public void delOrder(String orderId){
		int i = getOrderPos(orderId);
		if(i!=-1){
			List<Object[]> l = c(data);
			l.remove(i);
			data = c(l);
		}
	}
	
	private int getOrderPos(String orderId){
		int index = -1; 
		for(int i=0;i<data.length;i++){
			if(orderId.equals(data[i][Columns.ORDERID.colIdx]))
				return i; 
		}
		return index; 
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
