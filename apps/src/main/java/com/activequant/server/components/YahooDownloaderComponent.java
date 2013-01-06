package com.activequant.server.components;

import java.util.List;

import org.snmp4j.agent.mo.snmp.SNMPv2MIB.SysOREntry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.activequant.component.ComponentBase;
import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.exceptions.TransportException;
import com.activequant.interfaces.transport.IPublisher;
import com.activequant.interfaces.transport.ITransportFactory;
import com.activequant.messages.AQMessages;
import com.activequant.messages.Marshaller;
import com.activequant.utils.ArrayUtils;

/**
 * sample component that creates random market data. 
 * 
 * @author GhostRider
 *
 */
public class YahooDownloaderComponent extends ComponentBase {

		public YahooDownloaderComponent(ITransportFactory transFac)
			throws Exception {
		super("RandomDataGenerator", transFac);
	
		Runnable r = new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000 * 60);
						// check if it is a new day. 
						
						// 
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		};
		Thread t = new Thread(r);
		t.start();
	}

	@Override
	public String getDescription() {
		//
		return "The Yahoo Downloader component downloads daily data from Yahoo. ";

	}

}
