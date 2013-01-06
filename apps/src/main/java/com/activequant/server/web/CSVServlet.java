package com.activequant.server.web;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.activequant.archive.TSContainer;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.interfaces.archive.IArchiveFactory;
import com.activequant.interfaces.archive.IArchiveWriter;

public class CSVServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IArchiveFactory archFac;
	private Logger log = Logger.getLogger(CSVServlet.class);

	public CSVServlet(IArchiveFactory archFac) {
		this.archFac = archFac;

	}
	private String instructions = "You need to specify: SERIESID, FREQ, FIELD, STARTDATE, ENDDATE. Example:http://localhost:44444/csv/?SERIESID=BBGT_IRZ10 Comdty&FREQ=EOD&FIELD=PX_SETTLE&STARTDATE=20010101&ENDDATE=20120301";

	protected void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {

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
				TSContainer container = archFac.getReader(tf).getTimeSeries(
						mdiId, field, start, end);
				response.getWriter().print(
						"TimeStampNanos,DateTime," + field + "\n");
				for (int i = 0; i < container.timeStamps.length; i++) {
					// limiting to 1million rows.
					if (i >= maxRows)
						break;
					response.getWriter().print(container.timeStamps[i]);
					response.getWriter().print(",");
					response.getWriter().print(
							container.timeStamps[i].getCalendar().getTime());
					response.getWriter().print(",");
					response.getWriter().print(container.values[i]);
					response.getWriter().println();
					response.getWriter().flush();
				}
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else{
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