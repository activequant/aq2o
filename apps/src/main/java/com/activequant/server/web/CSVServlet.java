package com.activequant.server.web;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.activequant.archive.MultiValueTimeSeriesIterator;
import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.Tuple;
import com.activequant.domainmodel.exceptions.DaoException;
import com.activequant.interfaces.archive.IArchiveFactory;
import com.activequant.interfaces.archive.IArchiveWriter;
import com.activequant.interfaces.dao.IDaoFactory;

public class CSVServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IArchiveFactory archFac;
	private IDaoFactory daoF;
	private Logger log = Logger.getLogger(CSVServlet.class);
	private DecimalFormat dcf = new DecimalFormat("#.###########");

	public CSVServlet(IArchiveFactory archFac, IDaoFactory daoF) {
		this.archFac = archFac;
	}

	private String instructions = "You need to specify: SERIESID, FREQ, FIELD, STARTDATE, ENDDATE. Example:http://localhost:44444/csv/?SERIESID=BBGT_IRZ10 Comdty&FREQ=EOD&FIELD=PX_SETTLE&STARTDATE=20010101&ENDDATE=20120301";

	private void dumpSampleData(HttpServletResponse response)
			throws IOException {
		response.getWriter().println("TimeStampNanos,DateTime,RAND");
		for (long i = 0; i < 1000; i++) {
			Date d = new Date(i * 10000000);
			String line = d.getTime() + "," + d + "," + Math.random();
			response.getWriter().println(line);
			response.getWriter().flush();
		}
	}

	private void dumpRaw(HttpServletRequest req, HttpServletResponse response)
			throws Exception {
		@SuppressWarnings("rawtypes")
		Map paramMap = req.getParameterMap();
		TimeStamp end = new TimeStamp();
		TimeStamp start = new TimeStamp(Long.parseLong(((String[]) paramMap
				.get("START"))[0]));
		// let's create an inflater ... new DeflaterOutputStream
		OutputStream out = (response.getOutputStream());
		String timeFrame = ((String[]) paramMap.get("FREQ"))[0];
		TimeFrame tf = TimeFrame.valueOf(timeFrame);
		String seriesId = ((String[]) paramMap.get("SERIESID"))[0];
		MultiValueTimeSeriesIterator mvtsi = archFac.getReader(tf)
				.getMultiValueStream(seriesId, start, end);
		//
		while (mvtsi.hasNext()) {
			Tuple<TimeStamp, Map<String, Double>> values = mvtsi.next();
			// let's dump all values.
			StringBuffer sb = new StringBuffer();
			sb.append(values.getA().getNanoseconds() + ";");
			Iterator<Entry<String, Double>> iterator = values.getB().entrySet()
					.iterator();
			while (iterator.hasNext()) {
				Entry<String, Double> entry = iterator.next();
				if (entry.getValue() != null && entry.getKey() != null) {
					sb.append(entry.getKey()).append("=");
					sb.append(dcf.format(entry.getValue())).append(";");
				}
			}
			sb.append("\n");
			//
			out.write(sb.toString().getBytes());
			out.flush();
		}

	}

	protected void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {

		if (req.getRequestURI().endsWith("sampledata")) {
			dumpSampleData(response);
			return;
		}

		@SuppressWarnings("rawtypes")
		Map paramMap = req.getParameterMap();
		//
		if (paramMap.containsKey("DUMP"))
			try {
				dumpRaw(req, response);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		else if (paramMap.containsKey("DELETE")) {
			String seriesId = null; 
			String field = null; 
			String freq = null; 
			TimeStamp start = null; 		
			TimeStamp end = null; 
			
			// 
			if(paramMap.containsKey("FREQ"))
				freq = ((String[]) paramMap.get("FREQ"))[0];
			
			if(paramMap.containsKey("FIELD"))
				field = ((String[]) paramMap.get("FIELD"))[0];
			
			if(paramMap.containsKey("SERIESID"))
				seriesId = ((String[]) paramMap.get("SERIESID"))[0];
			
			if(paramMap.containsKey("STARTTS"))
				start = new TimeStamp(Long.parseLong(((String[]) paramMap.get("STARTTS"))[0]));
			
			if(paramMap.containsKey("ENDTS"))
				end = new TimeStamp(Long.parseLong(((String[]) paramMap.get("ENDTTS"))[0]));
			else
				end = new TimeStamp();
			
			if(seriesId!=null && freq != null && start!=null){
				TimeFrame tf = TimeFrame.valueOf(freq); 
				IArchiveWriter writer = this.archFac.getWriter(tf);
				//
				if(field==null){
					writer.delete(seriesId, start, end);
				}
				else{
					writer.delete(seriesId, field, start, end);
				}
			}
			// archFac.getWriter(TimeFrame.RAW).delete(arg0, arg1, arg2)
		} else if (paramMap.containsKey("SERIESID")
				&& paramMap.containsKey("FREQ")
				&& paramMap.containsKey("FIELD")
				&& paramMap.containsKey("STARTDATE")
				&& paramMap.containsKey("ENDDATE")) {

			String timeFrame = ((String[]) paramMap.get("FREQ"))[0];

			TimeFrame tf = TimeFrame.valueOf(timeFrame);
			String mdiId = ((String[]) paramMap.get("SERIESID"))[0];
			String field = ((String[]) paramMap.get("FIELD"))[0];
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
			String sd = ((String[]) paramMap.get("STARTDATE"))[0];
			String ed = ((String[]) paramMap.get("ENDDATE"))[0];

			log.info("Fetching: " + timeFrame + " - " + mdiId + " - " + field
					+ " - " + sd + " - " + ed);

			TimeStamp start;
			try {
				if (sd.length() == 8)
					start = new TimeStamp(sdf.parse(sd));
				else
					start = new TimeStamp(sdf2.parse(sd));
				int maxRows = 1000000;
				TimeStamp end;
				if (ed.length() == 8)
					end = new TimeStamp(sdf.parse(ed));
				else
					end = new TimeStamp(sdf2.parse(ed));

				response.getWriter().print(
						"TimeStampNanos,DateTime," + field + "\n");
				response.getWriter().flush();

				String[] fields = field.split(",");

				MultiValueTimeSeriesIterator mvtsi = archFac.getReader(tf)
						.getMultiValueStream(mdiId, start, end);
				int i = 0;
				while (mvtsi.hasNext()) {
					Tuple<TimeStamp, Map<String, Double>> values = mvtsi.next();
					boolean found = false;
					for (String f : fields) {
						if (values.getB().containsKey(f))
							found = true;
					}
					if (!found)
						continue;

					response.getWriter().print(values.getA());
					response.getWriter().print(",");
					response.getWriter().print(
							values.getA().getCalendar().getTime());
					response.getWriter().print(",");
					// now, let's dump all values.
					for (int j = 0; j < fields.length; j++) {
						Double val = values.getB().get(fields[j]);
						if (val != null) {
							// mind, this is just a symptom.
							if (!val.equals(Double.NaN)) {
								response.getWriter().print(dcf.format(val));
							}
						}
						if (j != (fields.length - 1))
							response.getWriter().print(",");
					}
					response.getWriter().println();
					response.getWriter().flush();
					//
					i++;
					if (i >= maxRows)
						break;

				}
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			response.getWriter().print(instructions);
			response.getWriter().flush();
		}
	}

	/**
	 * 
	 * @param seriesId
	 * @throws DaoException
	 */
	private void sanityCheck(String seriesId) throws DaoException {
		String[] similarIds = daoF.mdiDao().findIdsLike(seriesId);
		if (similarIds.length == 0) {
			// create a default market data instrument.
			MarketDataInstrument mdi = new MarketDataInstrument();
			int dotIndex = seriesId.indexOf(".");
			if (dotIndex != -1) {
				String provider = seriesId.substring(0, dotIndex);
				String inst = seriesId.substring(dotIndex + 1);
				mdi.setMdProvider(provider);
				mdi.setProviderSpecificId(inst);
				daoF.mdiDao().update(mdi);
			}
			//
		}
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter response = resp.getWriter();
		//
		log.info("Handling post request. ");
		Map paramMap = req.getParameterMap();

		@SuppressWarnings("rawtypes")
		String s = (String) paramMap.get("SERIESID");
		if (paramMap.containsKey("SERIESID") && paramMap.containsKey("FREQ")
				&& paramMap.containsKey("FIELD")) {

			IArchiveWriter iaw = archFac.getWriter(TimeFrame
					.valueOf((String) paramMap.get("FREQ")));
			if (iaw != null) {
				String seriesId = ((String) paramMap.get("SERIESID"));
				// let's check if we have a market data instrument for SeriesID.
				try {
					sanityCheck(seriesId);
				} catch (DaoException e) {
					// let's ignore anything that goes wrong ...
					e.printStackTrace();
				}
				//

				String field = ((String) paramMap.get("FIELD"));
				log.info("Storing data for " + seriesId + "/" + field + "/"
						+ paramMap.get("FREQ"));
				String data = paramMap.get("DATA").toString();
				BufferedReader br2 = new BufferedReader(new InputStreamReader(
						new ByteArrayInputStream(data.getBytes())));
				String line = br2.readLine();
				long lineCounter = 0;
				while (line != null) {
					try {
						String[] parts = line.split(",");
						if (parts.length == 2) {
							//
							TimeStamp ts = new TimeStamp(
									(long) Double.parseDouble(parts[0]));
							if (parts[1] != null
									&& !parts[1].trim().equals("NA")) {
								Double val = Double.parseDouble(parts[1]);
								//
								iaw.write(seriesId, ts, field, val);
								log.info("Writing " + seriesId + " / " + ts
										+ " / " + field + " /" + val);
								lineCounter++;
							}
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					line = br2.readLine();
				}
				iaw.commit();
				log.info("Committed " + lineCounter + " lines to storage. ");
				resp.getWriter().println("Wrote " + lineCounter + " lines. ");
			}
		}
	}
}