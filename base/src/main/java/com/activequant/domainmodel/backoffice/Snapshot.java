package com.activequant.domainmodel.backoffice;

import com.activequant.domainmodel.PersistentEntity;
import com.activequant.domainmodel.annotations.Property;

public abstract class Snapshot extends PersistentEntity {
    public long timeStampInNanoseconds;

	
	public abstract String getNonUniqueID();
	
	public final void setNonUniqueID(String id){}
	
	public Snapshot(String className){super(className);}

	

	@Override
	public String getId() {
		return getNonUniqueID() + "." + nullSafe(getCreationTime());
	}

    @Property
    public long getTimeStampInNanoseconds() {
        return timeStampInNanoseconds;
    }
    
    public void setTimeStampInNanoseconds(long timeStampInNanoseconds) {
        this.timeStampInNanoseconds = timeStampInNanoseconds;
        super.setCreationTime(timeStampInNanoseconds);
        super.setSnapshotTime(timeStampInNanoseconds);
    }


}
