package com.activequant.backtesting;

import java.util.List;

import com.activequant.domainmodel.PersistentEntity;
import com.activequant.utils.annotations.Property;

public class BacktestConfiguration extends PersistentEntity {

	private String id;
	private long date8Time6Start;
	private long date8Time6End;
	private String backtesterImplementation;
	private String[] mdis;
	private String[] tdis;
	
	private String resolutionTimeFrame;

	public BacktestConfiguration(){
		super(BacktestConfiguration.class.getCanonicalName());
	}
	
	public BacktestConfiguration(String className){
		super(className);
	}
	
	@Property
	@Override
	public String getId() {
		return id;
	}

	@Property
	public double getDate8Time6Start() {
		return date8Time6Start;
	}

	public void setDate8Time6Start(long date8Time6start) {
		this.date8Time6Start = date8Time6start;
	}

	@Property
	public double getDate8Time6End() {
		return date8Time6End;
	}

	public void setDate8Time6End(long date8Time6End) {
		this.date8Time6End = date8Time6End;
	}

	@Property
	public String getBacktesterImplementation() {
		return backtesterImplementation;
	}

	public void setBacktesterImplementation(String backtesterImplementation) {
		this.backtesterImplementation = backtesterImplementation;
	}

	@Property
	public String getResolutionTimeFrame() {
		return resolutionTimeFrame;
	}

	public void setResolutionTimeFrame(String resolutionTimeFrame) {
		this.resolutionTimeFrame = resolutionTimeFrame;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Property
	public String[] getMdis() {
		return mdis;
	}

	public void setMdis(String[] mdis) {
		this.mdis = mdis;
	}

	@Property
	public String[] getTdis() {
		return tdis;
	}

	public void setTdis(String[] tdis) {
		this.tdis = tdis;
	}

}
