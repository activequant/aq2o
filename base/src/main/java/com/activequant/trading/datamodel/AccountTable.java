package com.activequant.trading.datamodel;

import java.util.List;

import com.activequant.trading.AbstractTSBase;


@SuppressWarnings("serial")
public class AccountTable extends AQTableDataBase {

	public enum Columns{
		VARIABLE(0, "Variable"),
		VALUE(1, "Value");
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
	
	public AccountTable(){
		super();

		header = new String[Columns.values().length];
		for(int i=0;i<header.length;i++){
			header[i] = Columns.values()[i].name();
		}
	}
	

	public AccountTable(AbstractTSBase abstractTSBase) {
		super(abstractTSBase);
		header = new String[Columns.values().length];
		for(int i=0;i<header.length;i++){
			header[i] = Columns.values()[i].colName();
		}
	}
	

	public void deleteVariable(String variableId){		 
		if(containsVariable(variableId)){
			int pos = getRowIdx(variableId);
			// 
			List<Object[]> l = c(data);		
			l.remove(pos);
			data = c(l);
		}
	}

	public boolean containsVariable(String instId){				 
		for(Object[] row : data){		 
			if(row[Columns.VARIABLE.colIdx].equals(instId))return true; 
		}
		return false; 
	}
	

	/**
	 * returns the row index in which information is stored.
	 * This information never changes unless the table is emptied and new instruments are added. 
	 * 
	 * @param variable
	 * @return
	 */
	public int getRowIdx(String variable){
		int pos = -1; 
		for(Object[] row : data){
			pos++; 
			if(row[Columns.VARIABLE.colIdx].equals(variable))return pos; 
		}
		throw new RuntimeException("Instrument ID not present in current system configuration. Cannot continue. ");
	}
	

	/**
	 * Adds a variable to be shown in the account table. 
	 * 
	 * @param variableId
	 */
	public void addVariable(String variableId){		
		// convert data to list. 
		List<Object[]> l = c(data);		
		Object[] row = new Object[header.length];
		row[Columns.VARIABLE.colIdx] = variableId;
		row[Columns.VALUE.colIdx] = "";		
		l.add(row);		
		data = c(l);
	}
	
	/**
	 * stores a variable value
	 * 
	 * @param variableId
	 * @param value
	 */
	public void setVariable(String variableId, Object value){
		// 
		List<Object[]> rows = c(data);
		Object[] row = null;
		int rowIndex = -1;
		for(int i=0;i<rows.size();i++)
		{
			if(rows.get(i)[Columns.VARIABLE.colIdx].equals(variableId)){
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
		row[Columns.VARIABLE.colIdx] = variableId;
		row[Columns.VALUE.colIdx] = value; 
		// 
		data = c(rows);		
		getRowUpdateEvent().fire(rowIndex);
	}
	
	/**
	 * returns the current position for a variable.
	 * 
	 * @param variableId
	 * @return
	 */
	public Object getCurrentValue(String variableId){
		int pos = -1; 
		for(Object[] row : data){
			pos++; 
			if(row[Columns.VARIABLE.colIdx].equals(variableId))break; 
		}
		if(pos!=-1){
			return data[pos][Columns.VALUE.colIdx];
		}
		return null; 
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
