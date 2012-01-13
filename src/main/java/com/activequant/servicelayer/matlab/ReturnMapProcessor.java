package com.activequant.servicelayer.matlab;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.activequant.domainmodel.Date8Time6;

public class ReturnMapProcessor {

    // TODO: have to replace this ParseException with some proper, meaningful
    // exception at one day.
    public double[][][] processTimeStampList(double[][][] input, Map<Parameter, Object> paramMap) throws ParseException {

        double[][][] ret = input;
        // TODO: make smarter.
        // check if we have an NAN interpolation roule.
        if (paramMap.containsKey(Parameter.INTERPOLRULE)) {
            if (paramMap.get(Parameter.INTERPOLRULE) == InterpolationRule.CARRY_FORWARD) {
                for (int i = 0; i < input.length; i++) {
                    //
                    int days = input[i].length;
                    if (days < 2)
                        continue;
                    //
                    int fields = input[i][0].length;
                    if (fields < 1)
                        continue;
                    for (int field = 0; field < fields; field++) {
                        Double lastVal = null;
                        for (int day = 0; day < days; day++) {
                            Double currentVal = input[i][day][field];
                            if (currentVal == null || Double.isNaN(currentVal))
                                ret[i][day][field] = lastVal;
                            else
                                lastVal = currentVal;
                        }
                    }
                }
            }
        }

        return ret;

    }
}
