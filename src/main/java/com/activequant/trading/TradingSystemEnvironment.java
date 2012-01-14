package com.activequant.trading;

import com.activequant.archive.IArchiveFactory;
import com.activequant.dao.IDaoFactory;
import com.activequant.trading.virtual.IExchange;
import com.activequant.transport.ITransportFactory;

public class TradingSystemEnvironment {

	private IExchange exchange; 
	private ITransportFactory transportFactory; 
	private IArchiveFactory archiveFactory; 
	private IDaoFactory daoFactory; 
	
}
