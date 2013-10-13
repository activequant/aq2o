package com.activequant.server.components;

import java.util.List;

import com.activequant.component.ComponentBase;
import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.interfaces.transport.IPublisher;
import com.activequant.interfaces.transport.ITransportFactory;
import com.activequant.messages.Marshaller;
import com.activequant.messages.MessageFactory2;
import com.activequant.utils.ArrayUtils;

/**
 * sample component that creates random market data. 
 * 
 * @author GhostRider
 *
 */
public class RandomMarketDataGenerator extends ComponentBase {

	int maxInstruments = 1000;
	int delayBetweenSendingInMS = 5000;
	IPublisher[] publishers;
	IPublisher textLine;
	IPublisher valueSet ; 
	ITransportFactory transFac;
	Marshaller m = new Marshaller();
	MessageFactory2 mf = new MessageFactory2();

	public RandomMarketDataGenerator(ITransportFactory transFac)
			throws Exception {
		super("RandomDataGenerator", transFac);
		//
		maxInstruments = Integer.parseInt(System.getProperties().getProperty(
				"MAX_INSTRUMENTS", "100"));
		//
		delayBetweenSendingInMS = Integer.parseInt(System.getProperties()
				.getProperty("SEND_DELAY", "5000"));

		publishers = new IPublisher[maxInstruments];
		for (int i = 0; i < maxInstruments; i++) {
			publishers[i] = transFac.getPublisher(ETransportType.MARKET_DATA,
					"INST" + i);
		}
		// 
		textLine = transFac.getPublisher("TEXTCHANNEL");
		valueSet = transFac.getPublisher(ETransportType.STATE, "ID1");

		Runnable r = new Runnable() {
			public void run() {
				while (true) {
					try {
						log.info("Sleeping for " + delayBetweenSendingInMS);
						Thread.sleep(delayBetweenSendingInMS);
						for (int i = 0; i < maxInstruments; i++) {

							List<Double> doubleListSkipNull = ArrayUtils
									.toDoubleListSkipNull(new Double[] { Math
											.random() });
							List<Double> doubleListSkipNull2 = ArrayUtils
									.toDoubleListSkipNull(new Double[] { Math
											.random() });
							List<Double> doubleListSkipNull3 = ArrayUtils
									.toDoubleListSkipNull(new Double[] { Math
											.random() });
							List<Double> doubleListSkipNull4 = ArrayUtils
									.toDoubleListSkipNull(new Double[] { Math
											.random() });
							publishers[i].send(m.marshallToMDS("MDI" + i,
									doubleListSkipNull, doubleListSkipNull2,
									doubleListSkipNull3, doubleListSkipNull4));

						}
						textLine.send("TEST".getBytes());
						valueSet.send(mf.valueSet(new TimeStamp(), "TYPE", "ID", "FIELD", "VALUE"+Math.random()).toByteArray());
						log.info("Sent.");
						// System.out.println("Sent.");
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
		return "The random market data generator generates random bid/ask events for "
				+ maxInstruments
				+ " instruments. Their IDs look like this: INST1, INST25, INST99.";

	}

}
