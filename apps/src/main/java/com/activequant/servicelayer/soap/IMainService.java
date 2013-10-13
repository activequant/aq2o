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
import com.activequant.domainmodel.exceptions.DaoException;
import com.activequant.domainmodel.exceptions.InvalidDataException;
import com.activequant.dto.ClearedTradeDto;

@WebService
@MTOM(enabled = false)
@BindingType(SOAPBinding.SOAP11HTTP_BINDING)
public interface IMainService {

	@WebMethod
	public String[] instrumentKeys();

	@WebMethod
	public String[] findInstrumentKeys(
			@WebParam(name = "regexPattern") String regexPattern);

	@WebMethod
	public String[] mdiKeys();

	@WebMethod
	public String[] findMdiKeys(
			@WebParam(name = "regexPattern") String regexPattern);

	@WebMethod
	public String[] tdiKeys();

	@WebMethod
	public String[] findTdiKeys(
			@WebParam(name = "regexPattern") String regexPattern);

	@WebMethod
	public Instrument loadInstrument(
			@WebParam(name = "primaryKey") String primaryKey);

	@WebMethod
	public void storeInstrument(
			@WebParam(name = "instrument")	Instrument instrument);
	
	
	@WebMethod
	public int randomNumber();

	
	@WebMethod
	public int instrumentCount();

	@WebMethod	
	public int mdiCount();

	@WebMethod
	public int add(@WebParam(name = "a")int a, @WebParam(name = "b")int b);

	@WebMethod
	public String[][] getSampleMap();
	
	@WebMethod
	public double[][] getTimeSeries(
			@WebParam(name = "seriesId") String seriesId,
			@WebParam(name = "seriesColumn") String column,
			@WebParam(name = "timeFrame") TimeFrame timeFrame,
			@WebParam(name = "startTimeStamp") String date8Start,
			@WebParam(name = "endTimeStamp") String date8End) throws Exception;

	@WebMethod
	public void saveTimeSeriesValue(@WebParam(name = "seriesKey") String seriesKey, @WebParam(name = "timeFrame") TimeFrame timeFrame,
			@WebParam(name = "nanoseconds") long nanoSeconds, @WebParam(name = "field") String key, @WebParam(name = "value") double value) throws IOException;
	
	@WebMethod
	public void saveTimeSeriesValues(@WebParam(name = "seriesKey") String seriesKey, @WebParam(name = "timeFrame") TimeFrame timeFrame, @WebParam(name = "field") String key, 
			@WebParam(name = "nanoseconds") long[] nanoSeconds, @WebParam(name = "values") double[] value) throws IOException;
	

	@WebMethod
	public void storeKeyVal(@WebParam(name = "key") String key,
			@WebParam(name = "value") String val);

	@WebMethod
	public String fetchKeyVal(@WebParam(name = "key") String key);

	@WebMethod
	public double testCall() throws Exception;

	
	@WebMethod
	public void addPnS(@WebParam(name = "tradeableId") String tradeableId, @WebParam(name = "date8")  long date8,
			@WebParam(name = "clearingAccountId") String clearingAccountId, @WebParam(name = "currency") String currency, @WebParam(name = "netAmount") Double netAmount) throws DaoException;

	@WebMethod
	public void addPNL(@WebParam(name = "tradeableId") String tradeableId,@WebParam(name = "date8")  long date8,
			@WebParam(name = "clearingAccountId") String clearingAccountId, @WebParam(name = "currency") String currency, @WebParam(name = "grossPnL") Double grossPNL,
			@WebParam(name = "netPnL") Double netPNL) throws DaoException;

}
