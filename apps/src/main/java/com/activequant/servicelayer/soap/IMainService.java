package com.activequant.servicelayer.soap;

import java.io.IOException;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.MTOM;
import javax.xml.ws.soap.SOAPBinding;

import com.activequant.domainmodel.Instrument;
import com.activequant.domainmodel.PerformanceReport;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.exceptions.DaoException;

@WebService
@MTOM(enabled = false)
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
	public double[][] getTimeSeries(String seriesId, String column,
			TimeFrame timeFrame, String date8Start, String date8End)
			throws Exception;

	@WebMethod
	public void saveTimeSeriesValue(String seriesKey, TimeFrame timeFrame,
			long nanoSeconds, String key, Object value) throws IOException;

	@WebMethod
	public void storeKeyVal(String key, String val);
	
	@WebMethod
	public String fetchKeyVal(String key);

}
