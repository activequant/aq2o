package com.activequant.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MapToString {

	public String convert(Map<String, Object> mapIn) {
		StringBuffer r = new StringBuffer();
		Iterator<String> keyIterator = mapIn.keySet().iterator();
		Iterator<Object> valueIterator = mapIn.values().iterator();
		while (keyIterator.hasNext()) {
			//
			String key = keyIterator.next();
			Object value = valueIterator.next();
			if (value != null) {
				// might have to do base64 encoding at one point.
				r.append(key);
				r.append("=");
				r.append(value.toString());
				r.append(";");
			}
		}

		return r.toString();
	}

	public Map<String, Object> convert(String sIn) {
		Map<String, Object> ret = new HashMap<String, Object>();
		String[] keyVals = sIn.split(";");
		for (String s : keyVals) {
			if (s.length() == 0)
				continue;
			String[] keyVal = s.split("=");
			if (keyVal.length != 2) {
				continue;
			}
			String key = keyVal[0];
			if (tryDouble(keyVal[1]) != null)
				ret.put(key, tryDouble(keyVal[1]));
			else if (tryString(keyVal[1]) != null)
				ret.put(key, tryString(keyVal[1]));

		}
		return ret;

	}

	private Double tryDouble(String val) {
		try {
			return Double.parseDouble(val);
		} catch (Exception ex) {
			return null;
		}
	}

	private String tryString(String val) {
		try {
			return val;
		} catch (Exception ex) {
			return null;
		}
	}
}
