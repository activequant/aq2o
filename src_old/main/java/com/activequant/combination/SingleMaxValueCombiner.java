package com.activequant.combination;

import java.util.ArrayList;
import java.util.List;

public class SingleMaxValueCombiner {

    public List<OutputDataRow> combine(String valueSeriesKey, String denominatorSeriesKey, String[] instrumentIds) {
        return null;
    }

    public List<OutputDataRow> combine(List<InputDataRow> valSeries) {
        List<OutputDataRow> ret = new ArrayList<OutputDataRow>();
        for (InputDataRow dr : valSeries)
            ret.add(combine(dr));
        return ret;
    }

    private OutputDataRow combine(InputDataRow row) {
        double val = Double.MIN_VALUE;
        Integer index = null;
        for (int i = 0; i < row.denominators.length; i++) {
            if (row.denominators[i] != null && row.denominators[i] > val) {
                val = row.values[i];
                index = i;
            }
        }

        OutputDataRow ret = new OutputDataRow();
        if (index != null) {
            ret.value = val;
            ret.usedInstrument = row.instruments[index];
        }
        return ret;
    }

}