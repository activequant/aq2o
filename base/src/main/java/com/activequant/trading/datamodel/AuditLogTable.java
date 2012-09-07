package com.activequant.trading.datamodel;

import java.text.SimpleDateFormat;
import java.util.List;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.trading.AbstractTSBase;


@SuppressWarnings("serial")
public class AuditLogTable extends AQTableDataBase {

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss.SSS");
	public enum Columns{
		TIMESTAMP(0),
		TEXT(1); 
		int colIdx;		
		private Columns(int pos){
			this.colIdx = pos; 
		}		
		public int colIdx(){return colIdx;}
	}
	
	
	private Object[][] data = new Object[0][];
	
	private String[] header;

	public AuditLogTable(){
		super();
		header = new String[Columns.values().length];
		for(int i=0;i<header.length;i++){
			header[i] = Columns.values()[i].name();
		}
	}
	
	@Override
	public void clear(){
		data = new Object[0][];
	}

	public AuditLogTable(AbstractTSBase abstractTSBase) {
		super(abstractTSBase);
		header = new String[Columns.values().length];
		for(int i=0;i<header.length;i++){
			header[i] = Columns.values()[i].name();
		}
	}
	
	public void addAudit(TimeStamp timeStamp, String text){		
		// convert data to list. 
		List<Object[]> l = c(data);		
		Object[] row = new Object[header.length];
		row[Columns.TIMESTAMP.colIdx] = sdf.format(timeStamp.getDate());
		row[Columns.TEXT.colIdx] = text;
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
