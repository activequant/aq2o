package com.activequant.trading.datamodel;

import java.util.List;

import com.activequant.trading.AbstractTSBase;


@SuppressWarnings("serial")
public class InstrumentTable extends AQTableDataBase {

	public enum Columns{
		INSTRUMENTID(0),
		TRADEABLEID(1),
		CURRENCY(2), 
		LASTTRADDATE(3), 
		TICKSIZE(4),
		TICKVALUE(5), 
		OPENTIME(6),
		CLOSETIME(7);		
		
		// 
		int colIdx;		
		private Columns(int pos){
			this.colIdx = pos; 
		}		
		public int colIdx(){return colIdx;}
	}

	private Object[][] data = new Object[0][];
	
	private String[] header = new String[]{"InstrumentID", "TradeableID", "currency", "lastTradingDate", "tickSize", "tickValue", "marketStart", "marketEnd"};
	
	public InstrumentTable(){
		super();
	}

	public InstrumentTable(AbstractTSBase abstractTSBase) {
		super(abstractTSBase);
	}


	public void addInstrument(String instrumentId, String tradeableId, String currency, long lastTradDate8, double tickSize, double tickVal, double openTime6, double closeTime6){		
		// convert data to list. 
		List<Object[]> l = c(data);		
		Object[] row = new Object[header.length];
		row[0] = instrumentId;
		row[1] = tradeableId;
		row[2] = currency; 
		row[3] = lastTradDate8;
		row[4] = tickSize; 
		row[5] = tickVal; 
		row[6] = openTime6; 
		row[7] = closeTime6; 
		l.add(row);		
		data = c(l);
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
	public Object[][] getData() {
		return data;
	}

	@Override
	public String[] getHeader() {
		return header;
	}

	public boolean containsInstrumentId(String instId){				 
		for(Object[] row : data){		 
			if(row[0].equals(instId))return true; 
		}
		return false; 
	}
	
	
	public int getRowIndexOf(String mdiId){
		int pos = -1; 
		for(Object[] row : data){
			pos++; 
			if(row[0].equals(mdiId))return pos; 
		}
		throw new RuntimeException("MDI ID not present in current system configuration. Cannot continue. ");
	}
	
	/**
	 * rather use getRowIndexOf
	 * @param instId
	 * @return
	 */
	@Deprecated
	public int getPosition(String instId){
		return getRowIndexOf(instId);
	}
}
