package com.activequant.domainmodel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.activequant.domainmodel.annotations.Property;

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
    private String product;

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

    /**
     * expensive function.
     * @return
     */
    public TimeStamp getFirstTradingDateAsTS(){
    	SimpleDateFormat date8 = new SimpleDateFormat("yyyyMMdd");
    	try {
			return new TimeStamp(date8.parse(""+getFirstTradingDate()));
		} catch (ParseException e) {
			return null; 
		}
    }

    public String getId() {
        return "FUT." + nullSafe(getExchangeCode()) + "." + nullSafe(getShortName()) + "." + nullSafe(expiry);
    }

    @Property
    public Long getLastTradingDate() {
        return lastTradingDate;
    }

    /**
     * expensive function.
     * @return
     */
    public TimeStamp getLastTradingDateAsTS(){
    	SimpleDateFormat date8 = new SimpleDateFormat("yyyyMMdd");
    	try {
			return new TimeStamp(date8.parse(""+getLastTradingDate()));
		} catch (ParseException e) {
			return null; 
		}
    }

    @Property
    public Double getLotSize() {
        return lotSize;
    }

    @Property
    public Long getRollDate() {
        return rollDate;
    }

    /**
     * expensive function.
     * @return
     */
    public TimeStamp getRollDateAsTS(){
    	SimpleDateFormat date8 = new SimpleDateFormat("yyyyMMdd");
    	try {
			return new TimeStamp(date8.parse(""+getRollDate()));
		} catch (ParseException e) {
			return null; 
		}
    }

    @Property
    public Long getSettlementDate() {
        return settlementDate;
    }
    

    /**
     * expensive function.
     * @return
     */
    public TimeStamp getSettlementDateAsTS(){
    	SimpleDateFormat date8 = new SimpleDateFormat("yyyyMMdd");
    	try {
			return new TimeStamp(date8.parse(""+getSettlementDate()));
		} catch (ParseException e) {
			return null; 
		}
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

    /**
     * Should be in date8 format. 
     * @param expiry
     */
    public void setExpiry(Long expiry) {
        this.expiry = expiry;
    }

    /**
     * Should be in date8 format. 
     * @param firstTradingDate
     */
    public void setFirstTradingDate(Long firstTradingDate) {
        this.firstTradingDate = firstTradingDate;
    }

    /**
     * Should be in date8 format. 
     * @param lastTradingDate
     */
    public void setLastTradingDate(Long lastTradingDate) {
        this.lastTradingDate = lastTradingDate;
    }

    public void setLotSize(Double lotSize) {
        this.lotSize = lotSize;
    }

    /**
     * Should be in date8 format. 
     * @param rollDate
     */
    public void setRollDate(Long rollDate) {
        this.rollDate = rollDate;
    }

    /**
     * Should be in date8 format. 
     * @param settlementDate
     */
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

    /**
     * expensive function.
     * @return
     */
    public TimeStamp getFirstNoticeAsTS(){
    	SimpleDateFormat date8 = new SimpleDateFormat("yyyyMMdd");
    	try {
			return new TimeStamp(date8.parse(""+getFirstNotice()));
		} catch (ParseException e) {
			return null; 
		}
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

	@Property
	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

}
