package com.activequant.trading;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import com.activequant.domainmodel.Tuple;
import com.activequant.trading.datamodel.AQTableDataBase;

public abstract class AQTableDataBaseAlpha extends AQTableDataBase implements
		ActionListener {
	private List<Tuple<Integer, Integer>> alphaList = new ArrayList<Tuple<Integer, Integer>>();
	private Object[][] alpha = new Integer[0][];
	private boolean enableBlinking;
	private int sleepTime = 100;
	public AQTableDataBaseAlpha() {
		super();
		setEnableBlinking(false);
	}
	
	public AQTableDataBaseAlpha(AbstractTSBase abstractTSBase) {
		super(abstractTSBase);
		setEnableBlinking(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (!enableBlinking) {
			return;
		}
		synchronized (alphaList) {
			for (Tuple<Integer, Integer> t : alphaList) {
				if ((Integer) alpha[t.getA()][t.getB()] == 0) {
					alphaList.remove(t);
					return;
				}
			}
		}
	}

	public void setValueAt(Object value, int row, int col) {
		super.setValueAt(value, row, col);
		if (!enableBlinking) {
			return;
		}
		try {
			alpha[row][col] = 100;
		} catch (ArrayIndexOutOfBoundsException ex) {
			initRow(col);
		}
		synchronized (alphaList) {
			alphaList.add(new Tuple<Integer, Integer>(row, col));
		}
		fireTableCellUpdated(row, col);
	}

	public int getAlpha(int row, int col) {
		if (!enableBlinking) {
			return 0;
		}
		try {
			if (alpha[row][col] == null) {
				alpha[row][col] = 100;
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
			initRow(col);
		}
		synchronized (alphaList) {
			alphaList.add(new Tuple<Integer, Integer>(row, col));
		}
		fireTableCellUpdated(row, col);
		return (Integer) alpha[row][col];
	}

	public void setAlpha(int row, int col, int alpha) {
		if (!enableBlinking) {
			return;
		}
		this.alpha[row][col] = alpha;
	}

	public boolean isEnableBlinking() {
		return enableBlinking;
	}

	public void setEnableBlinking(boolean enableBlinking) {
		this.enableBlinking = enableBlinking;
	}

	private void initRow(int col) {
		Object[] new_row = new Integer[getHeader().length];
		new_row[col] = 100;
		List<Object[]> l = c(alpha);
		l.add(new_row);
		alpha = c(l);
	}

	public int getSleepTime() {
		return sleepTime;
	}

	public void setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
	}
}
