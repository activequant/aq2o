package com.activequant.server;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.handler.AbstractHandler;

import com.activequant.archive.IArchiveFactory;
import com.activequant.archive.TSContainer;
import com.activequant.archive.hbase.HBaseArchiveFactory;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;

public class LocalJettyServer {

	private int port;
	private String zookeeper;
	private IArchiveFactory archFactory;

	public LocalJettyServer(int port, String zookeeper) {
		this.port = port;
		this.zookeeper = zookeeper;
		archFactory = new HBaseArchiveFactory(zookeeper);
	}

	public static void main(String[] args) throws Exception {
		LocalJettyServer s = new LocalJettyServer(44444, "localhost");
		s.start();
	}

	public void start() throws Exception {
		Server server = new Server();
		Connector connector = new SocketConnector();
		connector.setPort(port);
		server.setConnectors(new Connector[] { connector });

		Handler handler = new RequestHandler();
		server.setHandler(handler);

		server.start();
		server.join();
	}

	private String instructions = "You need to specify: SERIESID, FREQ, FIELD, STARTDATE, ENDDATE. Example:http://localhost:44444/?SERIESID=BBGT_IRZ10 Comdty&FREQ=EOD&FIELD=PX_SETTLE&STARTDATE=20010101&ENDDATE=20120301";

	public class RequestHandler extends AbstractHandler {
		public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch)
				throws IOException, ServletException {
			Request base_request = (request instanceof Request) ? (Request) request : HttpConnection
					.getCurrentConnection().getRequest();
			base_request.setHandled(true);
			// response.setContentType("plain/txt");
			response.setStatus(HttpServletResponse.SC_OK);

			@SuppressWarnings("rawtypes")
			Map paramMap = request.getParameterMap();
			if (paramMap.containsKey("SERIESID") && paramMap.containsKey("FREQ") && paramMap.containsKey("FIELD")
					&& paramMap.containsKey("STARTDATE") && paramMap.containsKey("ENDDATE")) {

				String timeFrame = ((String[]) paramMap.get("FREQ"))[0]; 
				System.out.println(timeFrame);
				TimeFrame tf = TimeFrame.valueOf(timeFrame);
				String mdiId = ((String[]) paramMap.get("SERIESID"))[0]; 
				String field = ((String[]) paramMap.get("FIELD"))[0]; 
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				String sd = ((String[]) paramMap.get("STARTDATE"))[0];
				String ed = ((String[]) paramMap.get("ENDDATE"))[0]; 

				TimeStamp start;
				try {
					start = new TimeStamp(sdf.parse(sd));

					TimeStamp end = new TimeStamp(sdf.parse(ed));
					TSContainer container = archFactory.getReader(tf).getTimeSeries(mdiId, field, start, end);
					for(int i=0;i<container.timeStamps.length;i++){
						response.getWriter().print(container.timeStamps[i]);
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

			} else {
				// response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().println(instructions);
			}

		}
	}

}
