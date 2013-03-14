package com.activequant.utils.recorder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.archive.MultiValueTimeSeriesIterator;
import com.activequant.domainmodel.OHLCV;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.Tuple;
import com.activequant.interfaces.archive.IArchiveFactory;
import com.activequant.interfaces.archive.IArchiveReader;
import com.activequant.interfaces.archive.IArchiveWriter;

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
	
	final Timer t = new Timer(true);
	final long collectionPhase = 5000l;
	private TimeFrame tf;
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
		System.out.println("Processing data from " + startTimeStamp.getCalendar().getTime() + " to " + endTimeStamp.getCalendar().getTime());

		ApplicationContext appContext = new ClassPathXmlApplicationContext(new String[] { springFile });
		System.out.println("Starting up and fetching idf");
//		IDaoFactory idf = (IDaoFactory) appContext.getBean("ibatisDao");
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
//				int depth = 1;
				if (l.indexOf(";") != -1) {
					String[] s = l.split(";");
					symbol = s[0];
//					depth = Integer.parseInt(s[1]);
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
		System.out.println("Processing " + s);

		// delete what we have for these timestamps.
		writer.delete(s, startTimeStamp, endTimeStamp);

		// ...
		MultiValueTimeSeriesIterator mvsi = reader.getMultiValueStream(s, startTimeStamp, endTimeStamp);
		long currentFrame = 0L;

		OHLCV temp = new OHLCV();
		temp.setResolutionInSeconds(tf.getMinutes() * 60);
		temp.setMdiId(s);

		while (mvsi.hasNext()) {
			System.out.print(".");
			Tuple<TimeStamp, Map<String, Double>> t = mvsi.next();

			long frame = t.getA().getNanoseconds() - t.getA().getNanoseconds()
					% (tf.getMinutes() * 60l * 1000l * 1000l * 1000l);
			if (frame != currentFrame) {
				if (temp.getOpen() != null) {
					System.out.print("X");
					// collectionList.add(temp.clone());
					store(temp.clone());
					writer.commit();

				}
				temp.clear();
				currentFrame = frame;
			}

			Double bid = t.getB().get("BID");
			Double ask = t.getB().get("ASK");
			if(bid!=null && ask!=null){
				temp.update(t.getA(), (bid+ask)/2.0);
			}
			if (bid != null)
				temp.update(t.getA(), bid);
			if (ask != null)
				temp.update(t.getA(), ask);
			if(bid!=null && ask!=null){
				temp.update(t.getA(), (bid+ask)/2.0);
			}

		}

		// System.out.println("Created " + collectionList.size() + " candles.");
		// if (collectionList.size() > 0) {
		// for (OHLCV o : collectionList)
		// store((OHLCV) o);
		// writer.commit();
		// }
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
