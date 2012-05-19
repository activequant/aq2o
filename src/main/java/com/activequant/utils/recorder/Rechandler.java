package com.activequant.utils.recorder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.archive.IArchiveFactory;
import com.activequant.archive.IArchiveReader;
import com.activequant.archive.IArchiveWriter;
import com.activequant.archive.MultiValueTimeSeriesIterator;
import com.activequant.dao.IDaoFactory;
import com.activequant.domainmodel.OHLCV;
import com.activequant.domainmodel.PersistentEntity;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.Tuple;
import com.activequant.exceptions.TransportException;
import com.activequant.transport.ITransportFactory;
import com.activequant.utils.events.IEventListener;

/**
 * Rechandler recreates the OHLCV bars for a specific day, for a specific time
 * frame. This rechandler uses BID+ASK for midpoint calculation and generates
 * candles based on these midpoints.
 * 
 * @author GhostRider
 * 
 */
public class Rechandler {

	private IArchiveFactory archiveFactory;
	private ITransportFactory transFac;
	private Logger log = Logger.getLogger(Rechandler.class);
	final Timer t = new Timer(true);
	final long collectionPhase = 5000l;
	private TimeFrame tf;
	private final ConcurrentLinkedQueue<OHLCV> collectionList = new ConcurrentLinkedQueue<OHLCV>();
	private TimeStamp startTimeStamp, endTimeStamp;
	private final IArchiveWriter writer;
	private final IArchiveReader reader;


	public Rechandler(String springFile, String mdiFile, TimeFrame tf, int dayOffset, int days) throws Exception {

		this.tf = tf;
		Calendar cal = GregorianCalendar.getInstance();
		cal.add(Calendar.DATE, dayOffset);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		endTimeStamp = new TimeStamp(sdf.parse(sdf.format(cal.getTime())));
		endTimeStamp = new TimeStamp(endTimeStamp.getNanoseconds() - 1L);
		cal.add(Calendar.DATE, -days);
		startTimeStamp = new TimeStamp(sdf.parse(sdf.format(cal.getTime())));

		//
		System.out.println("Processing data from " + startTimeStamp.getDate() + " to " + endTimeStamp.getDate());

		ApplicationContext appContext = new ClassPathXmlApplicationContext(new String[] { springFile });
		System.out.println("Starting up and fetching idf");
		IDaoFactory idf = (IDaoFactory) appContext.getBean("ibatisDao");
		archiveFactory = (IArchiveFactory) appContext.getBean("archiveFactory");
		writer = archiveFactory.getWriter(tf);
		reader = archiveFactory.getReader(TimeFrame.RAW);

		process(mdiFile);

	}

	private void process(String mdiFile) throws Exception {

		List<String> instruments = new ArrayList<String>();

		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(mdiFile)));
		String l = br.readLine();
		while (l != null) {
			if (!l.startsWith("#") && !l.isEmpty()) {
				String symbol = l;
				int depth = 1;
				if (l.indexOf(";") != -1) {
					String[] s = l.split(";");
					symbol = s[0];
					depth = Integer.parseInt(s[1]);
				}
				instruments.add(symbol);
			}
			l = br.readLine();
		}
		// ...
		for (String s : instruments) {

			processMDIID(s);
		}
	}

	private void processMDIID(String s) throws Exception {
		// delete what we have for these timestamps.
		writer.delete(s, startTimeStamp, endTimeStamp);

		// ...
		MultiValueTimeSeriesIterator mvsi = reader.getMultiValueStream(s, startTimeStamp, endTimeStamp);
		long currentFrame = 0L;

		OHLCV temp = new OHLCV();
		temp.setResolutionInSeconds(tf.getMinutes() * 60);
		temp.setMdiId(s);

		while (mvsi.hasNext()) {
			Tuple<TimeStamp, Map<String, Double>> t = mvsi.next();

			long frame = t.getA().getNanoseconds() - t.getA().getNanoseconds()
					% (tf.getMinutes() * 60l * 1000l * 1000l * 1000l);
			if (frame != currentFrame) {
				if (temp.getOpen() != null) {
					collectionList.add(temp.clone());
				}
				temp.clear();
				currentFrame = frame;
			}

			Double bid = t.getB().get("BID");
			Double ask = t.getB().get("ASK");
			if (bid != null && ask != null) {
				double mp = (bid.doubleValue() + ask.doubleValue()) / 2.0;
				temp.update(t.getA(), mp);
			}
		}

		if (collectionList.size() > 0) {
			for (OHLCV o : collectionList)
				store((OHLCV) o);
			writer.commit();
		}
	}

	public void store(OHLCV mds) {
		if (mds == null)
			return;
		writer.write(mds.getMdiId(), mds.getTimeStamp(), "OPEN", mds.getOpen());
		writer.write(mds.getMdiId(), mds.getTimeStamp(), "HIGH", mds.getHigh());
		writer.write(mds.getMdiId(), mds.getTimeStamp(), "LOW", mds.getLow());
		writer.write(mds.getMdiId(), mds.getTimeStamp(), "CLOSE", mds.getClose());
		writer.write(mds.getMdiId(), mds.getTimeStamp(), "VOLUME", mds.getVolume());
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		new Rechandler(args[0], args[1], TimeFrame.valueOf(args[2]), Integer.parseInt(args[3]),
				Integer.parseInt(args[4]));
	}

}
