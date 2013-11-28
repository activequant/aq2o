package com.activequant.domainmodel.streaming;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import com.activequant.domainmodel.ETransportType;
import com.activequant.domainmodel.PersistentEntity;
import com.activequant.domainmodel.TimeStamp;


@Entity
@MappedSuperclass
public abstract class StreamEvent extends PersistentEntity {
	@Column
	private TimeStamp timeStamp;
	public StreamEvent(String className){
		super(className);
	}
	public abstract ETransportType getEventType();
	public TimeStamp getTimeStamp(){
		return timeStamp; 
	}
	public void setTimeStamp(TimeStamp ts){
		timeStamp = ts; 
	}
}
