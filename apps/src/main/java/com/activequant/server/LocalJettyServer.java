package com.activequant.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.kahadb.util.ByteArrayInputStream;
import org.apache.log4j.Logger;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.HttpConnection;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.handler.AbstractHandler;
import org.mortbay.jetty.handler.HandlerList;
import org.mortbay.jetty.handler.ResourceHandler;

import com.activequant.archive.TSContainer;
import com.activequant.archive.hbase.HBaseArchiveFactory;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.interfaces.archive.IArchiveFactory;
import com.activequant.interfaces.archive.IArchiveWriter;

/**
 * The local jetty server provides REST like functionality.
 * 
 * @author GhostRider
 * 
 */
public class LocalJettyServer {

	private int port;
	private IArchiveFactory archFactory;
	private Logger log = Logger.getLogger(LocalJettyServer.class);

	public LocalJettyServer(int port, String zookeeper, String zookeeperPort) {
		this.port = port;
		if (zookeeper != null) {
			archFactory = new HBaseArchiveFactory(zookeeper,
					Integer.parseInt(zookeeperPort));
		}
	}

	public LocalJettyServer(int port) {
		this.port = port;
	}

	/**
	 * default main that runs the jetty server in foreground, connecting to a
	 * local hbase server.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		LocalJettyServer s = new LocalJettyServer(44444, "localhost", "2181");
		s.start();
	}

	public void start() throws Exception {
		log.info("Starting new AQ jetty server.");
		Server server = new Server();
		Connector connector = new SocketConnector();
		connector.setPort(port);
		server.setConnectors(new Connector[] { connector });

		Handler handler = new RequestHandler();

		ResourceHandler resource_handler = new ResourceHandler();
		resource_handler.setWelcomeFiles(new String[] { "index.html" });
		resource_handler.setResourceBase("htmlroot");

		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { handler, resource_handler });
		server.setHandler(handlers);

		server.start();
		server.join();
	}

	private String instructions = "You need to specify: SERIESID, FREQ, FIELD, STARTDATE, ENDDATE. Example:http://localhost:44444/?SERIESID=BBGT_IRZ10 Comdty&FREQ=EOD&FIELD=PX_SETTLE&STARTDATE=20010101&ENDDATE=20120301";

	public class RequestHandler extends AbstractHandler {
		public void handle(String target, HttpServletRequest request,
				HttpServletResponse response, int dispatch) throws IOException,
				ServletException {
			Request base_request = (request instanceof Request) ? (Request) request
					: HttpConnection.getCurrentConnection().getRequest();
			// only handling /csv/
			System.out.println(base_request.getUri());
			if (!base_request.getUri().getPath().equals("/csv/")) {
				base_request.setHandled(false);
				return;
			}
			base_request.setHandled(true);

			if (archFactory == null) {
				//
				response.getWriter()
						.println(
								"No archive endpoint configured. Cannot fetch or write time series data. ");
				return;
			}

			String method = base_request.getMethod();

			boolean fullyHandled = false;

			// response.setContentType("plain/txt");
			response.setStatus(HttpServletResponse.SC_OK);
			if (method.equals("GET")) {

				@SuppressWarnings("rawtypes")
				Map paramMap = request.getParameterMap();
				if (paramMap.containsKey("SERIESID")
						&& paramMap.containsKey("FREQ")
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

					log.info("Fetching: " + timeFrame + " - " + mdiId + " - "
							+ field + " - " + sd + " - " + ed);

					TimeStamp start;
					try {
						start = new TimeStamp(sdf.parse(sd));
						int maxRows = 1000000;
						TimeStamp end = new TimeStamp(sdf.parse(ed));
						TSContainer container = archFactory.getReader(tf)
								.getTimeSeries(mdiId, field, start, end);
						response.getWriter().print(
								"TimeStampNanos,DateTime," + field + "\n");
						for (int i = 0; i < container.timeStamps.length; i++) {
							// limiting to 1million rows.
							if (i >= maxRows)
								break;
							response.getWriter().print(container.timeStamps[i]);
							response.getWriter().print(",");
							response.getWriter().print(
									container.timeStamps[i].getCalendar()
											.getTime());
							response.getWriter().print(",");
							response.getWriter().print(container.values[i]);
							response.getWriter().println();
							response.getWriter().flush();
						}
						fullyHandled = true;
					} catch (ParseException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			} else if (method.equals("POST")) {
				//
				log.info("Handling post request. ");

				InputStream body = request.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						body));
				String l = br.readLine();
				String pname = "";
				Map<String, String> parameterMap = new HashMap<String, String>();
				String currentPart = "";
				while (l != null) {
					System.out.println(l);
					if (l.startsWith("-------------")) {
						// next part
						l = br.readLine();
						if (l == null)
							break;
						pname = l.substring(l.indexOf("\""));

						pname = pname.replaceAll("\"", "");
						currentPart = "";
						br.readLine();
						l = br.readLine();

					}
					if (!pname.equals("")) {
						String current = parameterMap.get(pname);
						if(pname.equals("DATA"))l += "\n";
						if (current != null)
							current += l;
						else
							current = l;
						parameterMap.put(pname, current);
						log.debug("Updating map value of " + pname);
					}
					l = br.readLine();
				}
				log.info("Post request parsed.");
				@SuppressWarnings("rawtypes")
				String s = request.getParameter("SERIESID");
				if (parameterMap.containsKey("SERIESID")
						&& parameterMap.containsKey("FREQ")
						&& parameterMap.containsKey("FIELD")) {

					IArchiveWriter iaw = archFactory.getWriter(TimeFrame
							.valueOf((String) parameterMap.get("FREQ")));
					if (iaw != null) {
						String seriesId = ((String) parameterMap
								.get("SERIESID"));
						String field = ((String) parameterMap.get("FIELD"));
						log.info("Storing data for " + seriesId + "/" + field
								+ "/" + parameterMap.get("FREQ"));
						String data = parameterMap.get("DATA").toString();
						BufferedReader br2 = new BufferedReader(
								new InputStreamReader(new ByteArrayInputStream(
										data.getBytes())));
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
										Double val = Double
												.parseDouble(parts[1]);
										//
										iaw.write(seriesId, ts, field, val);
										lineCounter++;
									}
								}
							} catch (Exception ex) {
								ex.printStackTrace();
							}
							line = br2.readLine();
						}
						iaw.commit();
						log.info("Committed " + lineCounter
								+ " lines to storage. ");
						response.getWriter().println(
								"Wrote " + lineCounter + " lines. ");
					}
				}

				fullyHandled = true;

				//
			}
			if (!fullyHandled) {
				response.getWriter().println(instructions);
			}

		}
	}

}
