package com.activequant.domainmodel;

import org.apache.commons.codec.digest.DigestUtils;

import com.activequant.domainmodel.annotations.Property;

/**
 * A very basic macro event class.
 * 
 * @author GhostRider
 * 
 */
public class BasicMacroEvent extends PersistentEntity {

	private long date8time6;
	private String currency, event, importance, actual, forecast, previous;

	public BasicMacroEvent() {
		super(BasicMacroEvent.class.getCanonicalName());
	}

	@Property
	public String getActual() {
		return actual;
	}

	@Property
	public String getCurrency() {
		return currency;
	}

	@Property
	public long getDate8time6() {
		return date8time6;
	}

	@Property
	public String getEvent() {
		return event;
	}

	@Property
	public String getForecast() {
		return forecast;
	}

	public String getId() {
		// let's make a hash out of our event.
		return DigestUtils.md5Hex(event) + "." + date8time6;
	}

	@Property
	public String getImportance() {
		return importance;
	}

	@Property
	public String getPrevious() {
		return previous;
	}

	public void setActual(String actual) {
		this.actual = actual;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setDate8time6(long date8time6) {
		this.date8time6 = date8time6;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public void setForecast(String forecast) {
		this.forecast = forecast;
	}

	public void setImportance(String importance) {
		this.importance = importance;
	}

	public void setPrevious(String previous) {
		this.previous = previous;
	}

}
