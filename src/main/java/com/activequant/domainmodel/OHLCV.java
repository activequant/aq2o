package com.activequant.domainmodel;

import com.activequant.tools.streaming.TimeStreamEvent;
import com.activequant.utils.annotations.Property;

/**
 * Chandlers make candles. 
 * 
 * @author GhostRider
 *
 */
public class OHLCV extends TimeStreamEvent {

	private String mdiId;
	private Integer resolutionInSeconds;
	private Double open, high, low, close, volume;
	private String id; 

	public OHLCV() {
		super(OHLCV.class.getCanonicalName());
	}

	@Property
	public String getMdiId() {
		return mdiId;
	}

	public void setMdiId(String mdiId) {
		// also reset the ID. 
		id = "OHLCV:"+mdiId+"."+ resolutionInSeconds;
		this.mdiId = mdiId;
	}

	@Property
	public Double getOpen() {
		return open;
	}

	public void setOpen(Double open) {
		this.open = open;
	}

	@Property
	public Double getHigh() {
		return high;
	}

	public void setHigh(Double high) {
		this.high = high;
	}

	@Property
	public Double getLow() {
		return low;
	}

	public void setLow(Double low) {
		this.low = low;
	}

	@Property
	public Double getClose() {
		return close;
	}

	public void setClose(Double close) {
		this.close = close;
	}

	@Property
	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	@Override
	public String getId() {
		if(id==null)
			id = "OHLCV:"+mdiId+"."+ resolutionInSeconds; 
		return id;
	}

	@Property
	public Integer getResolutionInSeconds() {
		return resolutionInSeconds;
	}

	public void setResolutionInSeconds(Integer resolutionInSeconds) {
		// also reset the ID. 
		id = "OHLCV:"+mdiId+"."+ resolutionInSeconds;
		
		this.resolutionInSeconds = resolutionInSeconds;
	}

	public void update(TimeStamp ts, Double val){
		if(open==null)
		{
			open = high = low = close = val; 
			volume = 0.0;
		}
			
		if(val > high) high = val; 
		if(val < low) low= val;
		// last value. 
		close = val;
		
		// framing it. 
		long frame = ts.getNanoseconds() - ts.getNanoseconds() % (resolutionInSeconds * 1000l * 1000l * 1000l); 
		setTimeStamp(new TimeStamp(frame));
	}
	
	public void clear(){
		open = high = low = close = volume = null;
		setTimeStamp(null);
	}
	
	public OHLCV clone(){
		OHLCV ret = new OHLCV();
		ret.setSnapshotTime(getSnapshotTime());
		ret.setTimeStamp(super.getTimeStamp());
		ret.setMdiId(mdiId);
		ret.setOpen(open);
		ret.setHigh(high);
		ret.setLow(low);
		ret.setClose(close);
		ret.setVolume(volume);
		ret.setResolutionInSeconds(resolutionInSeconds);
		ret.setCreationTime(super.getCreationTime());
		ret.setDeletionTime(super.getDeletionTime());
		return ret; 
	}
}
