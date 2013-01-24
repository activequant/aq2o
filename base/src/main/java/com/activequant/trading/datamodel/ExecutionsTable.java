package com.activequant.trading.datamodel;

import java.text.SimpleDateFormat;
import java.util.List;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.trading.AbstractTSBase;


@SuppressWarnings("serial")
public class ExecutionsTable extends AQTableDataBase {

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss"); 
	
	public enum Columns{		
		ORDERID(0),
		EXECID(1),
		TIMESTAMP(2),
		INSTRUMENTID(3),		
		SIDE(4), 
		PRICE(5),
		QUANTITY(6);
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
	
	public void addExecution(String orderId, String execId, TimeStamp ts, String instrumentId, String side, double price, double quantity){		
		// convert data to list. 
		List<Object[]> l = c(data);		
		Object[] row = new Object[header.length];
		row[Columns.ORDERID.colIdx] = orderId; 
		row[Columns.EXECID.colIdx] = execId; 
		row[Columns.TIMESTAMP.colIdx] = sdf.format(ts.getCalendar().getTime());
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
