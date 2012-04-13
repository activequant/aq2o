package com.activequant.servicelayer.soap;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.MTOM;
import javax.xml.ws.soap.SOAPBinding;

import com.activequant.domainmodel.Instrument;
import com.activequant.domainmodel.TimeFrame;

@WebService
@MTOM(enabled=false)
@BindingType(SOAPBinding.SOAP11HTTP_BINDING)
public interface IMainService {
    public String[] instrumentKeys();

    @WebMethod
    public Instrument loadInstrument(String primaryKey);

    @WebMethod
    public int instrumentCount();

    @WebMethod
    public int mdiCount();
    
    @WebMethod
    public double[][] getTimeSeries(String seriesId, String column, TimeFrame timeFrame, long date8Start, long date8End) throws Exception ;
    
    
    
}
