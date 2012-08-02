import com.activequant.aqviz.BluntConsole;
import com.activequant.domainmodel.AlgoConfig;
import com.activequant.domainmodel.streaming.StreamEvent;
import com.activequant.interfaces.trading.ITradingSystem;
import com.activequant.trading.TradingSystemEnvironment;

/**
 * This trading system does absolutely nothing. 
 * 
 * @author GhostRider
 *
 */
public class DoesNothingSystem implements ITradingSystem {

	BluntConsole console = new BluntConsole("DoesNothing TS");
	
	@Override
	public void process(StreamEvent se) {
		console.addLog("********** Receiving an event.");
	}

	@Override
	public void environment(TradingSystemEnvironment env) {
		console.addLog("********** TradSysEnv injected.");
	}

	@Override
	public void initialize() throws Exception {
		console.addLog("********** Initialized.");		
	}

	private boolean isRunning = false; 
	@Override
	public void start() throws Exception {
		console.addLog("********** Starting TestMain trading systems");
		isRunning = true; 
	}

	@Override
	public void stop() throws Exception {
		console.addLog("********** Stopping TestMain trading systems");
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
