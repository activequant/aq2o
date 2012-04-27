package com.activequant.trading.datamodel;

import java.util.List;

import com.activequant.trading.AbstractTSBase;


@SuppressWarnings("serial")
public class PositionTable extends AQTableDataBase {

	public enum Columns{
		INSTRUMENTID(0),
		POSITION(1), 
		ENTRYPRICE(2);		
		// 
		int colIdx;		
		private Columns(int pos){
			this.colIdx = pos; 
		}		
		public int colIdx(){return colIdx;}
	}
	
	
	private Object[][] data = new Object[0][];
	
	private String[] header;
	
	public PositionTable(){
		super();

		header = new String[Columns.values().length];
		for(int i=0;i<header.length;i++){
			header[i] = Columns.values()[i].name();
		}
	}
	

	public PositionTable(AbstractTSBase abstractTSBase) {
		super(abstractTSBase);
		header = new String[Columns.values().length];
		for(int i=0;i<header.length;i++){
			header[i] = Columns.values()[i].name();
		}
	}
	

	public void deleteInstrument(String instrumentId){		 
		if(containsInstrumentId(instrumentId)){
			int pos = getPosition(instrumentId);
			// 
			List<Object[]> l = c(data);		
			l.remove(pos);
			data = c(l);
		}
	}

	public boolean containsInstrumentId(String instId){				 
		for(Object[] row : data){		 
			if(row[Columns.INSTRUMENTID.colIdx].equals(instId))return true; 
		}
		return false; 
	}
	
	public int getPosition(String instId){
		int pos = -1; 
		for(Object[] row : data){
			pos++; 
			if(row[Columns.INSTRUMENTID.colIdx].equals(instId))return pos; 
		}
		throw new RuntimeException("Instrument ID not present in current system configuration. Cannot continue. ");
	}
	

	public void addInstrument(String instrumentId){		
		// convert data to list. 
		List<Object[]> l = c(data);		
		Object[] row = new Object[header.length];
		row[Columns.INSTRUMENTID.colIdx] = instrumentId;
		l.add(row);		
		data = c(l);
	}
	
	
	public void setPosition(String instrumentId, Double price, Double quantity){
		// 
		List<Object[]> rows = c(data);
		Object[] row = null;
		int rowIndex = -1;
		for(int i=0;i<rows.size();i++)
		{
			if(rows.get(i)[Columns.INSTRUMENTID.colIdx].equals(instrumentId)){
				row = rows.get(i);
				rowIndex = i; 
				break; 
			}
		}
		if(row == null){
			row =new Object[header.length];			
			rows.add(row);
			rowIndex = rows.size()-1;
		}
		
		// 
		row[Columns.INSTRUMENTID.colIdx] = instrumentId;
		row[Columns.ENTRYPRICE.colIdx] = price; 
		row[Columns.POSITION.colIdx] = quantity; 
		
		// 
		data = c(rows);		
		getRowUpdateEvent().fire(rowIndex);
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
