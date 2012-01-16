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

	public IExchange getExchange() {
		return exchange;
	}

	public void setExchange(IExchange exchange) {
		this.exchange = exchange;
	}

	public ITransportFactory getTransportFactory() {
		return transportFactory;
	}

	public void setTransportFactory(ITransportFactory transportFactory) {
		this.transportFactory = transportFactory;
	}

	public IArchiveFactory getArchiveFactory() {
		return archiveFactory;
	}

	public void setArchiveFactory(IArchiveFactory archiveFactory) {
		this.archiveFactory = archiveFactory;
	}

	public IDaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(IDaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

}
