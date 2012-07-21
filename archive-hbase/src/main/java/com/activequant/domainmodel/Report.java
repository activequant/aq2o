package com.activequant.domainmodel;

import com.activequant.domainmodel.annotations.Property;

/**
 * Just mark all properties as @Property.
 * 
 * @author ustaudinger
 * 
 */
public class Report extends PersistentEntity {

    private String id;
    private String sourceFolder;
    private String status = "N/A";

    public Report() {
        super(Report.class.getCanonicalName());
    }

	@Property
    public String getId() {
        return id;
    }
    
    @Property
	public String getSourceFolder() {
		return sourceFolder;
	}

	public void setSourceFolder(String sourceFolder) {
		this.sourceFolder = sourceFolder;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Property
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
