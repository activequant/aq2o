
package com.activequant.servicelayer.soap;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.MTOM;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.log4j.Logger;

import com.activequant.archive.TSContainer;
import com.activequant.domainmodel.Future;
import com.activequant.domainmodel.Instrument;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.backoffice.ClearedTrade;
import com.activequant.domainmodel.backoffice.ClearerAccount;
import com.activequant.domainmodel.backoffice.ClearerAccountSnap;
import com.activequant.domainmodel.backoffice.OrderFill;
import com.activequant.domainmodel.backoffice.PNL;
import com.activequant.domainmodel.backoffice.PandS;
import com.activequant.domainmodel.backoffice.PortfolioSnap;
import com.activequant.domainmodel.backoffice.SubClearerAccount;
import com.activequant.domainmodel.exceptions.DaoException;
import com.activequant.domainmodel.exceptions.InvalidDataException;
import com.activequant.dto.ClearedTradeDto;
import com.activequant.dto.ClearerAccountStatementDto;
import com.activequant.dto.DtoToDomainConv;
import com.activequant.dto.OrderFillDto;
import com.activequant.dto.PortfolioDto;
import com.activequant.dto.PositionDto;
import com.activequant.interfaces.archive.IArchiveFactory;
import com.activequant.interfaces.archive.IArchiveWriter;
import com.activequant.interfaces.dao.IAccountDao;
import com.activequant.interfaces.dao.IClearedTradeDao;
import com.activequant.interfaces.dao.IClearerAccountSnapDao;
import com.activequant.interfaces.dao.IDaoFactory;
import com.activequant.interfaces.dao.IInstrumentDao;
import com.activequant.interfaces.dao.IMarketDataInstrumentDao;
import com.activequant.interfaces.dao.IOrderFillDao;
import com.activequant.interfaces.dao.IPNLDao;
import com.activequant.interfaces.dao.IPandSDao;
import com.activequant.interfaces.dao.IPerformanceReportDao;
import com.activequant.interfaces.dao.IPortfolioDao;
import com.activequant.interfaces.dao.IPortfolioSnapDao;
import com.activequant.interfaces.dao.IReportDao;
import com.activequant.interfaces.dao.ISubClearerAccountDao;
import com.activequant.interfaces.dao.ITradeableInstrumentDao;
import com.activequant.utils.Date8Time6Parser;
import com.activequant.utils.worker.Worker;
import com.activequant.utils.worker.WorkerThread;

@WebService(endpointInterface = "com.activequant.servicelayer.soap.IMainService", serviceName = "MainService")
@BindingType(SOAPBinding.SOAP11HTTP_BINDING)
@MTOM(enabled = false)
// 

public class MainService implements IMainService {

	private IInstrumentDao idao;
	private IMarketDataInstrumentDao mdiDao;
	private ITradeableInstrumentDao tdiDao;
	private IArchiveFactory archFac;
	private IPerformanceReportDao perfDao;
	private IReportDao reportDao;

	private DtoToDomainConv converter = new DtoToDomainConv();
	private final IOrderFillDao ofDao;
	private final IClearedTradeDao ctDao;
	private final IAccountDao accDao;
	private final ISubClearerAccountDao subClrAccDao;
	private final IPortfolioSnapDao pSnapDao;
	private final IPortfolioDao pDao;
	private final IClearerAccountSnapDao caSnapDao;
	private Logger log = Logger.getLogger(MainService.class);
	private List<String> brokerIds = new ArrayList<String>();
	private List<String> accountIds = new ArrayList<String>();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	LinkedBlockingQueue<PositionDto> posDtoQueue = new LinkedBlockingQueue<PositionDto>();
	private WorkerThread<PositionDto> workerThread = null;

	LinkedBlockingQueue<PortfolioDto> posDtoQueue2 = new LinkedBlockingQueue<PortfolioDto>();
	private WorkerThread<PortfolioDto> workerThread2 = null;
	private IPandSDao pandsDao;
	private IPNLDao pnlDao;
	// only for development purposes.
	private Map<String, String> inMemoryKeyValMap = new HashMap<String, String>();

	public MainService(IDaoFactory daoFactory, IArchiveFactory factory) throws DaoException {
		this.idao = daoFactory.instrumentDao();
		this.archFac = factory;
		this.perfDao = daoFactory.perfDao();
		this.reportDao = daoFactory.reportDao();
		this.tdiDao = daoFactory.tradeableDao();
		this.mdiDao = daoFactory.mdiDao();

		ofDao = daoFactory.orderFillDao();
		ctDao = daoFactory.clearedTradeDao();
		accDao = daoFactory.accountDao();
		pSnapDao = daoFactory.portfolioSnapDao();
		pDao = daoFactory.portfolioDao();
		subClrAccDao = daoFactory.subClearerAccountDao();

		caSnapDao = daoFactory.clearerAccountSnapDao();

		pnlDao = daoFactory.pnlDao();
		pandsDao = daoFactory.pAndSDao();

		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

		// create the three test instruments
		initTestData();
	}

	public void initTestData() throws DaoException {
		Future f = new Future();
		f.setCurrency("EUR");
		f.setExchangeCode("EUR");
		f.setDescription("TEST FDAX 2012/12");
		f.setExpiry(20121217L);
		f.setFirstTradingDate(20120317L);
		f.setLastTradingDate(f.getExpiry());
		f.setSettlementDate(f.getLastTradingDate());
		f.setLotSize(1.0);
		f.setTickSize(0.5);
		f.setTickValue(12.5);
		f.setShortName("TEST GXZ12");
		f.setSymbolId("TEST DAX30");
		this.idao.delete(f);
		this.idao.create(f);
		
		
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
		return mdiDao.count();
	}

	public double[][] getTimeSeries(String seriesId, String column,
			TimeFrame timeFrame, String date8Time6Start, String date8Time6End)
			throws Exception {

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
					new TimeStamp(p.parse(date8Time6Start)),
					new TimeStamp(p.parse(date8Time6End)));

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

	public void saveTimeSeriesValue(String seriesKey, TimeFrame timeFrame,
			long nanoSeconds, String key, double value) throws IOException {
		IArchiveWriter w = archFac.getWriter(timeFrame);
		w.write(seriesKey, new TimeStamp(nanoSeconds), key, (Double) value);
		w.commit();
	}

	/**
	 * Stores something in-memory
	 */
	@Override
	public void storeKeyVal(String key, String val) {
		inMemoryKeyValMap.put(key, val);
	}

	/**
	 * Fetches from the in-memory key-value store.
	 */
	@Override
	public String fetchKeyVal(String key) {
		return inMemoryKeyValMap.get(key);
	}

	@Override
	public String[] mdiKeys() {
		try {
			return mdiDao.loadIDs();
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String[] tdiKeys() {
		try {
			return tdiDao.loadIDs();
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return null;
	
	}
	
	@Override
	public	String[][] getSampleMap(){
		
		Map<String, String> object = new HashMap<String, String>(); 
		object.put("A","b");
		String[][] ret = new String[object.keySet().size()][2];
		int n = object.keySet().size();
		Object[] keyArray = object.keySet().toArray();
		Object[] valueArray = object.values().toArray();
		for(int i =0;i<n;i++){
			ret[i][0] = (String)keyArray[i];
			ret[i][1] = (String)valueArray[i];			
		}
		return ret; 
	}
	
	@Override
	public int add(int a, int b){
		return a + b;  
	}
	
	@Override
	public String[] findMdiKeys(String regexPattern) {
		try {
			String[] ids = mdiDao.loadIDs();
			Pattern p = Pattern.compile(regexPattern);
			List<String> ret = new ArrayList<String>();
			for (String id : ids) {
				if (p.matcher(id).matches())
					ret.add(id);
			}
			return ret.toArray(new String[] {});

		} catch (DaoException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String[] findInstrumentKeys(String regexPattern) {
		try {
			String[] ids = idao.loadIDs();
			Pattern p = Pattern.compile(regexPattern);
			List<String> ret = new ArrayList<String>();
			for (String id : ids) {
				if (p.matcher(id).matches())
					ret.add(id);
			}
			return ret.toArray(new String[] {});

		} catch (DaoException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String[] findTdiKeys(String regexPattern) {
		try {
			String[] ids = tdiDao.loadIDs();
			Pattern p = Pattern.compile(regexPattern);
			List<String> ret = new ArrayList<String>();
			for (String id : ids) {
				if (p.matcher(id).matches())
					ret.add(id);
			}
			return ret.toArray(new String[] {});

		} catch (DaoException e) {
			e.printStackTrace();
		}
		return null;
	}

	public double testCall() {
		return (int) (100 * Math.random());
	}

	/**
	 * adds an order fill.
	 * 
	 */
	@Override
	public void addOrderFill(OrderFillDto dto) throws Exception {
		OrderFill of = converter.convert(dto);
		log.info("Adding order fill for " + dto.brokerAccountId);
		try {
			ofDao.update(of);

			// also checking if we have an existing or a new mdi.
			String tradId = dto.tradeableId;
			//
			//
			if (mdiDao.findByProvId("BBGT", tradId) == null) {
				/*BloombergMarketDataInstrument mdi = new BloombergMarketDataInstrument();
				mdi.setMdProvider("BBGT");
				mdi.setProviderSpecificId(tradId.toUpperCase());
				mdiDao.update(mdi);*/
			}

		} catch (Exception ex) {
			log.warn("Error while adding order fill: " + dto.toString(), ex);
		}
		log.debug("Adding done..");
	}

	/**
	 * adds an order fill.
	 * 
	 */
	@Override
	public void addClearedTrade(ClearedTradeDto dto) throws Exception {
		ClearedTrade ct = converter.convert(dto);
		log.info("Adding cleared trade. " + dto.clearingAccountId);
		try {
			ctDao.update(ct);
			System.out.println(ct.getId());
		} catch (Exception ex) {
			log.warn("Error while adding order fill: " + dto.toString(), ex);
		}
		log.debug("Adding done.");
	}

	@Override
	public void setSeriesValue(@WebParam(name = "portfolio") String portfolio,
			@WebParam(name = "seriesName") String seriesName,
			@WebParam(name = "timeStampInNanos") long timeStampInNanos,
			@WebParam(name = "value") double value) {
		String seriesId = "CUSTSERIES." + portfolio;
		IArchiveWriter iaw = archFac.getWriter(TimeFrame.RAW);
		iaw.write(seriesId, new TimeStamp(timeStampInNanos), seriesName, value);
	}

	@Override
	/**
	 * Method add positions. positions have to be added in their entirety.
	 * 
	 *  1) checks if we have such a clearer account, if not, create it. 
	 *  2) checks if we have such a sub clearer account, if not, create it. 
	 *  3) loads the up-to-dateportfolio snap for the handed in date. 
	 *  3.1.) if no snap exists, create a new snap
	 *  3.2.) if the loaded snap's timestamp in nanos does not match the incoming date, create a new snap with this particular snapdate. 
	 *  Reason: it could be that we loaded an old snap, from the day before, therefore we have to compare the new snap's date with the old snap's date.   
	 *   
	 */
	public void addPosition(PositionDto p) throws Exception {

		if (workerThread == null) {
			Worker<PositionDto> worker = new Worker<PositionDto>() {
				@Override
				public void process(PositionDto p) {
					// find the corresponding portfolio.
					String cAct = p.clearerAcctId;
					String subAct = p.subAcctId;
					SubClearerAccount sca;
					try {
						sca = checkClearerAccount(p.clearer, cAct, subAct);
						Long date8 = p.positionDate8;
						PortfolioSnap snap;
						try {
							snap = getSnapCache(sca.getId(), date8);
							log.info("Adding position to snap (" + snap.getId()
									+ "): " + p.toString());
							snap.addPosition(p);
						} catch (ParseException e) {
							e.printStackTrace();
						}
					} catch (DaoException e1) {
						e1.printStackTrace();
					}

				}

				@Override
				public void queueEmpty() {
					synchronized (portfolioSnapCache) {
						Collection<PortfolioSnap> snaps = portfolioSnapCache
								.values();
						for (PortfolioSnap snap : snaps) {
							log.info("Saving position snap ...");
							// ... and update it in the database.
							try {
								pSnapDao.delete(snap);
								pSnapDao.create(snap);

							} catch (DaoException e) {
								e.printStackTrace();
							}
						}
						// clear the snaps.
						portfolioSnapCache.clear();
					}
				}
			};
			workerThread = new WorkerThread<PositionDto>(posDtoQueue, worker);
			Thread t = new Thread(workerThread);
			t.start();
		}

		log.info("Adding position  ... ");
		posDtoQueue.add(p);

	}

	private PortfolioSnap getSnapCache(String ownerId, Long date8)
			throws ParseException {
		synchronized (portfolioSnapCache) {
			String key = ownerId + "." + date8;
			if (!portfolioSnapCache.containsKey(key)) {
				TimeStamp when = new TimeStamp(sdf.parse(date8.toString()));
				PortfolioSnap snap = new PortfolioSnap();
				snap.setOwnerObjectId(ownerId);
				snap.setTimeStampInNanoseconds(when.getNanoseconds());
				snap.setCreationTime(date8);

				portfolioSnapCache.put(key, snap);
			}
			return portfolioSnapCache.get(key);
		}
	}

	private Map<String, PortfolioSnap> portfolioSnapCache = new HashMap<String, PortfolioSnap>();

	private PortfolioSnap getSnapCache2(String ownerId, Long date8)
			throws ParseException {
		synchronized (portfolioSnapCache2) {
			String key = ownerId + "." + date8;
			if (!portfolioSnapCache2.containsKey(key)) {
				TimeStamp when = new TimeStamp(sdf.parse(date8.toString()));
				PortfolioSnap snap = new PortfolioSnap();
				snap.setOwnerObjectId(ownerId);
				snap = pSnapDao.loadSnapshot(snap.getNonUniqueID(), when);
				if (snap == null
						|| snap.getTimeStampInNanoseconds() != when
								.getNanoseconds()) {
					// ok, the most up-to-date snap is not our position's snap.
					snap = new PortfolioSnap();
					snap.setTimeStampInNanoseconds(when.getNanoseconds());
					// initialize it.
					snap.setOwnerObjectId(ownerId);
				}
				portfolioSnapCache2.put(key, snap);
			}
			return portfolioSnapCache2.get(key);
		}
	}

	private Map<String, PortfolioSnap> portfolioSnapCache2 = new HashMap<String, PortfolioSnap>();

	private SubClearerAccount checkClearerAccount(
			@WebParam(name = "legalEntity") String legalEntity,
			@WebParam(name = "accountId") String accountId,
			@WebParam(name = "subAccountId") String subAccountId)
			throws DaoException {
		//
		ClearerAccount ca = new ClearerAccount();
		ca.setLegalEntity(legalEntity);
		ca.setAccountId(accountId);
		String id = ca.getId();

		// check if we have such a clearer account.
		if (accDao.load(id) == null)
			accDao.create(ca);

		SubClearerAccount sca = new SubClearerAccount();
		sca.setClearerAcctObjId(id);
		sca.setSubAcctId(subAccountId);
		if (subClrAccDao.load(sca.getId()) == null)
			subClrAccDao.create(sca);
		return sca;
	}

	@Override
	public void addClearerAccountSnap(ClearerAccountStatementDto csdto)
			throws InvalidDataException, DaoException {
		//
		ClearerAccountSnap c = converter.convert(csdto);
		// create the snap.
		caSnapDao.update(c);
	}

	/**
	 * 
	 */
	public void addPnS(String tradeableId, long date8,
			String clearingAccountId, String currency, Double netAmount)
			throws DaoException {
		PandS p = new PandS();
		p.setTradeableId(tradeableId);
		p.setDate8(date8);
		p.setClearingAccount(clearingAccountId);
		p.setCurrency(currency);
		p.setNetAmount(netAmount);

		pandsDao.delete(p);
		pandsDao.create(p);
	}

	public void addPNL(String tradeableId, long date8,
			String clearingAccountId, String currency, Double grossPNL,
			Double netPNL) throws DaoException {
		PNL p = new PNL();
		p.setTradeableId(tradeableId);
		p.setDate8(date8);
		p.setClearingAccountId(clearingAccountId);
		p.setCurrency(currency);
		p.setGrossPNL(grossPNL);
		p.setNetPNL(netPNL);
		pnlDao.delete(p);
		pnlDao.create(p);
	}

	@Override
	public void storeInstrument(Instrument instrument) {
		// TODO Auto-generated method stub
	}

	@Override
	public void saveTimeSeriesValues(String seriesKey, TimeFrame timeFrame,
			String key, long[] nanoSeconds, double[] value) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public PositionDto[] getPositions(String clearingAccountId,
			String subClearingAccountId, String date8Time6) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int randomNumber() {
		return (int) (100 * Math.random());
	}

}
