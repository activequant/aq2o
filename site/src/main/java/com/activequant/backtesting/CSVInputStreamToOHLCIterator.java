package com.activequant.backtesting;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import com.activequant.archive.MultiValueTimeSeriesIterator;
import com.activequant.domainmodel.OHLCV;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.Tuple;
import com.activequant.domainmodel.streaming.StreamEventIterator;
import com.activequant.domainmodel.streaming.TimeStreamEvent;
import com.activequant.interfaces.utils.IEventListener;
import com.activequant.utils.CsvMapReader;

/**
 * Execting header and then date, open, high, low, close
 * 
 * @author GhostRider
 * 
 */
public class CSVInputStreamToOHLCIterator extends StreamEventIterator<TimeStreamEvent> {

	private String mdiId, tdiId;
	private double open, high, low, close;
	private int resInSeconds;
	private long offset;
	private String OPEN = "OPEN", HIGH = "HIGH", LOW = "LOW", CLOSE = "CLOSE", VOL = "VOLUME";
	private MultiValueTimeSeriesIterator streamIterator;
	private InputStream in;
	private BufferedReader br;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private String line;
	private boolean shiftTime = true; 

	public CSVInputStreamToOHLCIterator(String mdiId, TimeFrame timeFrame, InputStream in) throws Exception {
		this.mdiId = mdiId;
		this.tdiId = mdiId;
		resInSeconds = timeFrame.getMinutes() * 60;
		offset = resInSeconds * 1000l * 1000l * 1000l;
		this.in = in;
		br = new BufferedReader(new InputStreamReader(in));
		// header.
		String l = br.readLine();

		line = br.readLine();
	}

	@Override
	public boolean hasNext() {
		if (line != null)
			return true;
		return false;
	}

	@Override
	public OHLCV next() {
		String[] parts = line.split(",");
		OHLCV o = new OHLCV();
		o.setMdiId(mdiId);
		o.setOpen(Double.parseDouble(parts[1]));
		o.setHigh(Double.parseDouble(parts[2]));
		o.setLow(Double.parseDouble(parts[3]));
		o.setClose(Double.parseDouble(parts[4]));
		o.setVolume(0.0);
		o.setResolutionInSeconds(resInSeconds);
		// shift it, so that it looks as if it emitted at the end of a candle.
		try {
			if(isShiftTime())
				o.setTimeStamp(new TimeStamp(sdf.parse(parts[0]).getTime() * 1000 * 1000 + offset));
			else
				o.setTimeStamp(new TimeStamp(sdf.parse(parts[0]).getTime() * 1000 * 1000 ));
			line = br.readLine();
		} catch (Exception e) {	
			throw new RuntimeException(e);
		}
		return o;

	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public boolean isShiftTime() {
		return shiftTime;
	}

	public void setShiftTime(boolean shiftTime) {
		this.shiftTime = shiftTime;
	}
}
