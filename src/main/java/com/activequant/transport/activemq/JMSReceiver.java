package com.activequant.transport.activemq;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.activequant.domainmodel.PersistentEntity;
import com.activequant.transport.IReceiver;
import com.activequant.utils.MapToString;
import com.activequant.utils.events.Event;
import com.activequant.utils.events.IEventSource;

class JMSReceiver implements IReceiver, MessageListener {
	JSONParser parser = new JSONParser();

	JMSReceiver() {
	}

	private Logger log = Logger.getLogger(JMSReceiver.class.getName());
	private Event<Map<String, Object>> rawMsgRecEvent = new Event<Map<String, Object>>();
	private Event<PersistentEntity> msgRecEvent = new Event<PersistentEntity>();

	/**
	 * 
	 * the converter.
	 */
	private MapToString mapToString = new MapToString();

	public IEventSource<Map<String, Object>> getRawMsgRecEvent() {
		return rawMsgRecEvent;
	}

	public IEventSource<PersistentEntity> getMsgRecEvent() {
		return msgRecEvent;
	}

	@SuppressWarnings("unchecked")
	public void onMessage(Message message) {
	

        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                String text = textMessage.getText();
                // convert to hashmap.
                
                
                
                Object obj = parser.parse(text);
                Map<String, Object> mmap = (Map)obj; // mapToString.convert(text);
                
                // 
                
                Map<String, Object> map = new HashMap<String, Object>();
        		Iterator<Entry<String, Object>> eit = mmap.entrySet().iterator();
        		
        		while(eit.hasNext()){
        			Entry<String, Object> e = eit.next();
        			String key = e.getKey();
        			Object val = e.getValue();
        			if(val==null)continue;
        			if(!(val instanceof JSONArray)){
        				map.put(key, val);
        			}
        			else{
        				JSONArray js = (JSONArray)val;
        				map.put(key, js.toArray());
        				if(js.size()>0){
        					Object o = js.get(0);
        					if(o==null)continue; 
        					String simpleName = o.getClass().getSimpleName();

        					if(simpleName.startsWith("Double"))
            				{
        						double[] array = new double[js.size()];
        						for(int i=0;i<js.size();i++)
        						{
        							Object o1 = js.get(i);
        							if(o1==null)continue; 
        							array[i] = ((Double)o1).doubleValue();
        						}
        						map.put(key, array);
            				}
            				else if(simpleName.startsWith("Long"))
            				{
            					long[] array = new long[js.size()];
        						for(int i=0;i<js.size();i++)
        						{
        							Object o1 = js.get(i);
        							if(o1==null)continue; 
        							array[i] = ((Long)o1).longValue();
        						}
        						map.put(key, array);
            				}
            				else if(simpleName.startsWith("String"))
            				{
            					String[] array = new String[js.size()];
        						for(int i=0;i<js.size();i++)
        						{
        							Object o1 = js.get(i);
        							if(o1==null)continue; 
        							array[i] = ((String)o1);
        						}
        						map.put(key, array);
            				}
        					
        				}
        				
        			}
        		}
          
                
                rawMsgRecEvent.fire(map);
                // check if it is a persistent entity. 
              
                // TODO for speed up reasons, make it possible to disable this.                 
                if(map.containsKey("CLASSNAME")){
                	Class<PersistentEntity> clazz;
					try {
						clazz = (Class<PersistentEntity>) 
								Class.forName((String)map.get("CLASSNAME"));
						PersistentEntity pe = clazz.newInstance();
						pe.initFromMap(map);
						msgRecEvent.fire(pe);
					} catch (ClassNotFoundException e) {
						log.warn("Map containing classname, but could not instantiate it. ", e);
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}                	
                	 
                }
                
            } catch (JMSException e) {
                log.warn("Error while receiving message", e);
            } catch (ParseException e) {
				// TODO Auto-generated catch block
                log.warn("Error while parsing message", e);
			}
        }
		

    }
}
