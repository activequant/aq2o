package com.activequant.server.components;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.sql.DataSource;

import com.activequant.component.ComponentBase;
import com.activequant.domainmodel.TimeFrame;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.interfaces.archive.IArchiveWriter;
import com.activequant.interfaces.transport.ITransportFactory;
import com.activequant.transport.activemq.ActiveMQTransportFactory;

/**
 * 
 * 
 * @author GhostRider
 * 
 */
public class ReplicatorSlaveComponent extends ComponentBase {

	//
	private String replicationMaster = "213";
	private String targetTables = "Instrument,MarketDataInstrument,TradeableInstrument";
	private DataSource ds = null;
	private static boolean running = false;
	private static boolean running2 = false;
	private IArchiveWriter iaw = null; 

	//

	class ReplicationTask2 extends TimerTask {
		public void run() {
			if (running2)
				return;
			running2 = true;
			//
			
			
			//
			String seriesId = "OFDP/ALUMINIUM_21";
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

			try {
				TimeStamp ts = new TimeStamp(sdf.parse("20000101"));
				URL u = new URL("http://" + replicationMaster
						+ "/csv/?DUMP=1&SERIESID=" + seriesId + "&FREQ="
						+ TimeFrame.EOD + "&START=" + ts.getNanoseconds());
				// open the buffered reader.
				InputStreamReader isr = new InputStreamReader((u.openStream()));
				BufferedReader br2 = new BufferedReader(isr);
				//
				String inputLine;
				StringBuffer sb = new StringBuffer();
				int counter = 0; 
				while ((inputLine = br2.readLine()) != null) {
					// ok, we got a line.
					if (inputLine.length() > 0) {
						counter ++; 
						System.out.println(inputLine);
						String[] parts = inputLine.split(";");
						Long timestamp = Long.parseLong(parts[0]);
						if (parts.length > 0) {
							for (int i = 1; i < parts.length; i++) {
								String[] keyVal = parts[i].split("="); 
								String key = keyVal[0]; 
								Double val = Double.parseDouble(keyVal[1]);
								// 
								if(iaw!=null){
									iaw.write(seriesId, new TimeStamp(timestamp), key, val);
									if(counter>100){
										counter = 0; 
										iaw.commit(); 								
									}
								}
								//
							}
						}

					}
				}
				if(iaw!=null)
					iaw.commit();

			} catch (Exception e) {
				e.printStackTrace();
			}

			//
			running2 = false;
		}
	}

	class ReplicationTask extends TimerTask {
		public void run() {
			if (running)
				return;
			running = true;
			//
			Long now = new Date().getTime();
			String[] tables = targetTables.split(",");
			for (String s : tables) {

				// let's build a url connection and read and write while we go.
				Long lastDump = Long.parseLong(properties.getProperty(s, "0"));
				Statement statement = null;
				Connection con = null;
				try {
					con = ds.getConnection();

					statement = con.createStatement();
					URL u = new URL("http://" + replicationMaster
							+ "/datadump/?TABLEDUMP=" + s + "&CREATED="
							+ lastDump);
					//
					// open the buffered reader.
					BufferedReader br2 = new BufferedReader(
							new InputStreamReader(u.openStream()));
					//
					String inputLine;
					StringBuffer sb = new StringBuffer();
					while ((inputLine = br2.readLine()) != null) {
						// ok, we got a line.
						if (inputLine.length() > 0) {
							// ...
							String[] parts1 = inputLine.split("-;-");
							String[] parts = new String[6];
							for (int i = 0; i < parts1.length; i++)
								parts[i] = parts1[i];
							String keyVal = parts[0];
							String created = parts[1];
							String fieldName = parts[2];
							String stringVal = parts[3];
							if (parts[3] != null && parts[3].length() == 0)
								stringVal = null;
							String longVal = parts[4];
							if (parts[4] != null && parts[4].length() == 0)
								longVal = null;
							String doubleVal = parts[5];
							if (parts[5] != null && parts[5].length() == 0)
								doubleVal = null;
							// ...
							String query1 = "DELETE FROM " + s
									+ " WHERE keyVal='" + keyVal
									+ "' and fieldName='" + fieldName + "';";
							System.out.println(query1);
							statement.executeUpdate(query1);
							String query2 = "INSERT INTO "
									+ s
									+ " (keyVal, created, fieldName, stringVal, longVal, doubleVal) VALUES ('"
									+ keyVal + "', " + created + ", '"
									+ fieldName + "', " + " '"
									+ (stringVal != null ? stringVal : "NULL")
									+ "', "
									+ (longVal != null ? longVal : "NULL")
									+ ","
									+ (doubleVal != null ? doubleVal : "NULL")
									+ ");";
							System.out.println(query2);
							statement.executeUpdate(query2);
							con.commit();

						}
					}
					br2.close();
					properties.put(s, "" + now);

					storeProperties();
					//
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					if (con != null)
						try {
							con.commit();
							con.close();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}

					if (statement != null)
						try {
							statement.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
				}

				//

			}
			running = false;
		}
	}

	public ReplicatorSlaveComponent(ITransportFactory transFac,
			String replicationMaster, DataSource ds) throws Exception {
		super("Replicator Slave", transFac);
		this.replicationMaster = replicationMaster;
		this.ds = ds;
		//
		Timer timer = new Timer();
		timer.schedule(new ReplicationTask(), 0, 10 * 60 * 1000);
		Timer timer2 = new Timer();
		timer2.schedule(new ReplicationTask2(), 0 * 60 * 1000, 10 * 60 * 1000);

		//
	}

	//
	public static void main(String[] args) throws Exception {
		ITransportFactory t = new ActiveMQTransportFactory("localhost", 61616);
		new ReplicatorSlaveComponent(t, "localhost:44444", null);
	}

	//
	public void customMessage(String message) {
		if (message.equals("REPLICATE")) {
			new ReplicationTask().run();
		}
	}

	//
	@Override
	public String getDescription() {
		//
		return "This replicator slave is connected to "
				+ replicationMaster
				+ ". It will replicate automatically every 10 minutes. Tables to be replicated: "
				+ this.targetTables + ". There is a replication running: "
				+ running;

	}

}
