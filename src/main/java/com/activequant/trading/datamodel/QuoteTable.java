package com.activequant.trading.datamodel;

import java.util.List;

import com.activequant.trading.AQTableDataBaseAlpha;
import com.activequant.trading.AbstractTSBase;


@SuppressWarnings("serial")
public class QuoteTable extends AQTableDataBaseAlpha {

	public enum Columns{
		INSTRUMENTID(0),
		BIDSIZE(1), 
		BID(2), 
		MID(3),
		ASK(4),
		ASKSIZE(5), 
		TRADE(6),
		TRADESIZE(7);		
		
		// 
		int colIdx;		
		private Columns(int pos){
			this.colIdx = pos; 
		}		
		public int colIdx(){return colIdx;}
	}
	
	
	private Object[][] data = new Object[0][];
	
	private String[] header;
	
	public QuoteTable(){
		super();
		header = new String[Columns.values().length];
		for(int i=0;i<header.length;i++){
			header[i] = Columns.values()[i].name();
		}
	}
	

	public QuoteTable(AbstractTSBase abstractTSBase) {
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
	
	@Override
	public Object getValueAt(int row, int col) {
		Object o = getCell(row, col);
		if (o == null)
			return "";
		if (o.getClass().isAssignableFrom(Double.class)){
			// adding a formatter. 
			Double val = (Double)o;
			return dcf.format(val);
		}		
		return o;
	}
	

	public boolean containsInstrumentId(String instId){				 
		for(Object[] row : data){		 
			if(row[Columns.INSTRUMENTID.colIdx].equals(instId))return true; 
		}
		return false; 
	}
	
	/**
	 * delegates to get Position 
	 * @param instrumentId
	 * @return
	 */
	public int getRowIndex(String instrumentId){
		int pos = -1; 
		for(Object[] row : data){
			pos++; 
			if(row[Columns.INSTRUMENTID.colIdx].equals(instrumentId))return pos; 
		}
		throw new RuntimeException("Instrument ID not present in current system configuration. Cannot continue. ");
	}
	
	/**
	 * deprecated, users please use getRowIndex in the future .. 
	 * 
	 * @param instId
	 * @return
	 */
	@Deprecated
	public int getPosition(String instId){
		return getRowIndex(instId);
	}
	
	
	public void addInstrument(String instrumentId){		
		// convert data to list. 
		List<Object[]> l = c(data);		
		Object[] row = new Object[header.length];
		row[Columns.INSTRUMENTID.colIdx] = instrumentId;
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


	@Override
	public void signalUpdate() {
		super.signalUpdate();
		getAbstractTSBase().getQuoteViz().changeSelection(getRowSelected(),
				getColSelected(), false, false);

	}
	
}
