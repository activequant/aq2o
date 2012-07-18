package com.activequant.trading.datamodel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Timer;
import javax.swing.table.AbstractTableModel;

import com.activequant.trading.AbstractTSBase;
import com.activequant.utils.events.Event;

@SuppressWarnings("serial")
public abstract class AQTableDataBase extends AbstractTableModel {

	private AbstractTSBase abstractTSBase;
	protected final Event<Integer> rowUpdateEvent = new Event<Integer>();
	protected final Event<Object> tableUpdateEvent = new Event<Object>();
	// empty placeholder object.
	private final Object marker = new Object();
	private Timer t = null;
	protected int rowSelected = -1;
	protected int colSelected = -1;
	protected boolean toogleSelection;

	protected DecimalFormat dcf = new DecimalFormat("#.####");

	public AQTableDataBase() {
		dcf.setGroupingUsed(false);		
	}

	public AQTableDataBase(AbstractTSBase abstractTSBase) {
		dcf.setGroupingUsed(false);
		/*
		 * t = new Timer(100, this); t.setInitialDelay(5000); t.start();
		 */this.abstractTSBase = abstractTSBase;
	}

	public String getColumnName(int col) {
		return getHeader()[col];
	}

	public int getRowCount() {
		return getData().length;
	}

	public int getColumnCount() {
		return getHeader().length;
	}

	/**
	 * Returns a FORMATTED representation of the data. 
	 * This is used by Java's SWING layer. 
	 * Do not use this to get the value at a cell.
	 * 
	 * Rather use getCell.
	 */
	public Object getValueAt(int row, int col) {
		Object o = getCell(row, col);
		if (o == null)
			return "";
		if (o.getClass().isAssignableFrom(Double.class)) {
			// adding a formatter.
			Double val = (Double) o;
			return dcf.format(val);
		}
		return o;
	}

	public boolean isCellEditable(int row, int col) {
		return false;
	}

	public void setValueAt(Object value, int row, int col) {
		Object[][] x = getData();
		if (row >= x.length)
			return;
		if (col >= getHeader().length)
			return;
		x[row][col] = value;
	}

	public abstract Object[][] getData();

	public abstract String[] getHeader();

	public Object getCell(int row, int col) {
		if (row > getData().length)
			return null;
		if (col > getData()[row].length)
			return null;
		return getData()[row][col];
	}

	public void signalUpdate() {
		if(!tableUpdateEvent.isEmpty())tableUpdateEvent.fire(marker);
		fireTableDataChanged();
	}

	protected List<Object[]> c(Object[][] in) {
		List<Object[]> rows = new ArrayList<Object[]>();
		for (int i = 0; i < in.length; i++) {
			rows.add(in[i]);
		}
		return rows;
	}

	protected Object[][] c(List<Object[]> in) {
		Object[][] out = new Object[in.size()][];
		for (int i = 0; i < in.size(); i++) {
			out[i] = in.get(i);
		}
		return out;
	}

	public Event<Integer> getRowUpdateEvent() {
		return rowUpdateEvent;
	}

	public AbstractTSBase getAbstractTSBase() {
		return abstractTSBase;
	}

	public int getRowSelected() {
		return rowSelected;
	}

	public void setRowSelected(int rowSelected) {
		this.rowSelected = rowSelected;
	}

	public int getColSelected() {
		return colSelected;
	}

	public void setColSelected(int colSelected) {
		this.colSelected = colSelected;
	}

	public boolean isToogleSelection() {
		return toogleSelection;
	}

	public void setToogleSelection(boolean toogleSelection) {
		this.toogleSelection = toogleSelection;
	}

	public DecimalFormat getDCF() {
		return dcf;
	}

	/**
	 * You can inject a new Decimal Format here. 
	 * @param dcf
	 */
	public void setDCF(DecimalFormat dcf) {
		this.dcf = dcf;
	}

}
