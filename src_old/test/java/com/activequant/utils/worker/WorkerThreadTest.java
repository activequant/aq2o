package com.activequant.utils.worker;

import java.util.concurrent.LinkedBlockingQueue;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class WorkerThreadTest extends TestCase {
	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(WorkerThreadTest.class);
	}

	public void testThreading() throws InterruptedException {
		LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
		for (int i = 0; i < 100; i++) queue.add("SOMETEXT");
		for (int i = 0; i < 1; i++) queue.add("TERMINATE");
		AnonymousWorker worker = new AnonymousWorker();
		new Thread(new WorkerThread<String>(queue, worker)).start();
		// give it some time ... 
		Thread.sleep(100);
		assertEquals(100, worker.counter);
	}

	class AnonymousWorker extends Worker<String> {
		int counter = 0;

		public void process(String event) {
			if (event.equals("TERMINATE")) {
				setRunFlag(false);
				return;
			}
			counter++;
		}
	}
}
