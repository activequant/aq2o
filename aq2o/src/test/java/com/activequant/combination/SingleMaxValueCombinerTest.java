package com.activequant1.combination;

import java.util.ArrayList;
import java.util.List;

import com.activequant.combination.InputDataRow;
import com.activequant.combination.OutputDataRow;
import com.activequant.combination.SingleMaxValueCombiner;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SingleMaxValueCombinerTest extends TestCase {
    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(SingleMaxValueCombinerTest.class);
    }

    public void testCombination() {
        List<InputDataRow> l = new ArrayList<InputDataRow>();
        InputDataRow idr1 = new InputDataRow();
        idr1.instruments = new String[] { "a", "b" };
        idr1.values = new Double[] { 10.0, -10.0 };
        idr1.denominators = new Double[] { 10.0, 20.0 };
        InputDataRow idr2 = new InputDataRow();
        idr2.instruments = new String[] { "a", "b" };
        idr2.values = new Double[] { 10.0, -10.0 };
        idr2.denominators = new Double[] { 20.0, 10.0 };

        l.add(idr1);
        l.add(idr2);

        SingleMaxValueCombiner s = new SingleMaxValueCombiner();
        List<OutputDataRow> rows = s.combine(l);
        assertEquals(2, rows.size());
        assertEquals(-10.0, rows.get(0).value);
        assertEquals("b", rows.get(0).usedInstrument);
        assertEquals(10.0, rows.get(1).value);
        assertEquals("a", rows.get(1).usedInstrument);

    }

}
