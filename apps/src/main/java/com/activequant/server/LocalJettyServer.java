package com.activequant.server;

import org.apache.log4j.Logger;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Credential;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;

import com.activequant.archive.hbase.HBaseArchiveFactory;
import com.activequant.interfaces.archive.IArchiveFactory;
import com.activequant.interfaces.dao.IDaoFactory;
import com.activequant.server.web.CSVServlet;
import com.activequant.server.web.RefDataCSVServlet;

/**
 * The local jetty server provides REST like functionality.
 * 
 * @author GhostRider
 * 
 */
public class LocalJettyServer {

	private int port;
	private IArchiveFactory archFactory;
	private IDaoFactory daoFactory; 
	private Logger log = Logger.getLogger(LocalJettyServer.class);
	// the actual jetty server instance.
	private Server server;
	private String webappFolder;
	private String sslKeyStoreLocation = null;
	private String sslKeyStorePassword;
	private String sslKeyPassword;
	private String sslCertName;

	// jetty.webapp.folder
	public LocalJettyServer(int port, String zookeeper, String zookeeperPort,
			String webappFolder, String sslKeyStoreLocation,
			String sslKeyStorePassword, String sslKeyPassword,
			String sslCertName, IDaoFactory daoFactory) {
		this.sslKeyStoreLocation = sslKeyStoreLocation;
		this.sslKeyStorePassword = sslKeyStorePassword;
		this.sslKeyPassword = sslKeyPassword;
		this.sslCertName = sslCertName;
		this.port = port;
		this.webappFolder = webappFolder;
		this.daoFactory = daoFactory; 
		if (zookeeper != null) {
			archFactory = new HBaseArchiveFactory(zookeeper,
					Integer.parseInt(zookeeperPort));
		}

	}

	public LocalJettyServer(int port, String zookeeper, String zookeeperPort,
			String webappFolder, IDaoFactory daoFactory) {
		this.port = port;
		this.webappFolder = webappFolder;
		this.daoFactory = daoFactory; 
		if (zookeeper != null) {
			archFactory = new HBaseArchiveFactory(zookeeper,
					Integer.parseInt(zookeeperPort));
		}
	}

	public LocalJettyServer(int port, IDaoFactory daoFactory) {
		this.port = port;
		this.daoFactory = daoFactory; 
	}

	/**
	 * default main that runs the jetty server in foreground, connecting to a
	 * local hbase server.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		LocalJettyServer s = new LocalJettyServer(44444, "localhost", "2181",
				"../webapp",null);
		s.start();
	}

	//
	public void start() throws Exception {
		log.info("Starting new AQ jetty server.");
		// instantiating the server.
		server = new Server(port);

		if (this.sslKeyStoreLocation != null) {
			// use ssl as things are set.
			Connector[] connectors = server.getConnectors();
			SslContextFactory sslCtx = new SslContextFactory(
					sslKeyStoreLocation);
			sslCtx.setKeyStorePassword(sslKeyStorePassword);
			sslCtx.setKeyManagerPassword(sslKeyPassword);
			sslCtx.setCertAlias(sslCertName);
			ServerConnector sc = new ServerConnector(server, sslCtx);
			sc.setPort(port);
			connectors = new Connector[1];
			connectors[0] = sc;
			server.setConnectors(connectors);

		}

		// let's add a default user.
		HashLoginService ls = new HashLoginService("ActivrQuant MASTER");
		ls.putUser("user", Credential.getCredential("user"),
				new String[] { "user" });
		server.addBean(ls);

		// instantiate a webapp context.
		WebAppContext wac = new WebAppContext();
		wac.setContextPath("/");
		wac.setWar(webappFolder);
		//
		ServletContextHandler csvContext = new ServletContextHandler(server,
				"/csv", true, false);
		csvContext.addServlet(new ServletHolder(new CSVServlet(archFactory)),
				"/");
		ServletContextHandler refDataContext = new ServletContextHandler(server,
				"/refdata", true, false);
		refDataContext.addServlet(new ServletHolder(new RefDataCSVServlet(daoFactory)),
				"/");
		// register all handlers.
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { csvContext, refDataContext, wac }); // ,
																	// resource_handler
																	// });
		server.setHandler(handlers);

		//
		server.start();
		server.join();
	}

}
