package com.activequant.servicelayer.matlab;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.exceptions.InvalidDate8Time6Input;
import com.activequant.utils.TimeTools;

public class TimeStampProcessor {

    // TODO: have to replace this ParseException with some proper, meaningful
    // exception at one day.
    public List<TimeStamp> processTimeStampList(List<TimeStamp> timeStamps, Map<Parameter, Object> paramMap)
            throws InvalidDate8Time6Input {
        List<TimeStamp> ret = timeStamps;
        TimeTools tt = new TimeTools();
        
        // 
        
        
        // TODO: put into utility class.
        // check if we have a week-day only parameter
        if (paramMap.containsKey(Parameter.DAYRULE)) {
            if (paramMap.get(Parameter.DAYRULE) == DayRule.WEEKDAYS) {
                List<TimeStamp> tsTemp = new ArrayList<TimeStamp>();
                for (int i = 0; i < timeStamps.size(); i++) {
                    if (tt.isWeekday(timeStamps.get(i)))
                        tsTemp.add(timeStamps.get(i));
                }
                ret = tsTemp;
            }
        }
        
        // 
        
        
        return ret;

    }
}
