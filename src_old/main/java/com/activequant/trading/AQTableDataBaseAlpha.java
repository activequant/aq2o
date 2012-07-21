package com.activequant.trading;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.Timer;

import com.activequant.domainmodel.Tuple;
import com.activequant.trading.datamodel.AQTableDataBase;

public abstract class AQTableDataBaseAlpha extends AQTableDataBase {
	private ConcurrentHashMap<String, Integer> alphaList = new ConcurrentHashMap<String, Integer>();
	private boolean enableBlinking;
	// Sleep time
	private int sleepTime = 50;

	Timer time = null;

	public AQTableDataBaseAlpha() {
		super();
		setEnableBlinking(false);
	}

	public AQTableDataBaseAlpha(AbstractTSBase abstractTSBase) {
		super(abstractTSBase);
		setEnableBlinking(false);

		/*
		 * Timer timer = new Timer(sleepTime, this);
		 * timer.setInitialDelay(sleepTime); timer.start();
		 */
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					while (true) {
						synchronized (alphaList) {
							for (String t : alphaList.keySet()) {
								if ((Integer) alphaList.get(t) == 0) {
									alphaList.remove(t);
								} else {
									int newAlpha = alphaList.get(t) - 10;
									alphaList.put(t, newAlpha);
								}
							}
						}
						Thread.sleep((long) (sleepTime));
						signalUpdate();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		t.setDaemon(true);
		t.start();
	}

	public void setValueAt(Object value, int row, int col) {
		super.setValueAt(value, row, col);
		if (!enableBlinking) {
			return;
		}
		synchronized (alphaList) {
			alphaList.put("" + row + ":" + col, 100);
		}
	}

	public int getAlpha(int row, int col) {
		if (!enableBlinking) {
			return 0;
		}
		synchronized (alphaList) {
			if (alphaList.get(""+row+":"+col) == null) {
				return 0;
			} else {
				return alphaList.get(""+row+":"+col);
			}
		}
	}

	public void setAlpha(int row, int col, int alpha) {
		if (!enableBlinking) {
			return;
		}
		synchronized (alphaList) {
			alphaList.put(""+row+":"+col, alpha);
		}
	}

	public boolean isEnableBlinking() {
		return enableBlinking;
	}

	public void setEnableBlinking(boolean enableBlinking) {
		this.enableBlinking = enableBlinking;
	}
}
