package com.activequant;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.renjin.sexp.DoubleVector;

import com.activequant.utils.RenjinCore;

/**
 * Unit test for simple App.
 */
public class RenjinCoreTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public RenjinCoreTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(RenjinCoreTest.class);
	}

	public void testRenjin() throws Exception {
		RenjinCore r = new RenjinCore();
		r.put("a", 1);
		r.execute("b = c(a, sqrt(a + 1));");
		r.execute("b = c(a, sqrt(a + 1), sqrt(50));");
		r.execute("d = sum(b);");
		r.execute("sdb = sd(c(1,2,3));");
		// r.execute("plot(b);");
		
		assertEquals(DoubleVector.class, r.get("b").getClass());
		assertEquals(9.485281374238571, ((DoubleVector)r.get("d")).get(0));
		
		System.out.println("Obj: " + r.get("b") + " " + r.get("b").getClass());
		System.out.println(r.get("d"));
		System.out.println(r.get("sdb"));
	}

}
