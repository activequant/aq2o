package com.activequant.utils.checks;

import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.exceptions.TransportException;
import com.activequant.interfaces.transport.IPublisher;
import com.activequant.interfaces.transport.IReceiver;
import com.activequant.interfaces.transport.ITransportFactory;
import com.activequant.interfaces.utils.IEventListener;
import com.activequant.messages.AQMessages.BaseMessage;
import com.activequant.messages.AQMessages.BaseMessage.CommandType;
import com.activequant.messages.AQMessages;
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
							counter --; 
							
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
			L1 = 0; L2 = 0; L3 = 0; 
			int maxCounter = 1000; 
			counter = maxCounter;
			long l4 = System.nanoTime();
			for (int i = 0; i < maxCounter; i++) {
				BaseMessage bm = mf.buildCustomCommand("LATENCY_REQ "
						// + utsg.now().getNanoseconds() + " "
						+ System.nanoTime() + " " 
						+ "TT.SFE.XTH3M3.SFE_XT");
				p.send(bm.toByteArray());
				
			}
			long l5 = System.nanoTime();
			while(counter!=0)
			{				
				Thread.sleep(10);
			}
			// 
			
			System.out.println(L1 / maxCounter  / 1000.0+ " "
					+ L2 /  maxCounter / 1000.0+ " "
					+ L3 / maxCounter / 1000.0+ " " + ((l5-l4) / maxCounter / 1000.0 ) );
			Thread.sleep(1000);
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
		new PerformanceMeasure();

	}

}
