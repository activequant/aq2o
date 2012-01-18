package com.activequant.utils;

import com.activequant.domainmodel.SecurityChainByDate;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SecurityChainByDateTest extends TestCase {
	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(SecurityChainByDateTest.class);
	}

	public void testChains(){
		SecurityChainByDate sc = new SecurityChainByDate();
		sc.add("A", 2010L);
		assertEquals(1, sc.getRollDates().length);
		
		sc.add("B", 2011L);
		assertEquals(2, sc.getRollDates().length);
		
		assertEquals("A", sc.getValidInstrumentIDs()[0]);
		assertEquals("B", sc.getValidInstrumentIDs()[1]);
		
		assertEquals(new Long(2010L), sc.getRollDates()[0]);
		assertEquals(new Long(2011L), sc.getRollDates()[1]);
		
		
	}
}
