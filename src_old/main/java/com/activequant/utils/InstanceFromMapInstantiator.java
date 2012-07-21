package com.activequant.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.activequant.domainmodel.PersistentEntity;

public class InstanceFromMapInstantiator<T extends PersistentEntity> {
	
	/**
	 * dirty. 
	 * filtering out typical BB artifacts: NAs and #
	 * 
	 * @param inMap
	 * @return
	 */
	public T loadStringString(Map<String, String> inMap)
	{
		Map<String, Object> tempMap = new HashMap<String, Object>();
		Iterator<Entry<String, String>> it = inMap.entrySet().iterator();
		while(it.hasNext())
		{
			Entry<String, String> e = it.next();
			if(e.getValue().equals("n/a") || e.getValue().equals("#"))continue; 
			tempMap.put(e.getKey(), e.getValue());
		}
		return load(tempMap);
	}
	
	@SuppressWarnings("unchecked")
	public T load(Map<String, Object> inMap){
        String className = (String) inMap.get("ClassName".toUpperCase());
        if (className == null)
            // no valid entry.
            return null;
        T ret = null;
        try {
            @SuppressWarnings({ "rawtypes" })
            Class clazz = Class.forName(className);
            ret = (T) clazz.newInstance();
            ret.initFromMap(inMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret; 
	}

}
