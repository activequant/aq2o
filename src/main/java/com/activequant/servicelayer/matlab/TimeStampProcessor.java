package com.activequant.servicelayer.matlab;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.activequant.domainmodel.Date8Time6;
import com.activequant.exceptions.InvalidDate8Time6Input;

public class TimeStampProcessor {

    // TODO: have to replace this ParseException with some proper, meaningful
    // exception at one day.
    public List<Double> processTimeStampList(List<Double> timeStamps, Map<Parameter, Object> paramMap)
            throws InvalidDate8Time6Input {
        List<Double> ret = timeStamps;
        // TODO: put into utility class.
        // check if we have a week-day only parameter
        if (paramMap.containsKey(Parameter.DAYRULE)) {
            if (paramMap.get(Parameter.DAYRULE) == DayRule.WEEKDAYS) {
                List<Double> tsTemp = new ArrayList<Double>();
                for (int i = 0; i < timeStamps.size(); i++) {
                    Date8Time6 d1 = new Date8Time6(timeStamps.get(i));
                    if (d1.isWeekday())
                        tsTemp.add(timeStamps.get(i));
                }
                ret = tsTemp;
            }
        }
        return ret;

    }
}
