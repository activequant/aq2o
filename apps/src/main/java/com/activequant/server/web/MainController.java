package com.activequant.server.web;

import java.io.FileInputStream;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

	//
	private Logger log = Logger.getLogger(MainController.class);
	@Autowired
	private ServerComponent sc;

	public MainController() {
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

	@RequestMapping("/charting")
	public String charting(Map<String, Object> map) {
		return "charting";
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
		for (Object s : properties.keySet()) {
			String key = (String) s;
			tmap2.put(key, properties.get(key));
		}

		properties = new Properties();
		try {
			properties.load(new FileInputStream("aq2server.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		TreeMap<String, Object> tmap = new TreeMap<String, Object>();
		for (Object s : properties.keySet()) {
			String key = (String) s;
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
		class Entry {
			String name;
			String lastSeen;
			String id;

			String getId() {
				return id;
			}
		}

		//
		List<String> pruneList = new ArrayList<String>();
		List<Map<String, String>> entries = new ArrayList<Map<String, String>>();
		Iterator<String> keyIt = sc.getComponentIdToName().keySet().iterator();
		while (keyIt.hasNext()) {
			String key = keyIt.next();

			long lastSeen = sc.getComponentLastSeen().get(key);
			if (System.currentTimeMillis() - lastSeen > (1000 * 60 * 5)) {
				pruneList.add(key);
			} else {
				Map<String, String> m = new HashMap<String, String>();
				m.put("name", sc.getComponentIdToName().get(key));
				m.put("id", key);
				m.put("lastSeen", sdf.format(new Date(sc.getComponentLastSeen()
						.get(key))));
				entries.add(m);
			}
		}

		//
		map.put("entries", entries);

		for (String s : pruneList) {
			sc.getComponentDescriptions().remove(s);
			sc.getComponentIdToName().remove(s);
			sc.getComponentLastSeen().remove(s);

		}
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

	@RequestMapping("/documentation")
	public String documentation(Map<String, Object> map) {
		return "documentation_main";
	}

	@RequestMapping(value = "/component/description", method = RequestMethod.GET)
	public @ResponseBody
	String description(@RequestParam String componentId) {
		if (sc.getComponentDescriptions().containsKey(componentId))
			return sc.getComponentDescriptions().get(componentId);
		return "N/A";
	}

	@RequestMapping(value = "/component", method = RequestMethod.GET)
	public String component(@RequestParam String componentId,
			Map<String, Object> map) {
		map.put("description", sc.getComponentDescriptions().get(componentId));
		map.put("id", componentId);
		map.put("name", sc.getComponentIdToName().get(componentId));
		return "component";
	}

}