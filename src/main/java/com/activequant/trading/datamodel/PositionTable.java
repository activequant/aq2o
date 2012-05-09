package com.activequant.trading.datamodel;

import java.util.List;

import com.activequant.trading.AbstractTSBase;


@SuppressWarnings("serial")
public class PositionTable extends AQTableDataBase {

	public enum Columns{
		INSTRUMENTID(0, "InstrumentID"),
		POSITION(1, "Position"), 
		ENTRYPRICE(2, "EntryPX"), 
		PNLATLIQUIDATION(3, "Liquidation PnL");
		// 
		int colIdx;	
		String colname; 
		private Columns(int pos, String colname){
			this.colIdx = pos; 
			this.colname = colname; 
		}		
		public int colIdx(){return colIdx;}
		public String colName(){return colname;}
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
			header[i] = Columns.values()[i].colName();
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
	
	/**
	 * Use getRowIdx instead
	 * @param instId
	 * @return
	 */
	@Deprecated
	public int getPosition(String instId){
		return getRowIdx(instId);
	}
	
	/**
	 * returns the row index in which information is stored.
	 * This information never changes unless the table is emptied and new instruments are added. 
	 * 
	 * @param instId
	 * @return
	 */
	public int getRowIdx(String instId){
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
		row[Columns.POSITION.colIdx] = 0.0;
		row[Columns.ENTRYPRICE.colIdx] = 0.0;
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
