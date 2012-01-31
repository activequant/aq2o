package com.activequant.server;

import javax.xml.ws.Endpoint;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.cxf.transport.http_jetty.JettyHTTPServerEngineFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.dao.IDaoFactory;
import com.activequant.servicelayer.soap.MainService;

public class LocalSoapServer {

    private final int port;
    private final String hostName;
    private final IDaoFactory idf; 

    public LocalSoapServer(String hostName, int port) {
        this.port = port;
        this.hostName = hostName;         
        ApplicationContext appContext2 = new ClassPathXmlApplicationContext("fwspring.xml");
        idf = (IDaoFactory) appContext2.getBean("ibatisDao");        
    }

    public void start() throws Exception {
        JettyHTTPServerEngineFactory eg = new JettyHTTPServerEngineFactory();
        eg.createJettyHTTPServerEngine(this.port, "http");

        Object implementor = new MainService(idf);
        Endpoint ep = Endpoint.publish("http://"+hostName+":" + port + "/aq2o", implementor);
        //org.apache.cxf.jaxws.EndpointImpl epImpl = (org.apache.cxf.jaxws.EndpointImpl) ep;
        SOAPBinding soap = (SOAPBinding) ep.getBinding();
        soap.setMTOMEnabled(true);
    }
    
}
