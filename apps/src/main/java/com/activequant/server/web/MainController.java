package com.activequant.server.web;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {
	
	// 
	private Logger log = Logger.getLogger(MainController.class);
	@Autowired
	private ServerComponent sc; 
	
	public MainController(){
		log.info("Instantiating main controller.");		
	}
	
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	@RequestMapping("/")
	public String index(Map<String, Object> map) {
		map.put("serverTime", sdf.format(new Date()));
		return "index";
	}
	
	@RequestMapping("/about")
	public String about(Map<String, Object> map) {
		
		return "about";
	}
	
	@RequestMapping("/server")
	public String server(Map<String, Object> map) {		
		
		Properties properties = new Properties();
        try {
			properties.load(new FileInputStream("framework.properties"));
		} catch (IOException e) { 
			e.printStackTrace();
		}
        
        TreeMap<String, Object> tmap2 = new TreeMap<String, Object>();
        for(Object s : properties.keySet()){
        	String key = (String)s; 
        	tmap2.put(key, properties.get(key));
        }
		
		
		properties = new Properties();
        try {
			properties.load(new FileInputStream("aq2server.properties"));
		} catch (IOException e) { 
			e.printStackTrace();
		}
        
        TreeMap<String, Object> tmap = new TreeMap<String, Object>();
        for(Object s : properties.keySet()){
        	String key = (String)s; 
        	tmap.put(key, properties.get(key));
        }
        
        map.put("framework", tmap2);
		map.put("aq2server", tmap);
		return "server";
	}
		
	@RequestMapping("/administration")
	public String administration(Map<String, Object> map) {
		return "administration";
	}
	
	@RequestMapping("/components")
	public String components(Map<String, Object> map) {
		// 
		class Entry{
			String name; 
			String lastSeen;
			String id;
			String getId(){return id;}
		}
		
		//
		List<Map<String, String>> entries = new ArrayList<Map<String, String>>();
		Iterator<String> keyIt = sc.getComponentIdToName().keySet().iterator();
		while(keyIt.hasNext()){
			String key = keyIt.next();
			Map<String, String> m = new HashMap<String, String>();			
			m.put("name", sc.getComponentIdToName().get(key));
			m.put("id", key);
			m.put("lastSeen", sdf.format(new Date(sc.getComponentLastSeen().get(key))));
			entries.add(m);			
		}
		
		// 
		map.put("entries", entries);
		
		
		return "components";
	}
	
	
	@RequestMapping("/license")
	public String license(Map<String, Object> map) {
		return "license";
	}
	

	@RequestMapping("/refdata")
	public String refdata(Map<String, Object> map) {
		return "refdata";
	}
	

	@RequestMapping("/marketdata")
	public String marketdata(Map<String, Object> map) {
		return "marketdata";
	}
	

	@RequestMapping("/traddata")
	public String traddata(Map<String, Object> map) {
		return "traddata";
	}
	
	
}