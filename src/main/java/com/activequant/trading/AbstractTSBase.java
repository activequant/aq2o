package com.activequant.trading;

import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import com.activequant.aqviz.HardcoreReflectionsFactory;
import com.activequant.aqviz.interfaces.IVisualTable;
import com.activequant.dao.DaoException;
import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.PersistentEntity;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.TradeableInstrument;
import com.activequant.domainmodel.trade.event.OrderAcceptedEvent;
import com.activequant.domainmodel.trade.event.OrderCancelledEvent;
import com.activequant.domainmodel.trade.event.OrderFillEvent;
import com.activequant.domainmodel.trade.event.OrderRejectedEvent;
import com.activequant.domainmodel.trade.event.OrderReplacedEvent;
import com.activequant.domainmodel.trade.order.LimitOrder;
import com.activequant.domainmodel.trade.order.MarketOrder;
import com.activequant.domainmodel.trade.order.Order;
import com.activequant.domainmodel.trade.order.StopOrder;
import com.activequant.exceptions.TransportException;
import com.activequant.tools.streaming.InformationalEvent;
import com.activequant.tools.streaming.MarketDataSnapshot;
import com.activequant.tools.streaming.OrderStreamEvent;
import com.activequant.tools.streaming.PositionEvent;
import com.activequant.tools.streaming.StreamEvent;
import com.activequant.tools.streaming.Tick;
import com.activequant.tools.streaming.TimeStreamEvent;
import com.activequant.trading.datamodel.AuditLogTable;
import com.activequant.trading.datamodel.ExecutionsTable;
import com.activequant.trading.datamodel.InstrumentTable;
import com.activequant.trading.datamodel.OrderTable;
import com.activequant.trading.datamodel.PositionTable;
import com.activequant.trading.datamodel.QuoteTable;
import com.activequant.transport.ETransportType;
import com.activequant.utils.events.IEventListener;

public abstract class AbstractTSBase implements ITradingSystem {

	private final InstrumentTable instrumentTable = new InstrumentTable(this);
	private final QuoteTable quoteTable = new QuoteTable(this);
	private final PositionTable positionTable = new PositionTable(this);
	private final ExecutionsTable executionsTable = new ExecutionsTable(this);
	private final OrderTable orderTable = new OrderTable(this);
	private final AuditLogTable auditLogTable = new AuditLogTable(this);
	private final Logger log = Logger.getLogger(AbstractTSBase.class);
	protected TradingSystemEnvironment env;
    protected TimeStamp currentTime;

    protected SimpleDateFormat date8 = new SimpleDateFormat("yyyyMMdd");
    protected SimpleDateFormat date8time6 = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
    
	
	// visuals. 
	private boolean auditLog = true;
	private boolean vizLayer = true;
	private long currentMinute = 0L;
	private IVisualTable instViz, quoteViz, execViz, orderViz, posViz, auditViz;

	// an internal listener.
	protected IEventListener<PersistentEntity> internalListener = new IEventListener<PersistentEntity>() {
		@Override
		public void eventFired(PersistentEntity arg0) {
			process((StreamEvent) arg0);
		}
	};

	public void environment(TradingSystemEnvironment env) {
		this.env = env;
	}

	public InstrumentTable getInstrumentTable() {
		return instrumentTable;
	}

	public QuoteTable getQuoteTable() {
		return quoteTable;
	}

	public PositionTable getPositionTable() {
		return positionTable;
	}

	public ExecutionsTable getExecutionsTable() {
		return executionsTable;
	}

	public OrderTable getOrderTable() {
		return orderTable;
	}

	public AuditLogTable getAuditLogTable() {
		return auditLogTable;
	}

	protected void subscribe(MarketDataInstrument mdi) throws TransportException {
		log.info("Subscribing to " + mdi.getId());
		env.getTransportFactory().getReceiver(ETransportType.MARKET_DATA, mdi.getId()).getMsgRecEvent()
				.addEventListener(internalListener);
	}

	protected void subscribe(TradeableInstrument tdi) throws TransportException {
		log.info("Subscribing to " + tdi.getId());
		env.getTransportFactory().getReceiver(ETransportType.TRAD_DATA, tdi.getId()).getMsgRecEvent()
				.addEventListener(internalListener);
	}

	protected void unsubscribe(MarketDataInstrument mdi) throws TransportException {
		unsubscribeMdi(mdi.getId());
	}

	protected void unsubscribeMdi(String mdiId) throws TransportException {
		log.info("Unsubscribing from " + mdiId);
		env.getTransportFactory().getReceiver(ETransportType.MARKET_DATA, mdiId).getMsgRecEvent()
				.removeEventListener(internalListener);
	}
	
	protected void unsubscribe(TradeableInstrument tdi) throws TransportException {
		unsubscribeTdi(tdi.getId());
	}

	protected void unsubscribeTdi(String tdiId) throws TransportException {
		log.info("Unsubscribing from " + tdiId);
		env.getTransportFactory().getReceiver(ETransportType.TRAD_DATA, tdiId).getMsgRecEvent()
				.removeEventListener(internalListener);
	}
	
	
	public void addInstrument(String mdiId) throws DaoException, TransportException {
		addInstrument(mdiId, mdiId);
	}
	
	
	/**
	 * Adds an instrument to our environment. 
	 * 
	 * @param mdiId
	 * @throws DaoException
	 * @throws TransportException
	 */
	public void addInstrument(String mdiId, String tdiId) throws DaoException, TransportException {
		// set up the instrument table and subscribe to one instrument.
		MarketDataInstrument mdi = env.getDaoFactory().mdiDao().load(mdiId);
		TradeableInstrument tdi = env.getDaoFactory().tradeableDao().load(tdiId);
		
		// 
		if(mdi==null || tdi == null)
		{
			throw new DaoException("Could not load " + mdiId + " or " + tdiId);
		}

		
		// add the instrument to our list of instruments. 
		getInstrumentTable().addInstrument(mdiId, tdiId, "", 0L, 1.0, 1.0, 80000.0, 220000.0);
		getQuoteTable().addInstrument(mdiId);
				
		getPositionTable().addInstrument(tdiId);
		
		// signal updates to our tables. 
		getInstrumentTable().signalUpdate();
		getQuoteTable().signalUpdate();
		getPositionTable().signalUpdate();
		
		
		
		// subscribe to market data and to instrument data.
		subscribe(mdi);
		subscribe(tdi);
	}
	
	/**
	 * removes an instrument from our environment. 
	 * 
	 * @param mdiId
	 * @throws TransportException
	 */
	public void removeInstrument(String mdiId, String tdiId) throws TransportException{
		getInstrumentTable().deleteInstrument(mdiId);
		getQuoteTable().deleteInstrument(mdiId);
		getPositionTable().deleteInstrument(mdiId);		
		// unsubscribe from market data and to instrument data.
		unsubscribeMdi(mdiId);
		unsubscribeTdi(tdiId);
	}
	
	

	public void initialize() throws Exception {

		
		// subscribe to time data.
		env.getTransportFactory().getReceiver(ETransportType.TIME.toString()).getMsgRecEvent()
				.addEventListener(internalListener);
		
		if (vizLayer) {
			HardcoreReflectionsFactory hrf = new HardcoreReflectionsFactory();
			// plug in two visual tables for the underlying table models.
			instViz = hrf.getInstrumentTableViz("Instruments", getInstrumentTable(), env.getExchange());
			quoteViz = hrf.getQuoteTableViz("Quotes", getQuoteTable(), env.getExchange());
			execViz = hrf.getVisualTableViz("Executions", getExecutionsTable());
			orderViz = hrf.getOrderTableViz("Working orders", getOrderTable(), env.getExchange());
			posViz = hrf.getVisualTableViz("Positions", getPositionTable());
			auditViz = hrf.getVisualTableViz("Audit", getAuditLogTable());
		}

		// refresh the table models.
		getInstrumentTable().signalUpdate();
		getQuoteTable().signalUpdate();
		getExecutionsTable().signalUpdate();
		getOrderTable().signalUpdate();
		getPositionTable().signalUpdate();
		getAuditLogTable().signalUpdate();
		//
	}

	public void setWindowTitles(String additionalTitle) {
		if (vizLayer) {
			instViz.setTitle("Instruments - " + additionalTitle);
			quoteViz.setTitle("Quotes - " + additionalTitle);
			execViz.setTitle("Executions - " + additionalTitle);
			orderViz.setTitle("Working orders - " + additionalTitle);
			posViz.setTitle("Positions - " + additionalTitle);
			auditViz.setTitle("Audit - " + additionalTitle);
		}
	}

	/**
	 * Called if there is a new market stream data event. 
	 */
	@Override
	public void process(StreamEvent se) {
		if (se instanceof Tick) {
			process((Tick) se);
		} else if (se instanceof MarketDataSnapshot) {
			process((MarketDataSnapshot) se);
		} else if (se instanceof PositionEvent) {
			process((PositionEvent) se);
			// log it to audit, too.
			auditLog(se.getTimeStamp(), se.toString());
		} else if (se instanceof OrderStreamEvent) {
			process((OrderStreamEvent) se);
			// log it to audit, too.
			auditLog(se.getTimeStamp(), ((OrderStreamEvent) se).getOe().toString());
		}
		else if(se instanceof InformationalEvent){
			auditLog(se.getTimeStamp(), ((InformationalEvent) se).getText());
		}
		if (se instanceof TimeStreamEvent) {
			process((TimeStreamEvent) se);
		}
	}

	private void auditLog(TimeStamp ts, String text) {
		if (auditLog) {
			log.info("AUDIT [" + ts + "] " + text);
			getAuditLogTable().addAudit(ts, text);
			getAuditLogTable().signalUpdate();
		}
	}

	public void process(PositionEvent pe) {
		getPositionTable().setPosition(pe.getTradInstId(), pe.getPrice(), pe.getQuantity());
		getPositionTable().signalUpdate();
	}

	public void process(OrderStreamEvent ose) {
		Order refOrder = ose.getOe().getRefOrder();
		if (ose.getOe() instanceof OrderFillEvent) {
			// add an execution.
			OrderFillEvent ofe = (OrderFillEvent) ose.getOe();
			getExecutionsTable().addExecution(ofe.getRefOrderId(), ofe.getOptionalInstId(), ofe.getSide(),
					ofe.getFillPrice(), ofe.getFillAmount());
			//
			if (ofe.getLeftQuantity() == 0) {
				getOrderTable().delOrder(ofe.getRefOrderId());
			} else {
				addOrSetOrderTable(refOrder);
			}
			getExecutionsTable().signalUpdate();
			getOrderTable().signalUpdate();
		} else if ((ose.getOe() instanceof OrderAcceptedEvent) || (ose.getOe() instanceof OrderReplacedEvent)) {
			// add it to our orders table.
			addOrSetOrderTable(refOrder);
		} else if (ose.getOe() instanceof OrderRejectedEvent) {
			getOrderTable().delOrder(ose.getOe().getRefOrderId());
			getOrderTable().signalUpdate();
		} else if (ose.getOe() instanceof OrderCancelledEvent) {
			getOrderTable().delOrder(ose.getOe().getRefOrderId());
			getOrderTable().signalUpdate();
		}
	}

	/**
	 * 
	 * Adds or sets the information in an order table, depending on the order
	 * type.
	 * 
	 * @param refOrder
	 */
	private void addOrSetOrderTable(Order refOrder) {
		if (refOrder instanceof MarketOrder) {
			MarketOrder mo = (MarketOrder) refOrder;
			getOrderTable()
					.addOrder(refOrder.getOrderId(), mo.getTradInstId(), "MKT", mo.getOrderSide().toString(),
							Double.NaN, mo.getQuantity(), mo.getQuantity() - mo.getOpenQuantity());
		}
		if (refOrder instanceof LimitOrder) {
			LimitOrder lo = (LimitOrder) refOrder;
			getOrderTable().addOrder(refOrder.getOrderId(), lo.getTradInstId(), "LMT",
					lo.getOrderSide().toString(), lo.getLimitPrice(), lo.getQuantity(),
					lo.getQuantity() - lo.getOpenQuantity());
		}
		if (refOrder instanceof StopOrder) {
			StopOrder so = (StopOrder) refOrder;
			getOrderTable().addOrder(refOrder.getOrderId(), so.getTradInstId(), "STP",
					so.getOrderSide().toString(), so.getStopPrice(), so.getQuantity(),
					so.getQuantity() - so.getOpenQuantity());
		}
		getOrderTable().signalUpdate();
	}

	public void process(MarketDataSnapshot mds) {
		// update the current mkt quotes table.
		String mdiId = mds.getMdiId();
		if (getInstrumentTable().containsInstrumentId(mdiId)) {
			int row = getInstrumentTable().getPosition(mdiId);
			
			// update the quote table.
			Double bid = null, ask = null; 
			if (mds.getAskPrices() != null && mds.getAskPrices().length > 0) {
				ask = mds.getAskPrices()[0];
				getQuoteTable().setValueAt(mds.getAskPrices()[0], row, QuoteTable.Columns.ASK.colIdx());
				getQuoteTable().setValueAt(mds.getAskSizes()[0], row, QuoteTable.Columns.ASKSIZE.colIdx());
			}
			else{
				getQuoteTable().setValueAt(null, row, QuoteTable.Columns.ASK.colIdx());
				getQuoteTable().setValueAt(null, row, QuoteTable.Columns.ASKSIZE.colIdx());
			}
			if (mds.getBidPrices() != null && mds.getBidPrices().length > 0) {
				bid = mds.getBidPrices()[0];				
				getQuoteTable().setValueAt(mds.getBidPrices()[0], row, QuoteTable.Columns.BID.colIdx());
				getQuoteTable().setValueAt(mds.getBidSizes()[0], row, QuoteTable.Columns.BIDSIZE.colIdx());
			}
			else
			{
				getQuoteTable().setValueAt(mds.getBidPrices()[0], row, QuoteTable.Columns.BID.colIdx());
				getQuoteTable().setValueAt(mds.getBidSizes()[0], row, QuoteTable.Columns.BIDSIZE.colIdx());
			}		
			// signaling that this row has changed.
			getQuoteTable().getRowUpdateEvent().fire(row);
			getQuoteTable().signalUpdate();

			//
		} else {
			log.info("Dropping data for unknown instrument. ");
		}
	}

	public void process(Tick mds) {
		// update the current mkt quotes table.
		String mdiId = mds.getMdiId();
		if (getInstrumentTable().containsInstrumentId(mdiId)) {
			int row = getInstrumentTable().getPosition(mdiId);
			// update the quote table.

			getQuoteTable().setValueAt(mds.getPrice(), row, QuoteTable.Columns.TRADE.colIdx());
			getQuoteTable().setValueAt(mds.getQuantity(), row, QuoteTable.Columns.TRADESIZE.colIdx());

			getQuoteTable().getRowUpdateEvent().fire(row);
			getQuoteTable().signalUpdate();
			//
		} else {
			log.info("Dropping data for unknown instrument. ");
		}
	}

	public void process(TimeStreamEvent se) {
		if (se instanceof TimeStreamEvent) {
			currentTime = se.getTimeStamp();
			long minute = currentTime.getNanoseconds() / (1000l * 1000l * 1000l * 60l);
			if (minute != currentMinute) {
				currentMinute = minute;
			}
			setWindowTitles(date8time6.format(currentTime.getDate()));
			// log.info("It is now " + ((TimeStreamEvent)
			// se).getTimeStamp().getDate());
		}
	}

	public boolean isVizLayer() {
		return vizLayer;
	}

	public void setVizLayer(boolean vizLayer) {
		this.vizLayer = vizLayer;
	}

	public long getCurrentMinute() {
		return currentMinute;
	}

	public void setCurrentMinute(long currentMinute) {
		this.currentMinute = currentMinute;
	}

	public IVisualTable getInstViz() {
		return instViz;
	}

	public void setInstViz(IVisualTable instViz) {
		this.instViz = instViz;
	}

	public IVisualTable getQuoteViz() {
		return quoteViz;
	}

	public void setQuoteViz(IVisualTable quoteViz) {
		this.quoteViz = quoteViz;
	}

	public IVisualTable getExecViz() {
		return execViz;
	}

	public void setExecViz(IVisualTable execViz) {
		this.execViz = execViz;
	}

	public IVisualTable getOrderViz() {
		return orderViz;
	}

	public void setOrderViz(IVisualTable orderViz) {
		this.orderViz = orderViz;
	}

	public IVisualTable getPosViz() {
		return posViz;
	}

	public void setPosViz(IVisualTable posViz) {
		this.posViz = posViz;
	}

	public IVisualTable getAuditViz() {
		return auditViz;
	}

	public void setAuditViz(IVisualTable auditViz) {
		this.auditViz = auditViz;
	}

	public Logger getLog() {
		return log;
	}	
	
}
