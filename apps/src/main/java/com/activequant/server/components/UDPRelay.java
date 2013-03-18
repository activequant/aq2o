package com.activequant.server.components;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.activequant.component.ComponentBase;
import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.interfaces.transport.IReceiver;
import com.activequant.interfaces.transport.ITransportFactory;
import com.activequant.interfaces.utils.IEventListener;
import com.activequant.messages.AQMessages.BaseMessage;
import com.activequant.messages.AQMessages.ValueSet;
import com.activequant.messages.Marshaller;
import com.activequant.messages.MessageFactory;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * work in progress.
 * 
 * @author GhostRider
 * 
 */
public class UDPRelay extends ComponentBase {
	//
	private final String udpTargetHost;
	private final int udpTargetPort;
	private String[] channels = new String[] {};
	MessageFactory mf = new MessageFactory();

	public UDPRelay(ITransportFactory localNode) throws Exception {
		super("UDPRelay", localNode);
		//
		this.udpTargetHost = super.properties.getProperty("UDP_TARGET_HOST",
				"192.168.0.177");
		this.udpTargetPort = Integer.parseInt(super.properties.getProperty(
				"UDP_TARGET_PORT", "54321"));

		//
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					subscribe();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		// let's start a separate thread for this component.
		t.start();

		Thread t2 = new Thread(new ListenerThread());
		t2.start();
	}

	class ListenerThread implements Runnable {
		//
		public void run() {
			try {
				System.out.println("Starting listener thread at port "
						+ udpTargetPort + " ...");
				DatagramSocket serverSocket = new DatagramSocket(udpTargetPort);
				while (true) {
					// read packets, we will take a maximum size of 1300.
					byte[] buffer = new byte[6 * 200];
					DatagramPacket packet = new DatagramPacket(buffer,
							buffer.length);

					serverSocket.receive(packet);
					handlePacket(packet);
					//
				}
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private void handlePacket(DatagramPacket packet) {
			// let's process
			for (int i = 0; i < 200; i++) {
				//
				int startPos = i * 6;
				int endPos = startPos + 6;
				//
				byte[] floatValue = new byte[4];
				//
				if (packet.getData()[startPos] == 0x00)
					break;
				//
				System.arraycopy(packet.getData(), startPos + 2, floatValue, 0,
						4);
				//
				byte iid = packet.getData()[startPos + 1];
				//
				float f = ByteBuffer.wrap(floatValue)
						.order(ByteOrder.BIG_ENDIAN).getFloat();

				String instId = "" + new Integer(iid);
				System.out.println("Theoretical price for instrument id "
						+ instId + " = " + f);

				//
				BaseMessage bm = mf
						.valueSet(new TimeStamp(),
								ETransportType.STATE.toString(), instId,
								"THEO", "" + f);
				try {
					transFac.getPublisher(ETransportType.STATE, instId).send(
							bm.toByteArray());
				} catch (Exception e) {
					e.printStackTrace();
				}
				//
				//
			}

			System.out.println("===PacketDump======");
			for (int i = 0; i < packet.getData().length; i++) {
				System.out.print(packet.getData()[i] + " ");
			}
			System.out.println("\n=========");
		}
	}

	private void subscribe() throws Exception {

		final DatagramSocket clientSocket = new DatagramSocket();
		final InetAddress ipAddress = InetAddress.getByName(this.udpTargetHost);

		for (int i = 0; i < 200; i++) {
			//
			IReceiver receiver = transFac.getReceiver(ETransportType.STATE, ""
					+ i);
			//
			receiver.getRawEvent().addEventListener(
					new IEventListener<byte[]>() {
						@Override
						public void eventFired(byte[] arg0) {
							try{
							// make a value set out of it.
							BaseMessage bm = null; 
							try {
								bm = new Marshaller().demarshall(arg0);
							} catch (InvalidProtocolBufferException e1) {
								//  Auto-generated catch block
								e1.printStackTrace();
							}
							if(bm==null)
								return; 
							ValueSet vs = bm.getExtension(ValueSet.cmd);

							String s = vs.getId();
							String field = vs.getField();
							String value = vs.getValue();
							Double v = Double.parseDouble(value);
							//
							byte commandByte = 0x01;
							if (field.equals("M")) {
								//
								commandByte = 0x01;
							} else if (field.equals("V")) {
								//
								commandByte = 0x02;
							}
							//
							Integer id = Integer.parseInt(s);
							//
							byte instrumentId = id.byteValue();
							//
							// assemble a datagram and send it ...
							byte[] data = new byte[6];
							data[0] = commandByte;
							data[1] = instrumentId;
							//
							byte[] bytes = new byte[4];
							ByteBuffer.wrap(bytes).putFloat(v.floatValue());
							//
							data[2] = bytes[0];
							data[3] = bytes[1];
							data[4] = bytes[2];
							data[5] = bytes[3];
							//
							try {
								System.out.println("Sending " + id + " / " + v +" / field : " + field);
								clientSocket
										.send(new DatagramPacket(bytes,
												bytes.length, ipAddress,
												udpTargetPort));
							} catch (IOException e) {
								e.printStackTrace();
							}
							//
							}
							catch(Exception ex){ex.printStackTrace();}
						}
					});
		}
		//
	}

	//
	@Override
	public String getDescription() {
		//
		return "Target host: " + this.udpTargetHost + " - target port: "
				+ this.udpTargetPort;
	}

}
