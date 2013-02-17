package com.activequant.utils.checks;

import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.streaming.MarketDataSnapshot;
import com.activequant.interfaces.transport.IPublisher;
import com.activequant.interfaces.transport.IReceiver;
import com.activequant.interfaces.transport.ITransportFactory;
import com.activequant.interfaces.utils.IEventListener;
import com.activequant.messages.AQMessages;
import com.activequant.messages.AQMessages.BaseMessage;
import com.activequant.messages.AQMessages.BaseMessage.CommandType;
import com.activequant.messages.Marshaller;
import com.activequant.messages.MessageFactory;
import com.activequant.transport.activemq.ActiveMQTransportFactory;
import com.activequant.utils.UniqueTimeStampGenerator;
import com.google.protobuf.InvalidProtocolBufferException;

public class PerformanceMeasure {
	private Marshaller m = new Marshaller();
	private UniqueTimeStampGenerator utsg = new UniqueTimeStampGenerator();
	private int counter = 99;
	private long L1, L2, L3;

	public PerformanceMeasure(ITransportFactory transFac) throws Exception {

		IPublisher p = transFac.getPublisher(ETransportType.TRAD_DATA,
				"TT.SFE.XTH3M3.SFE_XT");
		IReceiver r = transFac.getReceiver(ETransportType.TRAD_DATA,
				"TT.SFE.XTH3M3.SFE_XT");

		r.getRawEvent().addEventListener(new IEventListener<byte[]>() {

			@Override
			public void eventFired(byte[] event) {
				try {
					BaseMessage bm = m.demarshall(event);
					if (bm.getType().equals(CommandType.CUST_CMD)) {
						String custCommand = ((AQMessages.CustomCommand) bm
								.getExtension(AQMessages.CustomCommand.cmd))
								.getCommand();
						if (custCommand.startsWith("LATENCY_RES")) {
							String[] parts = custCommand.split(" ");
							long l1 = Long.parseLong(parts[1]);
							long l2 = Long.parseLong(parts[2]);
							long l3 = System.nanoTime(); // utsg.now().getNanoseconds();

							long fromHereToThere = l2 - l1;
							long fromTheretoHere = l3 - l2;
							long roundTrip = l3 - l1;

							L1 += fromHereToThere;
							L2 += fromTheretoHere;
							L3 += roundTrip;
							counter--;

							//
						}
					}
				} catch (InvalidProtocolBufferException e) {
					e.printStackTrace();
				}

			}
		});

		// and measure the latency.
		MessageFactory mf = new MessageFactory();

		while (true) {
			L1 = 0;
			L2 = 0;
			L3 = 0;
			int maxCounter = 1000;
			counter = maxCounter;
			long l4 = System.nanoTime();
			for (int i = 0; i < maxCounter; i++) {
				BaseMessage bm = mf.buildCustomCommand("LATENCY_REQ "
				// + utsg.now().getNanoseconds() + " "
						+ System.nanoTime() + " " + "TT.SFE.XTH3M3.SFE_XT");
				p.send(bm.toByteArray());

			}
			long l5 = System.nanoTime();
			while (counter != 0) {
				Thread.sleep(10);
			}
			//

			System.out.println(L1 / maxCounter / 1000.0 + " " + L2 / maxCounter
					/ 1000.0 + " " + L3 / maxCounter / 1000.0 + " "
					+ ((l5 - l4) / maxCounter / 1000.0));
			Thread.sleep(1000);
		}

	}

	/**
	 * 
	 * @param test
	 * @throws Exception
	 */
	public PerformanceMeasure(boolean test) throws Exception {

		Marshaller m = new Marshaller();
		MessageFactory mf = new MessageFactory();

		//
		for (int h = 0; h < 1000; h++) {
			long total = 0L;
			for (int i = 0; i < 1000; i++) {
				long l1 = System.nanoTime();
				MarketDataSnapshot mds = new MarketDataSnapshot();
				mds.setMdiId("TT.SFE.XTH3M3.SFE_XT");
				
				mds.setTimeStamp(new TimeStamp());
				mds.setBidPrices(new double[] { Math.random() });
				mds.setAskPrices(new double[] { Math.random() });
				mds.setBidSizes(new double[] { Math.random() });
				mds.setAskSizes(new double[] {Math.random() });
				
				
				
					
				byte[] b = m.marshall(mds);
				BaseMessage bm = m.demarshall(b);
			
				if (bm.getType().equals(CommandType.MDS)) {
					//
					MarketDataSnapshot mds2 = m
							.demarshall(((AQMessages.MarketDataSnapshot) bm
									.getExtension(AQMessages.MarketDataSnapshot.cmd)));

					// done ...
					long l2 = System.nanoTime();
					total += (l2 - l1);
					
				}

						
			}
			System.out.println("TOTAL: " + total +" ns for 1000 serializations and deserializations. This is " + (total / 1000L) + " ns per market data snapshot. ");
		}

	}

	public PerformanceMeasure() throws Exception {
		// connect to transport
		ITransportFactory transFac = new ActiveMQTransportFactory("localhost",
				61616);
		new PerformanceMeasure(transFac);
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		new PerformanceMeasure(false);

	}

}
