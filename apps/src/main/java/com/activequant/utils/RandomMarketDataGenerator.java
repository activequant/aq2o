package com.activequant.utils;

import java.util.List;

import org.snmp4j.agent.mo.snmp.SNMPv2MIB.SysOREntry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.exceptions.TransportException;
import com.activequant.interfaces.transport.IPublisher;
import com.activequant.interfaces.transport.ITransportFactory;
import com.activequant.messages.AQMessages;
import com.activequant.messages.Marshaller;

public class RandomMarketDataGenerator {

	int maxInstruments = 1000;
	int delayBetweenSendingInMS = 5000;
	IPublisher[] publishers; 
	IPublisher textLine; 
	ITransportFactory transFac;
	Marshaller m = new Marshaller();
	
	public RandomMarketDataGenerator() throws TransportException{
		// 		
		maxInstruments = Integer.parseInt(System.getProperties().getProperty("MAX_INSTRUMENTS", "100"));
		// 
		delayBetweenSendingInMS = Integer.parseInt(System.getProperties().getProperty("SEND_DELAY", "2500"));
		
		ApplicationContext appContext = new ClassPathXmlApplicationContext(new String[]{"fwspring.xml"});
		System.out.println("Starting up and fetching idf");
		transFac = appContext.getBean("jmsTransport", ITransportFactory.class);				
		
		publishers = new IPublisher[maxInstruments];
		for(int i=0;i<maxInstruments;i++){
			publishers[i] = transFac.getPublisher(ETransportType.MARKET_DATA,"INST"+i);
		}
		textLine = transFac.getPublisher("TEXTCHANNEL");
		
		Runnable r = new Runnable(){
			public void run(){
				while(true){
					try{
						Thread.sleep(delayBetweenSendingInMS);
						for(int i=0;i<maxInstruments;i++){
							
							List<Double> doubleListSkipNull = ArrayUtils
									.toDoubleListSkipNull(new Double[]{Math.random()});
							List<Double> doubleListSkipNull2 = ArrayUtils
									.toDoubleListSkipNull(new Double[]{Math.random()});
							List<Double> doubleListSkipNull3 = ArrayUtils
									.toDoubleListSkipNull(new Double[]{Math.random()});
							List<Double> doubleListSkipNull4 = ArrayUtils
									.toDoubleListSkipNull(new Double[]{Math.random()});
							publishers[i].send(m.marshallToMDS("MDI"+i,
									doubleListSkipNull, doubleListSkipNull2,
									doubleListSkipNull3, doubleListSkipNull4));
							
							
						}
						textLine.send("TEST".getBytes());
						System.out.println("Sent.");
					}
					catch(Exception ex){
						ex.printStackTrace();
					}
				}
			}
		};		
		Thread t = new Thread(r);
		t.start();
	}
	
	public static void main(String[] args) throws TransportException{
		new RandomMarketDataGenerator();
	}		
}
