package com.activequant.domainmodel;

import com.activequant.domainmodel.annotations.Property;
import com.activequant.domainmodel.exceptions.NoEntryForDate;

/**
 * 
 * @author ustaudinger
 *
 */
public class InstrumentChain extends Instrument {

	private Long[] rollDates;
	private String[] validInstrument;

	public InstrumentChain() {
		super(InstrumentChain.class.getCanonicalName());
	}

	/**
	 * returns the valid instrument Id for a specific date.
	 * 
	 * @param date8
	 * @return
	 */
	public String getValidInstrument(Long date8) throws NoEntryForDate {
		assert (date8 != null);
		String ret = null; 
		for(int i=0;i<rollDates.length;i++){					
			if (rollDates[i] > date8)
				return ret;
			ret = validInstrument[i];
		}
		throw new NoEntryForDate(date8.toString());
	}

	public String getId() {
		return "INST_CHAIN." + getName();
	}

	@Property()
	public Long[] getRollDates() {
		return rollDates;
	}

	@Property()
	public String[] getValidInstrument() {
		return validInstrument;
	}

	public void setRollDates(Long[] rollDates) {
		this.rollDates = rollDates;
	}

	public void setValidInstrument(String[] validInstrument) {
		this.validInstrument = validInstrument;
	}

	public String toString() {
		return getId() + "/" + getName();
	}
}
