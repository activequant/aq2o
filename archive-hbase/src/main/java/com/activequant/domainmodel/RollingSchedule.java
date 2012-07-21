package com.activequant.domainmodel;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.springframework.dao.DataIntegrityViolationException;

import com.activequant.domainmodel.exceptions.NoEntryForDate;

public class RollingSchedule extends TreeMap<Long, String> {

    private static final long serialVersionUID = 1L;

    public RollingSchedule(Long[] rollDates, String[] validInstrumentIds) {
        assert (rollDates.length == validInstrumentIds.length);
        if (rollDates.length != validInstrumentIds.length) {
            throw new DataIntegrityViolationException("Length mismatch");
        }
        for (int i = 0; i < rollDates.length; i++) {
            if (rollDates[i] != null && validInstrumentIds[i] != null)
                put(rollDates[i], validInstrumentIds[i]);
        }
    }

    /**
     * returns the valid instrument Id for a specific date.
     * 
     * @param date8
     * @return
     */
    public String getValidInstrument(Long date8) throws NoEntryForDate {
        assert (date8 != null);
        Iterator<Entry<Long, String>> it = entrySet().iterator();
        String ret = null;
        while (it.hasNext()) {
            Entry<Long, String> e = it.next();
            if (e.getKey() > date8)
                return ret;
            ret = e.getValue();
        }
        throw new NoEntryForDate(date8.toString());
    }

}
