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
import com.activequant.dto.ClearerAccountStatementDto;
import com.activequant.dto.OrderFillDto;
import com.activequant.dto.PortfolioDto;
import com.activequant.dto.PositionDto;

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
			@WebParam(name = "PrimaryKey") String primaryKey);

	@WebMethod
	public int instrumentCount();

	@WebMethod
	public int mdiCount();

	@WebMethod
	public double[][] getTimeSeries(
			@WebParam(name = "seriesId") String seriesId,
			@WebParam(name = "seriesColumn") String column,
			@WebParam(name = "timeFrame") TimeFrame timeFrame,
			@WebParam(name = "startTimeStamp") String date8Start,
			@WebParam(name = "endTimeStamp") String date8End) throws Exception;

	@WebMethod
	public void saveTimeSeriesValue(String seriesKey, TimeFrame timeFrame,
			long nanoSeconds, String key, Object value) throws IOException;

	@WebMethod
	public void storeKeyVal(@WebParam(name = "key") String key,
			@WebParam(name = "value") String val);

	@WebMethod
	public String fetchKeyVal(@WebParam(name = "key") String key);

	@WebMethod
	public double testCall() throws Exception;

	@WebMethod
	public void addOrderFill(OrderFillDto dto) throws Exception;

	@WebMethod
	public void addClearedTrade(ClearedTradeDto dto) throws Exception;

	@WebMethod
	public void setSeriesValue(String portfolio, String seriesName,
			long timeStampInNanos, double value);

	@WebMethod
	public void addPosition(PositionDto dto) throws Exception;



	@WebMethod
	public void addClearerAccountSnap(ClearerAccountStatementDto csdto)
			throws InvalidDataException, DaoException;



	@WebMethod
	public void addPnS(String tradeableId, long date8,
			String clearingAccountId, String currency, Double netAmount) throws DaoException;

	@WebMethod
	public void addPNL(String tradeableId, long date8,
			String clearingAccountId, String currency, Double grossPNL,
			Double netPNL) throws DaoException;

}
