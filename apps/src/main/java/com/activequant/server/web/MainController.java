package com.activequant.server.web;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.activequant.domainmodel.Instrument;
import com.activequant.domainmodel.MarketDataInstrument;
import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.TradeableInstrument;
import com.activequant.domainmodel.trade.event.OrderEvent;

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

	@RequestMapping("/webstart")
	public String webstart(Map<String, Object> map) {
		return "webstart";
	}
	
	@RequestMapping("/android_app")
	public String androidApp(Map<String, Object> map) {
		return "android_app";
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

	

	@RequestMapping("/data_inspector")
	public ModelAndView data_csv(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String componentId = request.getParameter("componentId");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("seriesid", request.getParameter("seriesid"));
		map.put("field", request.getParameter("field"));
		map.put("startdate", request.getParameter("startdate"));
		map.put("enddate", request.getParameter("enddate"));
		map.put("freq", request.getParameter("freq"));
		
		if(request.getParameter("field")==null)
			map.put("field", "BID");
		if(request.getParameter("startdate")==null)
			map.put("startdate", "20120101");
		if(request.getParameter("enddate")==null)
			map.put("enddate", "20131231");
		if(request.getParameter("freq")==null)
			map.put("freq", "RAW");
		
		
		return new ModelAndView("data_csv", map);
	}
	
	@RequestMapping("/documentation")
	public String documentation(Map<String, Object> map) {
		return "documentation_main";
	}
	
	@RequestMapping("/excel_addon")
	public String excelAddon(Map<String, Object> map) {
		return "excel_addon";
	}


	@RequestMapping(value = "/component/description", method = RequestMethod.GET)
	public @ResponseBody
	String description(@RequestParam String componentId) {
		if (sc.getComponentDescriptions().containsKey(componentId))
			return sc.getComponentDescriptions().get(componentId);
		return "N/A";
	}

	// @RequestMapping(value = "/component2", method = RequestMethod.GET)
	// public String component2(@RequestParam String componentId,
	// @RequestParam String msg,
	// return "component";
	// }

	@RequestMapping(value = "/component", method = RequestMethod.GET)
	public ModelAndView component2(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String componentId = request.getParameter("componentId");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("description", sc.getComponentDescriptions().get(componentId));
		map.put("id", componentId);
		map.put("name", sc.getComponentIdToName().get(componentId));

		String msg = request.getParameter("msg");
		if (msg != null) {
			sc.sendMessage(componentId, msg);
			map.put("msg", msg);
		}
		return new ModelAndView("component", map);
	}

	@RequestMapping(value = "/instruments", method = RequestMethod.GET)
	public ModelAndView instruments(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String searchString = request.getParameter("searchString");
		Map<String, Object> map = new HashMap<String, Object>();
		if (searchString != null)
			map.put("searchString", searchString);
		else
			map.put("searchString", "%");
		List<String> entries = new ArrayList<String>();
		if (searchString != null) {
			String[] ids = sc.getDaoFactory().instrumentDao()
					.findIdsLike(searchString);
			for (String s : ids)
				entries.add(s);
		}

		map.put("entries", entries);
		//
		return new ModelAndView("instruments", map);
	}

	@RequestMapping(value = "/instrument", method = RequestMethod.GET)
	public ModelAndView instrument(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String iid = request.getParameter("iid");
		Map<String, Object> map = new HashMap<String, Object>();
		if (iid != null) {			
			map.put("iid", iid);
			Instrument inst = sc.getDaoFactory().instrumentDao().load(iid);
			List<String> keys = new ArrayList<String>();
			keys.addAll(inst.getUnderlyingMap().keySet());
			Collections.sort(keys);
			map.put("keys", keys);
			map.put("instrument", inst.getUnderlyingMap());
			// load related mdis. 
			map.put("mdis", sc.getDaoFactory().mdiDao().findFor(inst));
			map.put("tdis", sc.getDaoFactory().tradeableDao().findFor(inst));
		}
		//
		return new ModelAndView("instrument", map);
	}
	
	
	

	@RequestMapping(value = "/mdis", method = RequestMethod.GET)
	public ModelAndView mdis(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String searchString = request.getParameter("searchString");
		Map<String, Object> map = new HashMap<String, Object>();
		if (searchString != null)
			map.put("searchString", searchString);
		else
			map.put("searchString", "%");
		List<String> entries = new ArrayList<String>();
		if (searchString != null) {
			String[] ids = sc.getDaoFactory().mdiDao()
					.findIdsLike(searchString);
			for (String s : ids)
				entries.add(s);
		}

		map.put("entries", entries);
		//
		return new ModelAndView("mdis", map);
	}

	@RequestMapping(value = "/mdi", method = RequestMethod.GET)
	public ModelAndView mdi(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String iid = request.getParameter("mdiid");
		Map<String, Object> map = new HashMap<String, Object>();
		if (iid != null) {			
			map.put("mdiid", iid);
			MarketDataInstrument inst = sc.getDaoFactory().mdiDao().load(iid);
			List<String> keys = new ArrayList<String>();
			keys.addAll(inst.getUnderlyingMap().keySet());
			Collections.sort(keys);
			map.put("keys", keys);
			map.put("mdi", inst.getUnderlyingMap());
		}
		//
		return new ModelAndView("mdi", map);
	}
	
	
	

	@RequestMapping(value = "/tdis", method = RequestMethod.GET)
	public ModelAndView tdis(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String searchString = request.getParameter("searchString");
		Map<String, Object> map = new HashMap<String, Object>();
		if (searchString != null)
			map.put("searchString", searchString);
		else
			map.put("searchString", "%");
		List<String> entries = new ArrayList<String>();
		if (searchString != null) {
			String[] ids = sc.getDaoFactory().mdiDao()
					.findIdsLike(searchString);
			for (String s : ids)
				entries.add(s);
		}

		map.put("entries", entries);
		//
		return new ModelAndView("tdis", map);
	}

	@RequestMapping(value = "/tdi", method = RequestMethod.GET)
	public ModelAndView tdi(HttpServletRequest request,
			HttpServletResponse response) throws Exception {	
		String iid = request.getParameter("tdiid");
		Map<String, Object> map = new HashMap<String, Object>();
		if (iid != null) {			
			map.put("tdiid", iid);
			TradeableInstrument inst = sc.getDaoFactory().tradeableDao().load(iid);
			List<String> keys = new ArrayList<String>();
			keys.addAll(inst.getUnderlyingMap().keySet());
			Collections.sort(keys);
			map.put("keys", keys);
			map.put("tdi", inst.getUnderlyingMap());
		}
		//
		return new ModelAndView("tdi", map);
	}
	
	
	
	@RequestMapping(value = "/orderevents", method = RequestMethod.GET)
	public ModelAndView orderEvents(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String date = request.getParameter("date");
		if(date!=null){
			// ok, we have a date, let's fetch all order events for this date. 
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			Date d1 = sdf.parse(date+"000000");
			Date d2 = sdf.parse(date+"235959");			
			// 
			String[] ids = sc.getDaoFactory().orderEventDao().findIDsWhereCreationDateBetween(new TimeStamp(d1), new TimeStamp(d2));
			map.put("eventIds", ids);
			map.put("date", date);
			// 
		}
		
		return new ModelAndView("orderevents", map);
	}
	 
	@RequestMapping(value = "/orderevent", method = RequestMethod.GET)
	public ModelAndView orderEvent(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String id = request.getParameter("id");
		if(id!=null){
		
			OrderEvent oe = sc.getDaoFactory().orderEventDao().load(id);		
			List<String> keys = new ArrayList<String>();
			keys.addAll(oe.getUnderlyingMap().keySet());
			Collections.sort(keys);
			map.put("keys", keys);
			map.put("event", oe.getUnderlyingMap());			
			// 
		}
		
		return new ModelAndView("orderevent", map);
	}
	
	
	
}