package com.activequant.server.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.activequant.interfaces.dao.IDaoFactory;

/**
 * 
 * @author GhostRider
 * 
 */
public class RefDataCSVServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IDaoFactory daoFac;
	private Logger log = Logger.getLogger(RefDataCSVServlet.class);

	public RefDataCSVServlet(IDaoFactory daoFac) {
		this.daoFac = daoFac;
	}

	private String instructions = "You need to specify TYPE, ID, and optionally FIELD. "
			+ "";

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Map paramMap = req.getParameterMap();
		PrintWriter response = resp.getWriter();
		try {

			if (paramMap.containsKey("TYPE") && paramMap.containsKey("ID")) {
				String field = null;
				if (paramMap.containsKey("FIELD"))
					field = ((String[]) paramMap.get("FIELD"))[0];
				String type = ((String[]) paramMap.get("TYPE"))[0];
				String id = ((String[]) paramMap.get("ID"))[0];
				Map<String, Object> resultMap = new HashMap<String, Object>();
				Map<String, Object> m = new HashMap<String, Object>();
				//
				if (type.equals("INSTRUMENT")) {
					// let's fetch the instrument.
					m = daoFac.instrumentDao().load(id).getUnderlyingMap();
					//
				} else if (type.equals("MDI")) {
					// let's fetch the mdi
					m = daoFac.mdiDao().load(id).getUnderlyingMap();
				} else if (type.equals("TDI")) {
					// let's fetch the tdi
					m = daoFac.tradeableDao().load(id).getUnderlyingMap();
				}
				//
				if (field != null) {
					// fetch all.
					if (m.containsKey(field)) {
						resultMap.put(field, m.get(field));
					} else {
						resultMap.put(field, "N/A");
					}
				} else {
					resultMap.putAll(m);
				}
				// let's dump the result map.
				List<String> keys = new ArrayList<String>();
				keys.addAll(resultMap.keySet());
				Collections.sort(keys);
				response.print(id + "\n");
				for (String key : keys) {
					response.print(key);
					response.print("=");
					response.print(resultMap.get(key));
					response.print("\n");
				}
				response.flush();

			} else {
				response.print(instructions);
				response.flush();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			response.print(ex);
			response.flush();
		}
	}
}