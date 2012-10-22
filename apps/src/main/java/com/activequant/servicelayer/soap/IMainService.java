package com.activequant.servicelayer.soap;

import java.io.IOException;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.MTOM;
import javax.xml.ws.soap.SOAPBinding;

import com.activequant.domainmodel.Instrument;
import com.activequant.domainmodel.TimeFrame;

@WebService
@MTOM(enabled = false)
@BindingType(SOAPBinding.SOAP11HTTP_BINDING)
public interface IMainService {
	
	@WebMethod
	public String[] instrumentKeys();

	@WebMethod
	public Instrument loadInstrument(@WebParam(name="PrimaryKey") String primaryKey);

	@WebMethod
	public int instrumentCount();

	@WebMethod
	public int mdiCount();

	@WebMethod
	public double[][] getTimeSeries(@WebParam(name="seriesId") String seriesId, @WebParam(name="seriesColumn") String column,
			@WebParam(name="timeFrame") TimeFrame timeFrame, @WebParam(name="startTimeStamp") String date8Start, @WebParam(name="endTimeStamp") String date8End)
			throws Exception;

	@WebMethod
	public void saveTimeSeriesValue(String seriesKey, TimeFrame timeFrame,
			long nanoSeconds, String key, Object value) throws IOException;

	@WebMethod
	public void storeKeyVal(@WebParam(name="key") String key, @WebParam(name="value") String val);
	
	@WebMethod
	public String fetchKeyVal(@WebParam(name="key") String key);

}
