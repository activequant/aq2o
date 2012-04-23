package com.activequant.utils.worker;

public abstract class Worker<T> {
    private boolean runFlag = true;

    public abstract void process(T t);

    /**
     * called when the queue is empty. May be implemented by 
     */
    public void queueEmpty() {
    	return;
    }
    
    public boolean runFlag() {
        return runFlag;
    }

    public void setRunFlag(boolean b) {
        runFlag = b;
    }
}