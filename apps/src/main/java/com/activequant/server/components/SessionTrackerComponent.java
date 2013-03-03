package com.activequant.server.components;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import com.activequant.component.ComponentBase;
import com.activequant.domainmodel.ETransportType;
import com.activequant.interfaces.transport.ITransportFactory;
import com.activequant.interfaces.utils.IEventListener;
import com.activequant.transport.activemq.ActiveMQTransportFactory;

/**
 * 
 * 
 * @author GhostRider
 * 
 */
public class SessionTrackerComponent extends ComponentBase {

	//
	private static Map<String, String> userSessionMap = new HashMap<String, String>();
	private static Map<String, Long> userLastSeenMap = new HashMap<String, Long>();

	//
	public static void main(String[] args) throws Exception {
		ITransportFactory t = new ActiveMQTransportFactory("localhost", 61616);
		new SessionTrackerComponent(t);
	}

	class PruneTask extends TimerTask {
		public void run() {
			//
			Long now = new Date().getTime();
			Long sessionLifeTime = 15000L;
			Long pruneAge = (now - sessionLifeTime);
			Iterator<Entry<String, Long>> it = userLastSeenMap.entrySet()
					.iterator();
			List<String> pruneList = new ArrayList<String>();
			while (it.hasNext()) {
				//
				Entry<String, Long> entry = it.next();
				if (entry.getValue() < pruneAge) {
					//
					pruneList.add(entry.getKey());
				}
				//
			}
			for (String s : pruneList) {
				userLastSeenMap.remove(s);
				userSessionMap.remove(s);
			}
		}
	}

	//
	public SessionTrackerComponent(ITransportFactory transFac) throws Exception {
		super("SessionTracker", transFac);
		transFac.getReceiver(ETransportType.CONTROL, "SESSIONS").getRawEvent()
				.addEventListener(new IEventListener<byte[]>() {
					@Override
					public void eventFired(byte[] arg0) {
						//
						String uidIp = new String(arg0);
						if (uidIp.contains(";")) {
							String uid = uidIp.split(";")[0];
							String ip = uidIp.split(";")[1];
							SessionTrackerComponent.trackUserSession(uid, ip);
							//
						}
					}
				});

		// regularly prune stale sessions.
		Timer timer = new Timer();
		timer.schedule(new PruneTask(), 0, 5 * 1000);

		//
	}

	/**
	 * Using null to signal.
	 * 
	 * @param uid
	 * @return
	 */
	public static String hasUserSession(String uid, String ip) {
		if (userSessionMap.containsKey(uid)
				&& !userSessionMap.get(uid).equals(ip))
			return userSessionMap.get(uid);
		return null;
	}

	//
	public static void trackUserSession(String uid, String ip) {
		userSessionMap.put(uid, ip);
		userLastSeenMap.put(uid, new Date().getTime());
	}

	//
	@Override
	public String getDescription() {
		//
		return "The session tracker component keeps track of active user sessions.";

	}

}
