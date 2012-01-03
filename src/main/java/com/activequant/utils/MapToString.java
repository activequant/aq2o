package com.activequant.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

public class MapToString {

    private static Logger log = Logger.getLogger(MapToString.class);

    public String convert(Map<String, Object> mapIn) {
        StringBuffer r = new StringBuffer();
        Iterator<String> keyIterator = mapIn.keySet().iterator();
        Iterator<Object> valueIterator = mapIn.values().iterator();
        while (keyIterator.hasNext()) {
            //
            String key = keyIterator.next();
            Object value = valueIterator.next();
            // might have to do base64 encoding at one point.
            r.append(key);
            r.append("=");
            r.append(value.toString());
            r.append(";");
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
                log.warn("Invalid value received: " + s);
                continue;
            }
            String key = keyVal[0];
            try {
                ActiveQuantDataField field = ActiveQuantDataField.valueOf(key);
                if (field != null) {
                    ret.put(key, field.getValueObject(keyVal[1]));
                } else {
                    log.warn("No AimHedgeDataField found for feed key >>" + key + "<<");
                }
            } catch (Exception ex) {
                if (tryDouble(keyVal[1]) != null)
                    ret.put(key, tryDouble(keyVal[1]));
                else if (tryString(keyVal[1]) != null)
                    ret.put(key, tryString(keyVal[1]));
                else
                    log.warn("Dropping unconvertable value");
            }
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
