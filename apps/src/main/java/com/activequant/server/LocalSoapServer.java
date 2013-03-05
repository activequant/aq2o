package com.activequant.server;

import javax.xml.ws.Endpoint;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.interfaces.archive.IArchiveFactory;
import com.activequant.interfaces.dao.IDaoFactory;
import com.activequant.servicelayer.soap.MainService;

/**
 * 
 * @author GhostRider
 *
 */
public class LocalSoapServer {

	private final int port;
	private final String hostName;
	private final IDaoFactory idf;
	private final IArchiveFactory archFac;

	public LocalSoapServer(String hostName, int port) {
		this.port = port;
		this.hostName = hostName;
		ApplicationContext appContext2 = new ClassPathXmlApplicationContext(
				"fwspring.xml");
		idf = (IDaoFactory) appContext2.getBean("ibatisDao");
		archFac = (IArchiveFactory) appContext2.getBean("archiveFactory");
	}

	public void start() throws Exception {

		// Setup the system properties to use the CXFBusFactory not the
		// SpringBusFactory
		String busFactory = System
				.getProperty(BusFactory.BUS_FACTORY_PROPERTY_NAME);
		System.setProperty(BusFactory.BUS_FACTORY_PROPERTY_NAME,
				"org.apache.cxf.bus.CXFBusFactory");

		// Start up the jetty embedded server
		Server httpServer = new Server(port);
		ContextHandlerCollection contexts = new ContextHandlerCollection();
		httpServer.setHandler(contexts);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        httpServer.setHandler(context);


		CXFNonSpringServlet cxf = new CXFNonSpringServlet();
        ServletHolder servlet = new ServletHolder(cxf);
        servlet.setName("soap");
        servlet.setForcedPath("soap");

		context.addServlet(servlet, "/*");

		httpServer.start();

		Bus bus = cxf.getBus();
		// setBus(bus);

		BusFactory.setDefaultBus(bus);

		Object implementor = new MainService(idf, archFac);
		Endpoint ep = Endpoint.publish("/main", implementor);
		SOAPBinding soap = (SOAPBinding) ep.getBinding();

		soap.setMTOMEnabled(false);
	}

	public void addService(String serviceSuburl, Object serviceImplementor) {
		Endpoint ep = Endpoint.publish("http://" + hostName + ":" + port + "/"
				+ serviceSuburl, serviceImplementor);
		SOAPBinding soap = (SOAPBinding) ep.getBinding();
		soap.setMTOMEnabled(false);
	}

	public static void main(String[] args) throws Exception {
		new LocalSoapServer("localhost", 8765).start();
	}

}
