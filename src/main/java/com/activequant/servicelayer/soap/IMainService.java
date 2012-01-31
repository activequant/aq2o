package com.activequant.servicelayer.soap;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.activequant.domainmodel.Instrument;

@WebService
public interface IMainService {
    public String[] instrumentKeys();

    @WebMethod
    public Instrument loadInstrument(String primaryKey);

    @WebMethod
    public int instrumentCount();

    @WebMethod
    public int mdiCount();
    
    
    
    
}
