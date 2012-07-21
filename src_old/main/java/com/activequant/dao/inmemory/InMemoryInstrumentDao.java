package com.activequant.dao.inmemory;

import com.activequant.domainmodel.Instrument;
import com.activequant.domainmodel.exceptions.DaoException;
import com.activequant.interfaces.dao.IInstrumentDao;

public class InMemoryInstrumentDao extends InMemoryDao<Instrument> implements IInstrumentDao {

	public void create(Instrument t) throws DaoException {
		// TODO Auto-generated method stub
		
	}

	public void delete(Instrument t) throws DaoException {
		// TODO Auto-generated method stub
		
	}

	public void update(Instrument t) throws DaoException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String[] getNonExpiredInstruments(long referenceDate8) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] searchById(String searchString, int resultAmount) {
		// TODO Auto-generated method stub
		return null;
	}


}
