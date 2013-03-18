package com.activequant;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.interfaces.transport.IPublisher;
import com.activequant.messages.AQMessages.BaseMessage;
import com.activequant.messages.Marshaller;
import com.activequant.messages.MessageFactory;
import com.activequant.server.components.UDPRelay;
import com.activequant.transport.memory.InMemoryTransportFactory;

/**
 * BlackScholes Test for Fabian
 * 
 * black scholes implementation from 
 * http://introcs.cs.princeton.edu/java/22library/BlackScholes.java.html 
 * 
 */
public class BlackScholesTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public BlackScholesTest(String testName) {
		super(testName);
	}
	
	private List<Double> execTimes = new ArrayList<Double>();

	   // Black-Scholes formula
    public static double callPrice(double S, double X, double r, double sigma, double T) {
        double d1 = (Math.log(S/X) + (r + sigma * sigma/2) * T) / (sigma * Math.sqrt(T));
        double d2 = d1 - sigma * Math.sqrt(T);
        return S * Gaussian.Phi(d1) - X * Math.exp(-r * T) * Gaussian.Phi(d2);
    }
	
	class BlackScholesServer implements Runnable {
		static final int BUFFERSIZE = 6*200;

		private double sharePrice = 50;
		private int instruments = 200; 
		private double riskFreeRate = 0.05; 
		private double dividendYield = 0.00; 
		private double expiryInYears = 0.2; // 73 days of 365 days
		private double volatility = 0.2; // 20% assumed volatility
		
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
//					System.out.println("===PacketDump======");
//					for (int i = 0; i < pack.getData().length; i++) {
//						System.out.print(pack.getData()[i] + " ");
//					}
//					System.out.println("\n=========");
					// extract the first byte ... 
					
					byte[] floatValue = new byte[4];				
					//
					System.arraycopy(pack.getData(), 2, floatValue, 0,
							4);
					//					
					//
					float f = ByteBuffer.wrap(floatValue)
							.order(ByteOrder.BIG_ENDIAN).getFloat();
					
					if(pack.getData()[0]==0x01){
						// ok, market price
						this.sharePrice = (double)f; 
					}
					else if(pack.getData()[0]==0x02){
						// ok, volatility
						this.volatility = (double)f; 
					}
					else
						return;
					
					byte[] buf = new byte[200*6];
					
					long nanoTime = System.nanoTime();
					// let's price our options. 
					for(int i=0;i<instruments;i++){
						double strike = 10.0 + i; 
						Double p = callPrice(sharePrice, strike, riskFreeRate, volatility, expiryInYears);
						buf[i*6 + 0] = 0x01;
						
						//
						Integer id = new Integer(i+1);
						//
						byte instrumentId = id.byteValue();
						buf[i*6 + 1] = instrumentId;
						byte[] bytes = new byte[4];
						ByteBuffer.wrap(bytes).putFloat(p.floatValue());
						//
						buf[i*6 + 2] = bytes[0];
						buf[i*6 + 3] = bytes[1];
						buf[i*6 + 4] = bytes[2];
						buf[i*6 + 5] = bytes[3];
					}
					long nanoTime2 = System.nanoTime();
					System.out.println(  (nanoTime2 - nanoTime)/1000.0  + " microseconds."   );
					
					execTimes.add((nanoTime2 - nanoTime)/1000.0);
					
					
					DatagramPacket dp = new DatagramPacket(buf, 200*6);
					//
					System.out.println("Received from:  " + pack.getAddress() + ":"
							+ pack.getPort());
					dp.setPort(12345);
					dp.setAddress(pack.getAddress());
					// let's send it back ... 					
					sock.send(dp);
				} catch (IOException ioe) {
					System.out.println(ioe);
				}
			}
		}
	}
	
    
	

	//
    
	public void setUp() throws InterruptedException {
    	Thread t = new Thread(new BlackScholesServer());
    	t.setDaemon(true);
    	t.start();
    	// let's give it some time to sleep. 
    	Thread.sleep(100);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(BlackScholesTest.class);
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
		for(int i=0;i<1000;i++){
			vs = mf.valueSet(new TimeStamp(), "STATE", "1", "V", ""+Math.random());
			p.send(vs.toByteArray());
			Thread.sleep(10);
		}
		
		for(Double d : execTimes){
			System.out.println(d);
		}
		
		Thread.sleep(5000);
		// 
		//
		
	}

}
