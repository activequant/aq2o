package com.activequant.backtesting.reporting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.activequant.domainmodel.OHLCV;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.trade.event.OrderFillEvent;
import com.activequant.tools.streaming.StreamEventIterator;
import com.activequant.tools.streaming.TimeStreamEvent;

/**
 * Execting header and then date, open, high, low, close
 * 
 * @author GhostRider
 * 
 */
public class TransactionFileStreamIterator extends StreamEventIterator<TimeStreamEvent> {

	private BufferedReader br;
	private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
	private String line;

	public TransactionFileStreamIterator(String fileName) throws Exception {
		File f = new File(fileName);
		br = new BufferedReader(new FileReader(f));
		// skip header 
		line = br.readLine();
		line = br.readLine();
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	@Override
	public boolean hasNext() {
		if (line != null)
			return true;
		return false;
	}

	/**
	 * format documentation see
	 * http://developers.activequant.org/projects/pecora/wiki
	 */
	@Override
	public OrderFillEvent next() {
		try {
			String[] p = line.split(",");
			String id = p[0];
			String inst = p[1];			
			TimeStamp tsUnixGmt = new TimeStamp(new Date(Long.parseLong(p[3]) * 1000));
			String dir = p[4];
			Double price = Double.parseDouble(p[6]);
			Double quantity = Double.parseDouble(p[5]);

			OrderFillEvent ofe = new OrderFillEvent();
			ofe.setRefOrderId(id);
			ofe.setOptionalInstId(inst);
			ofe.setFillPrice(price);
			ofe.setFillAmount(quantity);
			ofe.setSide(dir);
			ofe.setRefOrderId("-");
			ofe.setCreationTimeStamp(tsUnixGmt);

			// shift it, so that it looks as if it emitted at the end of a
			// candle.

			line = br.readLine();
			return ofe;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

}
