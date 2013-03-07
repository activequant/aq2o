package com.activequant.utils;

/**
 * DEPRECATED. Was used and isn't used any more, I think.
 * 
 * @author GhostRider
 * 
 */

@Deprecated
public enum ActiveQuantDataField {

	// will have to load this from DB at one point.
	BID(Double.class, "BID"), ASK(Double.class, "ASK"), BIDVOL(Double.class,
			"BIDVOL"), ASKVOL(Double.class, "ASKVOL"), EXP_DT(Long.class,
			"EXP_DT"), SETTLEMENTDATE(Long.class, "SETTLEMENTDATE"), GATEWAYTIMESTAMP(
			Long.class, "GATEWAYTIMESTAMP"), CREATED(Long.class, "CREATED"), NAME(
			String.class, "NAME"), ROLLDATE(Long.class, "ROLLDATE"), SEC_TYP(
			String.class, "SEC_TYP"), FIRSTTRADINGDATE(Long.class,
			"FIRSTTRADINGDATE");

	private Class<?> clazz;
	private String keyName;

	ActiveQuantDataField(Class<?> clazz, String keyName) {
		this.clazz = clazz;
		this.keyName = keyName;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public String keyName() {
		return keyName;
	}

	public Object getValueObject(String textRepresentation) {
		if (getClazz().equals(Double.class)) {
			return Double.parseDouble(textRepresentation);
		} else if (getClazz().equals(Integer.class)) {
			return Integer.parseInt(textRepresentation);
		} else if (getClazz().equals(Long.class)) {
			return Long.parseLong(textRepresentation);
		} else if (getClazz().equals(String.class)) {
			return textRepresentation;
		} else
			return textRepresentation;

	}

}
