package com.activequant.server.web;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.activequant.archive.MultiValueTimeSeriesIterator;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.Tuple;
import com.activequant.interfaces.archive.IArchiveFactory;
import com.activequant.interfaces.archive.IArchiveWriter;

public class CSVServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IArchiveFactory archFac;
	private Logger log = Logger.getLogger(CSVServlet.class);
	private DecimalFormat dcf = new DecimalFormat("#.###########");

	public CSVServlet(IArchiveFactory archFac) {
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

	protected void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {

		if (req.getRequestURI().endsWith("sampledata")) {
			dumpSampleData(response);
			return;
		}

		@SuppressWarnings("rawtypes")
		Map paramMap = req.getParameterMap();
		if (paramMap.containsKey("SERIESID") && paramMap.containsKey("FREQ")
				&& paramMap.containsKey("FIELD")
				&& paramMap.containsKey("STARTDATE")
				&& paramMap.containsKey("ENDDATE")) {

			String timeFrame = ((String[]) paramMap.get("FREQ"))[0];

			TimeFrame tf = TimeFrame.valueOf(timeFrame);
			String mdiId = ((String[]) paramMap.get("SERIESID"))[0];
			String field = ((String[]) paramMap.get("FIELD"))[0];
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String sd = ((String[]) paramMap.get("STARTDATE"))[0];
			String ed = ((String[]) paramMap.get("ENDDATE"))[0];

			log.info("Fetching: " + timeFrame + " - " + mdiId + " - " + field
					+ " - " + sd + " - " + ed);

			TimeStamp start;
			try {
				start = new TimeStamp(sdf.parse(sd));
				int maxRows = 1000000;
				TimeStamp end = new TimeStamp(sdf.parse(ed));

				response.getWriter().print(
						"TimeStampNanos,DateTime," + field + "\n");
				response.getWriter().flush();

				String[] fields = field.split(",");

				MultiValueTimeSeriesIterator mvtsi = archFac.getReader(tf)
						.getMultiValueStream(mdiId, start, end);
				int i = 0;
				while (mvtsi.hasNext()) {
					Tuple<TimeStamp, Map<String, Double>> values = mvtsi.next();
					// String[] p = l.split(",");

					// check if our map contains some of our requested fields.

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
							if (val != Double.NaN) {
								response.getWriter().print(dcf.format(val));
							}
						}
						if (j != (fields.length - 1))
							response.getWriter().print(",");
					}
					response.getWriter().println();
					response.getWriter().flush();

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