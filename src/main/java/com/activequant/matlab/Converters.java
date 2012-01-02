package com.activequant.matlab;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.activequant.domainmodel.Date8Time6;
import com.activequant.exceptions.InvalidDate8Time6Input;

public class Converters {
    private double offset = 719529.0;

    public TimeSeriesContainer convertDataMap(List<Double> timeStamps,
            Map<String, Map<String, Map<Double, Double>>> dataMap, String[] marketInstrumentIds, String[] fieldNames,
            Map<Parameter, Object> paramMap) throws InvalidDate8Time6Input, ParseException {
        timeStamps = new TimeStampProcessor().processTimeStampList(timeStamps, paramMap);

        //
        Collections.sort(timeStamps);
        TimeSeriesContainer ret = new TimeSeriesContainer();
        // convert the timestamps to matlab timestamps
        double[] matlabTS = new double[timeStamps.size()];
        for (int i = 0; i < timeStamps.size(); i++) {
            double milliseconds = new Date8Time6(timeStamps.get(i)).asMicroSeconds() / 1000.0;
            matlabTS[i] = milliseconds / 1000.0 / 86400.0 + offset;
        }
        // assemble the return matrix.
        ret.timeStamps(matlabTS);
        ret.values(new double[marketInstrumentIds.length][timeStamps.size()][fieldNames.length]);
        for (int i = 0; i < marketInstrumentIds.length; i++) {
            String instrument = marketInstrumentIds[i];
            // get the instrument specific map.
            Map<String, Map<Double, Double>> instrumentMap = dataMap.get(instrument);
            for (int j = 0; j < fieldNames.length; j++) {
                String field = fieldNames[j];
                // get the field specific map
                Map<Double, Double> fieldMap = instrumentMap.get(field);
                for (int k = 0; k < timeStamps.size(); k++) {
                    double day = timeStamps.get(k);
                    if (fieldMap.containsKey(day))
                        ret.values()[i][k][j] = fieldMap.get(day);
                    else
                        ret.values()[i][k][j] = Double.NaN;
                }
            }
        }

        ret.values(new ReturnMapProcessor().processTimeStampList(ret.values(), paramMap));

        return ret;
    }
}
