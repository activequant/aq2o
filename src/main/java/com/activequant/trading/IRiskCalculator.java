package com.activequant.trading;

import com.activequant.transport.ITransportFactory;

public interface IRiskCalculator {
	/**
	 * where to send risk events to. 
	 * @param transportFactory
	 */
	void setTransportFactory(ITransportFactory transportFactory);
	
	/**
	 * to indicate which position row changed. 
	 * @param rowIndex
	 */
	void execution(String tradInstId, double price, double quantity);
	
	/**
	 * to indicate with price row changed. 
	 * @param rowIndex
	 */
	void pricesUpdated(int rowIndex);
}
