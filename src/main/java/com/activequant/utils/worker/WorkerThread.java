package com.activequant.utils.worker;

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

public class WorkerThread<T> implements Runnable {
    private LinkedBlockingQueue<T> queue;
    private Worker<T> worker;
    private Logger log = Logger.getLogger(WorkerThread.class);

    public WorkerThread(LinkedBlockingQueue<T> queue, Worker<T> worker) {
        this.queue = queue;
        this.worker = worker;
    }

    public void run() {
        try {
            while (worker.runFlag()) {
                T t = queue.take();
                worker.process(t);
            }
        } catch (Exception ex) {
            log.error("Error while processing work load. ", ex);
        }
    }
}