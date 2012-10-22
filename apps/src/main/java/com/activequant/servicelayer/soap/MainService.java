package com.activequant.servicelayer.soap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.MTOM;
import javax.xml.ws.soap.SOAPBinding;

import com.activequant.archive.TSContainer;
import com.activequant.domainmodel.Instrument;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.exceptions.DaoException;
import com.activequant.interfaces.archive.IArchiveFactory;
import com.activequant.interfaces.archive.IArchiveWriter;
import com.activequant.interfaces.dao.IDaoFactory;
import com.activequant.interfaces.dao.IInstrumentDao;
import com.activequant.interfaces.dao.IPerformanceReportDao;
import com.activequant.interfaces.dao.IReportDao;
import com.activequant.utils.Date8Time6Parser;

@WebService(endpointInterface = "com.activequant.servicelayer.soap.IMainService", serviceName = "MainService")
@BindingType(SOAPBinding.SOAP11HTTP_BINDING)
@MTOM(enabled = false)
public class MainService implements IMainService {

	private IInstrumentDao idao;
	private IArchiveFactory archFac;
	private IPerformanceReportDao perfDao;
	private IReportDao reportDao;
	
	// only for development purposes. 
	private Map<String, String> inMemoryKeyValMap = new HashMap<String, String>();

	public MainService(IDaoFactory daoFactory, IArchiveFactory factory) {
		this.idao = daoFactory.instrumentDao();
		this.archFac = factory;
		this.perfDao = daoFactory.perfDao();
		this.reportDao = daoFactory.reportDao();
	}

	public String[] instrumentKeys() {
		try {
			return idao.loadIDs();
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Instrument loadInstrument(String primaryKey) {
		try {
			return idao.load(primaryKey);
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int instrumentCount() {
		return idao.count();
	}

	public int mdiCount() {
		return (int) (100 * Math.random());
	}

	public double[][] getTimeSeries(String seriesId, String column, TimeFrame timeFrame, String date8Time6Start,
			String date8Time6End) throws Exception {

		// format date...
		// expected output = "yyyyMMddHHmmss.SSS"
		// (1) yyyyMMdd
		if (date8Time6Start.length() == "yyyyMMdd".length()) {
			date8Time6Start += "000000.000";
		}
		if (date8Time6End.length() == "yyyyMMdd".length()) {
			date8Time6End += "235959.999";
		}
		// (2) yyyyMMddHHmm
		if (date8Time6Start.length() == "yyyyMMddHHmm".length()) {
			date8Time6Start += "00.000";
		}
		if (date8Time6End.length() == "yyyyMMddHHmm".length()) {
			date8Time6End += "59.999";
		}
		// (3) yyyyMMddHHmmss
		if (date8Time6Start.length() == "yyyyMMddHHmmss".length()) {
			date8Time6Start += ".000";
		}
		if (date8Time6End.length() == "yyyyMMddHHmmss".length()) {
			date8Time6End += ".999";
		}
		// (4) yyyyMMddHHmmss.S
		if (date8Time6Start.length() == "yyyyMMddHHmmss.S".length()) {
			date8Time6Start += "00";
		}
		if (date8Time6End.length() == "yyyyMMddHHmmss.S".length()) {
			date8Time6End += "99";
		}
		// (5) yyyyMMddHHmmss.SS
		if (date8Time6Start.length() == "yyyyMMddHHmmss.SS".length()) {
			date8Time6Start += "0";
		}
		if (date8Time6End.length() == "yyyyMMddHHmmss.SS".length()) {
			date8Time6End += "9";
		}
		// (5) yyyyMMddHHmmss.SSS
		if (date8Time6Start.length() == "yyyyMMddHHmmss.SSS".length()) {
		}
		if (date8Time6End.length() == "yyyyMMddHHmmss.SSS".length()) {
		}

		Date8Time6Parser p = new Date8Time6Parser();

		TSContainer ts;
		try {
			ts = archFac.getReader(timeFrame).getTimeSeries(seriesId, column,
					new TimeStamp(p.parse("" + date8Time6Start)), new TimeStamp(p.parse("" + date8Time6End)));

			// //////////// synthetic, custom fields.
			double[][] ret = new double[ts.timeStamps.length][2];

			if (ts.timeStamps.length > 0) {
				for (int k = 0; k < ts.timeStamps.length; k++) {
					TimeStamp timeStamp = ts.timeStamps[k];
					Double value = ts.values[k];
					ret[k][0] = timeStamp.getNanoseconds();
					ret[k][1] = value;
				}
			}
			return ret;
		} catch (Exception e) {
			throw e;
		}
	}

	public void saveTimeSeriesValue(String seriesKey, TimeFrame timeFrame, long nanoSeconds, String key, Object value)
			throws IOException {
		IArchiveWriter w = archFac.getWriter(timeFrame);
		w.write(seriesKey, new TimeStamp(nanoSeconds), key, (Double) value);
		w.commit();
	}

	@Override
	public void storeKeyVal(String key, String val) {
		inMemoryKeyValMap.put(key, val);
	}

	@Override
	public String fetchKeyVal(String key) {
		return inMemoryKeyValMap.get(key);
	}

	

}
