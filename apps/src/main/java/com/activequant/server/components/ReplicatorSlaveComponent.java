package com.activequant.server.components;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.sql.DataSource;

import com.activequant.component.ComponentBase;
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

	//

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
				try {
					Statement statement = ds.getConnection().createStatement();

					URL u = new URL("http://" + replicationMaster
							+ "/datadump/?TABLEDUMP=" + s + "&CREATED="
							+ lastDump);
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
							String[] parts = inputLine.split("-;-");
							String keyVal = parts[0];
							String created = parts[1];
							String fieldName = parts[2];
							String stringVal = parts[3];
							if (parts[3].length() == 0)
								stringVal = null;
							String longVal = parts[4];
							if (parts[4].length() == 0)
								longVal = null;
							String doubleVal = parts[5];
							if (parts[5].length() == 0)
								doubleVal = null;
							// ...
							String query1 = "DELETE FROM " + s
									+ " WHERE keyVal=\"" + keyVal
									+ "\" and fieldName=\"" + fieldName + "\";";
							statement.execute(query1);
							String query2 = "INSERT INTO " + s + " VALUES (\""
									+ keyVal + "\", " + created + ", \""
									+ fieldName + "\", " + " \"" + stringVal != null ? stringVal
									: "NULL" + "\", " + longVal != null ? longVal
											: "NULL" + "," + doubleVal != null ? doubleVal
													: "NULL" + ");";
							statement.execute(query2);

						}
					}
					br2.close();
					properties.put(s, "" + now);
					statement.close();
					storeProperties();
					//
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}

				//
				running = false;
			}
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

		//
	}

	//
	public static void main(String[] args) throws Exception {
		ITransportFactory t = new ActiveMQTransportFactory("localhost", 61616);
		new ReplicatorSlaveComponent(t, "localhost", null);
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
