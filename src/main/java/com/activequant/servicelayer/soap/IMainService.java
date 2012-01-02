package com.activequant.servicelayer.soap;

import javax.jws.WebService;

import com.activequant.domainmodel.Instrument;

@WebService(endpointInterface = "com.activequant.servicelayer.soap.MainService")
public interface IMainService {
    public String[] instrumentKeys();

    public Instrument loadInstrument(String primaryKey);

    public int instrumentCount();

    public int mdiCount();
}
