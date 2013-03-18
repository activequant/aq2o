package com.activequant.server.components;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.activequant.component.ComponentBase;

/**
 * work in progress.
 * 
 * @author GhostRider
 * 
 */
public class UDPSampleSender extends ComponentBase {
	//
	private final String udpTargetHost;
	private final int udpTargetPort;

	public static void main(String[] args) throws Exception {
		new UDPSampleSender();
	}

	// //
	// public UDPRelay(ITransportFactory remoteNode, ITransportFactory
	// localNode)
	// throws Exception {
	// super("FeedMirror", localNode);
	public UDPSampleSender() throws Exception {
		super("UDP Relay", null);
		//
		this.udpTargetHost = super.properties.getProperty("UDP_TARGET_HOST",
				"192.168.0.177");
		this.udpTargetPort = Integer.parseInt(super.properties.getProperty(
				"UDP_TARGET_HOST", "54321"));

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
				//
				byte[] floatValue = new byte[4];
				//
				if (packet.getData()[startPos] == 0x00)
					break;
				//
				System.arraycopy(packet.getData(), startPos + 2, floatValue, 0,
						4);
				//
				byte instId = packet.getData()[startPos + 1];
				//
				float f = ByteBuffer.wrap(floatValue)
						.order(ByteOrder.BIG_ENDIAN).getFloat();
				System.out.println("Theoretical price for instrument id "
						+ new Integer(instId) + " = " + f);
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
		//
		final DatagramSocket clientSocket = new DatagramSocket();
		final InetAddress ipAddress = InetAddress.getByName(this.udpTargetHost);
		while (true) {
			Thread.sleep(1000);
			byte[] data = new byte[6];
			data[0] = 0x01;

			data[1] = 0x00;
			//
			byte[] bytes = new byte[4];
			double r = Math.random();
			ByteBuffer.wrap(bytes).putFloat((float) r);
			//
			data[2] = bytes[0];
			data[3] = bytes[1];
			data[4] = bytes[2];
			data[5] = bytes[3];
			//
			try {
				clientSocket.send(new DatagramPacket(data, data.length,
						ipAddress, udpTargetPort));
				System.out.println("Market price sent for underlying: "
						+ (float) r);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//
		//
		// //
		// for (final String s : channels) {
		// //
		// IReceiver receiver = transFac.getReceiver(ETransportType.STATE, s);
		// //
		// final IPublisher publisher = transFac.getPublisher(s);
		// receiver.getRawEvent().addEventListener(
		// new IEventListener<byte[]>() {
		// @Override
		// public void eventFired(byte[] arg0) {
		// // base message.
		// ValueSet vs = null;
		// String field = vs.getField();
		// String value = vs.getValue();
		// Double v = Double.parseDouble(value);
		// //
		// byte commandByte = 0x00;
		// if(field.equals("M")){
		// //
		// commandByte = 0x00;
		// }
		// else if(field.equals("V")){
		// //
		// commandByte = 0x01;
		// }
		// //
		// Integer id = Integer.parseInt(s);
		// //
		// byte instrumentId = id.byteValue();
		// //
		// // assemble a datagram and send it ...
		// byte[] data = new byte[6];
		// data[0] = commandByte;
		// data[1] = instrumentId;
		// //
		// byte[] bytes = new byte[4];
		// ByteBuffer.wrap(bytes).putFloat(v.floatValue());
		// //
		// data[2] = bytes[0];
		// data[3] = bytes[1];
		// data[4] = bytes[2];
		// data[5] = bytes[3];
		// //
		// try {
		// clientSocket.send(new DatagramPacket(bytes, bytes.length, ipAddress,
		// udpTargetPort));
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// //
		// }
		// });
		// }
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
