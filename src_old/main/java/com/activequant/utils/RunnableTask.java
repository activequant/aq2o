package com.activequant.utils;

/**
 * 
 * This class can be used as a base class from which to extend a runnable from. It contains 
 * a runflag boolean property that can be used to nicely stop the runnable. <br>
 * Example:<br>
 * class ARunnableTask extends RunnableTask {<br>
 *   public void run(){<br>
 *   	while(runFlag){<br>
 *   		//do something <br>
 *   	}<br>
 *   }<br>
 * <br>
 * <b>History:</b><br>
 *  - [22.1.2008] Created (Ulrich Staudinger)<br>
 *
 *  @author Ulrich Staudinger
 */
public abstract class RunnableTask implements Runnable {
	
	private boolean runFlag = true;

	public boolean isRunFlag() {
		return runFlag;
	}

	public void setRunFlag(boolean runFlag) {
		this.runFlag = runFlag;
	}  

}
