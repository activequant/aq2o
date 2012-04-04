package com.activequant.domainmodel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.activequant.utils.annotations.Property;

public class Future extends Derivative {

    private Long expiry;
    private Long firstTradingDate;
    private Long lastTradingDate;
    private Double lotSize;
    private Long rollDate;
    private Long settlementDate;
    private Double tickSize;
    private Long firstNotice; 
    private String monthYear; 
    private Double tickValue;

    public Future() {
        super(Future.class.getCanonicalName());
    }

    public Future(String instrumentId, String description, Long expiry, Double lotSize, Double tickSize,
            Double tickValue) {
        super(Future.class.getCanonicalName());
        super.setShortName(instrumentId);
        super.setDescription(description);
        this.expiry = expiry;
        this.lotSize = lotSize;
        this.tickSize = tickSize;
        this.tickValue = tickValue;
    }

    public double fullPointValue() {
        return 1.0 / tickSize * tickValue;
    }

    @Property
    public Long getExpiry() {
        return expiry;
    }

    @Property
    public Long getFirstTradingDate() {
        return firstTradingDate;
    }

    public String getId() {
        return "FUT." + nullSafe(getExchangeCode()) + "." + nullSafe(getShortName()) + "." + nullSafe(expiry);
    }

    @Property
    public Long getLastTradingDate() {
        return lastTradingDate;
    }

    @Property
    public Double getLotSize() {
        return lotSize;
    }

    @Property
    public Long getRollDate() {
        return rollDate;
    }

    @Property
    public Long getSettlementDate() {
        return settlementDate;
    }

    @Property
    public Double getTickSize() {
        return tickSize;
    }

    @Property
    public Double getTickValue() {
        return tickValue;
    }

    public boolean isNowExpired() {
        Calendar cal = GregorianCalendar.getInstance();
        Long currentDate = Long.parseLong(new SimpleDateFormat("yyyyMMdd").format(cal.getTime()));
        return (expiry < currentDate);
    }

    public void setExpiry(Long expiry) {
        this.expiry = expiry;
    }

    public void setFirstTradingDate(Long firstTradingDate) {
        this.firstTradingDate = firstTradingDate;
    }

    public void setLastTradingDate(Long lastTradingDate) {
        this.lastTradingDate = lastTradingDate;
    }

    public void setLotSize(Double lotSize) {
        this.lotSize = lotSize;
    }

    public void setRollDate(Long rollDate) {
        this.rollDate = rollDate;
    }

    public void setSettlementDate(Long settlementDate) {
        this.settlementDate = settlementDate;
    }

    public void setTickSize(Double tickSize) {
        this.tickSize = tickSize;
    }

    public void setTickValue(Double tickValue) {
        this.tickValue = tickValue;
    }

    @Property
	public Long getFirstNotice() {
		return firstNotice;
	}

	public void setFirstNotice(Long firstNotice) {
		this.firstNotice = firstNotice;
	}

	@Property
	public String getMonthYear() {
		return monthYear;
	}

	public void setMonthYear(String monthYear) {
		this.monthYear = monthYear;
	}

}
