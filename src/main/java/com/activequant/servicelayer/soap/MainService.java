package com.activequant.servicelayer.soap;

import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.MTOM;
import javax.xml.ws.soap.SOAPBinding;

import com.activequant.archive.IArchiveFactory;
import com.activequant.archive.TSContainer;
import com.activequant.dao.DaoException;
import com.activequant.dao.IDaoFactory;
import com.activequant.dao.IInstrumentDao;
import com.activequant.domainmodel.Instrument;
import com.activequant.domainmodel.PerformanceReport;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.utils.Date8Time6Parser;

@WebService(endpointInterface = "com.activequant.servicelayer.soap.IMainService", serviceName = "MainService")
@BindingType(SOAPBinding.SOAP11HTTP_BINDING)
@MTOM(enabled = false)
public class MainService implements IMainService {

	private IInstrumentDao idao;
	private IArchiveFactory archFac;

	public MainService(IDaoFactory daoFactory, IArchiveFactory factory) {
		this.idao = daoFactory.instrumentDao();
		this.archFac = factory;
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

	public double[][] getTimeSeries(String seriesId, String column, TimeFrame timeFrame, long date8Time6Start,
			long date8Time6End) throws Exception {

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
	
	public void createOrUpdatePerformanceReport(PerformanceReport report){
		
	}

}
