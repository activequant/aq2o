package com.activequant.server;

import javax.xml.ws.Endpoint;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.cxf.transport.http_jetty.JettyHTTPServerEngineFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.interfaces.archive.IArchiveFactory;
import com.activequant.interfaces.dao.IDaoFactory;
import com.activequant.servicelayer.soap.MainService;

public class LocalSoapServer {

    private final int port;
    private final String hostName;
    private final IDaoFactory idf;
    private final IArchiveFactory archFac;

    public LocalSoapServer(String hostName, int port) {
        this.port = port;
        this.hostName = hostName;         
        ApplicationContext appContext2 = new ClassPathXmlApplicationContext("fwspring.xml");
        idf = (IDaoFactory) appContext2.getBean("ibatisDao");
        archFac = (IArchiveFactory) appContext2.getBean("archiveFactory");
    }

    public void start() throws Exception {
        JettyHTTPServerEngineFactory eg = new JettyHTTPServerEngineFactory();
        eg.createJettyHTTPServerEngine(this.port, "http");
        // bind the main service. 
        Object implementor = new MainService(idf, archFac);
        Endpoint ep = Endpoint.publish("http://"+hostName+":" + port + "/main", implementor);             
        SOAPBinding soap = (SOAPBinding) ep.getBinding();
        
        soap.setMTOMEnabled(false);
    }
    
    public void addService(String serviceSuburl, Object serviceImplementor){
        Endpoint ep = Endpoint.publish("http://"+hostName+":" + port + "/"+serviceSuburl, serviceImplementor);
        SOAPBinding soap = (SOAPBinding) ep.getBinding();
        soap.setMTOMEnabled(false);
    }
    
    public static void main(String[] args) throws Exception {
    	new LocalSoapServer("localhost", 8765).start();
    }
    
}
