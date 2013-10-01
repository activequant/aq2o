package com.activequant.interfaces.trading;

import com.activequant.interfaces.dao.IDaoFactory;
import com.activequant.interfaces.transport.ITransportFactory;

public interface ITradingSystemEnvironment {

	IExchange getExchange();

	ITransportFactory getTransportFactory();

	IDaoFactory getDaoFactory();

}
