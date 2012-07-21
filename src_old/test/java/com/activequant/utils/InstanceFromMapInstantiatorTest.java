package com.activequant.utils;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.activequant.domainmodel.Future;
import com.activequant.domainmodel.Instrument;

public class InstanceFromMapInstantiatorTest extends TestCase {
	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(InstanceFromMapInstantiatorTest.class);
	}

	public void testInitFromStringStringMap(){
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("CLASSNAME", "com.activequant.domainmodel.Future");
		map.put("SHORTNAME", "FDAX201212");
		map.put("NAME", "FDAX Dec 2012");
		map.put("DESCRIPTION", "Dax future");
		map.put("LOTSIZE", "1");
		map.put("TICKSIZE", "0.5");
		map.put("TICKVALUE", "12.5");
		map.put("EXPIRY", "20121227");
		
		InstanceFromMapInstantiator<Instrument> i = new InstanceFromMapInstantiator<Instrument>();
		Instrument instr = i.loadStringString(map);
		
		assertEquals(instr.getClass(), Future.class);
		Future f = (Future)instr; 
		assertEquals(12.5, f.getTickValue());
		assertEquals(0.5, f.getTickSize());
		assertEquals(1.0, f.getLotSize());		
		assertEquals(Long.valueOf(20121227l), f.getExpiry());
			
	}
}
