package com.activequant.dao;

import com.activequant.domainmodel.Instrument;

public interface IInstrumentDao extends IEntityDao<Instrument> {

	public String[] getNonExpiredInstruments(long referenceDate8);

    public String[] searchById(String searchString, int resultAmount);

}
