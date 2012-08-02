// START SNIPPET: buy1sell1

import java.util.Timer;
import java.util.TimerTask;

import com.activequant.aqviz.BluntConsole;
import com.activequant.domainmodel.AlgoConfig;
import com.activequant.domainmodel.exceptions.IncompleteOrderInstructions;
import com.activequant.domainmodel.exceptions.UnsupportedOrderType;
import com.activequant.domainmodel.streaming.MarketDataSnapshot;
import com.activequant.domainmodel.streaming.StreamEvent;
import com.activequant.domainmodel.trade.order.MarketOrder;
import com.activequant.domainmodel.trade.order.OrderSide;
import com.activequant.interfaces.trading.ITradingSystem;
import com.activequant.trading.TradingSystemEnvironment;

/**
 * This trading system buys 1, waits ten seconds and then sells one EUR/USD lot.
 * When started, it waits for the next stream event. It then sends an order. 
 * 
 * @author GhostRider
 * 
 */
public class Buy1Sell1System implements ITradingSystem {
	// let's create a blunt console. 
	BluntConsole console = new BluntConsole("Buy one, sell one trading system");
	// opens a position. 
	boolean positionOpen = false;
	boolean done = false; 
	MarketDataSnapshot lastQuote; 
	/**
	 * All events arrive here. For the type of different events, consult the jdocs.  
	 */
	@Override
	public void process(StreamEvent se) { 		
		// if we are done already, leave and don't do anything. 
		if(done)return; 
		// ok, we are still here, so let's go. 
		console.addLog("********** Receiving an event.");
		// we are only interested in market data snapshots. 
		if(se instanceof MarketDataSnapshot){
			// we store the current price for reuse at some other time.
			lastQuote = (MarketDataSnapshot)se; 
			// check if we sent our order already. 
			if(!positionOpen){
				MarketOrder mo = new MarketOrder(); 
				mo.setOrderSide(OrderSide.BUY);
				mo.setQuantity(100000.0);
				mo.setTradInstId("EUR/USD");
				try {
					env.getExchange().prepareOrder(mo).submit();
					// no error handling in this simple example
					// no position fill monitoring in this simple example
					// plain vanilla. 
					positionOpen = true;
					// schedule a callback for in five seconds. 
				    new Timer().schedule(new TimerTask(){public void run() {closePosition();}}, 5000); 
				} catch (UnsupportedOrderType e) {
					e.printStackTrace();
				} catch (IncompleteOrderInstructions e) {
					e.printStackTrace();
				}
			}			
		}
	}

	/**
	 * example method how to close a position. 
	 */
	private void closePosition(){
		console.addLog("----------- Closing position");
		MarketOrder mo = new MarketOrder(); 
		mo.setOrderSide(OrderSide.SELL);
		mo.setQuantity(100000.0);
		mo.setTradInstId("EUR/USD");
		try {
			env.getExchange().prepareOrder(mo).submit();
			positionOpen = false;
			done = true; 
		} catch (UnsupportedOrderType e) {
			e.printStackTrace();
		} catch (IncompleteOrderInstructions e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	/////////////////////////////////////////////////////////////////
	/////////////////COMMON FUNCTIONALITY BELOW. KEEP THIS ALWAYS. // 
	/////////////////////////////////////////////////////////////////
	
	
	// pointer to the trading system environment. Store it. 
	private TradingSystemEnvironment env; 	
	@Override
	public void environment(TradingSystemEnvironment env) {
		this.env = env; 
	}

	@Override
	public void initialize() throws Exception {
		console.addLog("********** Initialized Buy1Sell1");
	}

	private boolean isRunning = false;

	@Override
	public void start() throws Exception {
		console.addLog("********** Starting Buy1Sell1");
		isRunning = true;
	}

	@Override
	public void stop() throws Exception {
		console.addLog("********** Stopping Buy1Sell1");
		isRunning = false;
	}

	@Override
	public boolean isRunning() {
		return isRunning;
	}
	
	@Override
	public AlgoConfig getAlgoConfig() {
		return null;
	}

}
//END SNIPPET: buy1sell1