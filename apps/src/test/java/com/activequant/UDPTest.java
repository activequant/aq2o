package com.activequant;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.BeforeClass;

import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.interfaces.transport.IPublisher;
import com.activequant.messages.AQMessages.BaseMessage;
import com.activequant.messages.Marshaller;
import com.activequant.messages.MessageFactory;
import com.activequant.server.components.UDPRelay;
import com.activequant.transport.memory.InMemoryTransportFactory;

/**
 * Unit test for our UDP relay for Fabian.
 */
public class UDPTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public UDPTest(String testName) {
		super(testName);
	}

	class UdpEchoServer implements Runnable {
		static final int BUFFERSIZE = 256;

		public void run() {
			System.out.println("Starting local relay");
			DatagramSocket sock;
			DatagramPacket pack = new DatagramPacket(new byte[BUFFERSIZE],
					BUFFERSIZE);
			try {
				sock = new DatagramSocket(54321);
			} catch (SocketException e) {
				System.out.println(e);
				return;
			}
			// echo back everything
			while (true) {
				try {
					sock.receive(pack);
					//
					System.out.println("===PacketDump======");
					for (int i = 0; i < pack.getData().length; i++) {
						System.out.print(pack.getData()[i] + " ");
					}
					System.out.println("\n=========");
					//
					System.out.println("Received from:  " + pack.getAddress() + ":"
							+ pack.getPort());
					// let's send it back ... 
					pack.setPort(12345);
					sock.send(pack);
				} catch (IOException ioe) {
					System.out.println(ioe);
				}
			}
		}
	}
	
    
	

	//
    
	public void setUp() throws InterruptedException {
    	Thread t = new Thread(new UdpEchoServer());
    	t.setDaemon(true);
    	t.start();
    	// let's give it some time to sleep. 
    	Thread.sleep(100);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(UDPTest.class);
	}

	@org.junit.Test
	public void testUDPRelay() throws Exception {
		//
		InMemoryTransportFactory t = new InMemoryTransportFactory();
		UDPRelay ur = new UDPRelay(t,"localhost", 54321, 12345);
		//
		// let's create some test messages. 
		Marshaller m = new Marshaller();
		MessageFactory mf = new MessageFactory();
		
		BaseMessage vs = mf.valueSet(new TimeStamp(), "STATE", "1", "M", "100.0");
		IPublisher p = t.getPublisher(ETransportType.STATE, "1");
		p.send(vs.toByteArray());
		
		// 				
		Thread.sleep(100);
		
		vs = mf.valueSet(new TimeStamp(), "STATE", "1", "V", "100.0");
		p.send(vs.toByteArray());
		
		Thread.sleep(5000);
		// 
		//
		
	}

}
