package com.activequant.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import com.activequant.archive.hbase.HBaseArchiveFactory;
import com.activequant.interfaces.archive.IArchiveFactory;
import com.activequant.server.web.CSVServlet;

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
	// the actual jetty server instance.
	private Server server;
	private String webappFolder; 
	// 		jetty.webapp.folder
	public LocalJettyServer(int port, String zookeeper, String zookeeperPort, String webappFolder) {
		this.port = port;
		this.webappFolder = webappFolder; 
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
		LocalJettyServer s = new LocalJettyServer(44444, "localhost", "2181", "../webapp");
		s.start();
	}

	public void start() throws Exception {
		log.info("Starting new AQ jetty server.");
		// instantiating the server.
		server = new Server(port);
		
		// instantiate a local request handler.

		// 
		WebAppContext wac = new WebAppContext();
		wac.setContextPath("/");
		wac.setWar(webappFolder);
		// 		
		
		ServletContextHandler csvContext = new ServletContextHandler(server, "/csv", true, false);
        csvContext.addServlet(new ServletHolder(new CSVServlet(archFactory)), "/");
		
		
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { csvContext, wac }); // , resource_handler });
		server.setHandler(handlers);
		
		
		

		//
		server.start();
		server.join();
	}

}
