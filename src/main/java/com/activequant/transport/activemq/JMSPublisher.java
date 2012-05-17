package com.activequant.transport.activemq;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

import com.activequant.domainmodel.PersistentEntity;
import com.activequant.transport.IPublisher;
import com.activequant.utils.MapToString;

class JMSPublisher implements IPublisher {
	private JSONParser parser = new JSONParser();

	private MessageProducer producer;
	private Session session;
	private MapToString mapToString = new MapToString();
	private Logger log = Logger.getLogger(JMSPublisher.class);
	private final String channelId;

	JMSPublisher(Session session, MessageProducer producer, String channelId) {
		this.producer = producer;
		this.session = session;
		this.channelId = channelId;
	}

	public void send(Map<String, Object> message) throws Exception {
				
		
		Map<String, Object> mmap = new HashMap<String, Object>();
		Iterator<Entry<String, Object>> eit = message.entrySet().iterator();
		
		while(eit.hasNext()){
			Entry<String, Object> e = eit.next();
			String key = e.getKey();
			Object val = e.getValue();
			if(val==null)continue;
			if(!val.getClass().isArray()){
				mmap.put(key, val);
			}
			else{
				String simpleName = val.getClass().getSimpleName();
				if(simpleName.startsWith("double"))
				{
					JSONArray l = new JSONArray();
					for(double d: (double[])val){
						l.add(d);
					}
					mmap.put(key, l);
				}
				else if(simpleName.startsWith("long"))
				{
					JSONArray l = new JSONArray();
					for(long d: (long[])val){
						l.add(d);
					}
					mmap.put(key, l);
				}
				else if(simpleName.startsWith("String"))
				{
					JSONArray l = new JSONArray();
					for(String d: (String[])val){
						l.add(d);
					}
					mmap.put(key, l);
				}
			}
		}
		String text = JSONValue.toJSONString(mmap);

		if (log.isDebugEnabled())
			log.debug("[channelId=" + channelId + "] message: " + text);
		TextMessage tm = session.createTextMessage(text);
		tm.setStringProperty("channelId", channelId);
		
		producer.send(tm);
	}

	@Override
	public void send(PersistentEntity entity) throws Exception {
		send(entity.propertyMap());
	}

}
