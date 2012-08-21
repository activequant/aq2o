package com.activequant.server.trading;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.jboss.netty.channel.Channel;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.messages.AQMessages.BaseMessage;

public class SessionManager {

	private final Timer timer = new Timer();
	private final MessageFactory mf = new MessageFactory();
	private List<Channel> priceChannels = new ArrayList<Channel>();
	private List<Channel> tradeChannels = new ArrayList<Channel>();

	public void register(Channel socketChannel, String username, String session) {
		if(session.equals("PRICE"))priceChannels.add(socketChannel);
		if(session.equals("TRADE"))tradeChannels.add(socketChannel);
	}

	public SessionManager() {
		// register a time sender.
		reschedule();
	}

	private void reschedule() {
		int secondsBetweenResend = 1; 
		long timeUntilNextSecond = secondsBetweenResend * 1000 - new Date().getTime() % (secondsBetweenResend * 1000); 
		timer.schedule(new TimerTask() {
			public void run() {
				sendOutTime();
			}
		}, timeUntilNextSecond);
	}

	public void sendOutTime() {
		TimeStamp t = new TimeStamp();
		BaseMessage st = mf.buildServerTime(t.getNanoseconds());		
		broadcastToPrice(st);
		broadcastToTrade(st);
		reschedule();
	}
	
	public void sendOutMds(String mdiId, double bid, double ask, double bidQ, double askQ){
		BaseMessage st = mf.buildMds(mdiId, bid, ask, bidQ, askQ);		
		broadcastToPrice(st);
	}
	
	private void broadcastToTrade(BaseMessage bm){
		List<Channel> toberemoved = new ArrayList<Channel>();
		for(Channel sc : tradeChannels){
			if(!sendToChannel(sc, bm))
				toberemoved.add(sc);
		}
		// 
		for(Channel sc : toberemoved)
			priceChannels.remove(sc);
	}
	
	
	private void broadcastToPrice(BaseMessage bm){
		List<Channel> toberemoved = new ArrayList<Channel>();
		for(Channel sc : priceChannels){
			if(!sendToChannel(sc, bm))
				toberemoved.add(sc);
		}
		// 
		for(Channel sc : toberemoved)
			priceChannels.remove(sc);
	}
	
	private boolean sendToChannel(Channel c, BaseMessage bm){
		if(c.isOpen() && c.isWritable())
			c.write(bm);
		else{
			System.out.println("Channel not available. ");
			return false; 
		}
		return true; 
		
	}

}
