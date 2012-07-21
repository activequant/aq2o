package com.activequant.servicelayer.matlab;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.activequant.domainmodel.TimeStamp;
import com.activequant.domainmodel.exceptions.InvalidDate8Time6Input;
import com.activequant.utils.Date8Time6Parser;


public class Converters {
    private double offset = 719529.0;

    public TimeSeriesContainer convertDataMap(List<TimeStamp> timeStamps,
            Map<String, Map<String, Map<TimeStamp, Double>>> dataMap, String[] marketInstrumentIds, String[] fieldNames,
            Map<Parameter, Object> paramMap) throws InvalidDate8Time6Input, ParseException {
        timeStamps = new TimeStampProcessor().processTimeStampList(timeStamps, paramMap);

        //
        Collections.sort(timeStamps);
        Date8Time6Parser parser = new Date8Time6Parser(); 
        TimeSeriesContainer ret = new TimeSeriesContainer();
        // convert the timestamps to matlab timestamps
        double[] matlabTS = new double[timeStamps.size()];
        for (int i = 0; i < timeStamps.size(); i++) {
            double milliseconds = timeStamps.get(i).getNanoseconds() / 1000000.0;
            matlabTS[i] = milliseconds / 1000.0 / 86400.0 + offset;
        }
        // assemble the return matrix.
        ret.timeStamps(matlabTS);
        ret.values(new double[marketInstrumentIds.length][timeStamps.size()][fieldNames.length]);
        for (int i = 0; i < marketInstrumentIds.length; i++) {
            String instrument = marketInstrumentIds[i];
            // get the instrument specific map.
            Map<String, Map<TimeStamp, Double>> instrumentMap = dataMap.get(instrument);
            for (int j = 0; j < fieldNames.length; j++) {
                String field = fieldNames[j];
                // get the field specific map
                Map<TimeStamp, Double> fieldMap = instrumentMap.get(field);
                for (int k = 0; k < timeStamps.size(); k++) {
                    TimeStamp date8time6 = timeStamps.get(k);
                    if (fieldMap.containsKey(date8time6))
                        ret.values()[i][k][j] = fieldMap.get(date8time6);
                    else
                        ret.values()[i][k][j] = Double.NaN;
                }
            }
        }

        ret.values(new ReturnMapProcessor().processTimeStampList(ret.values(), paramMap));

        return ret;
    }
}
