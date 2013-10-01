package com.activequant.domainmodel;

import java.io.Serializable;

public class GenericRow implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long created;
    private Double doubleVal;
    private String fieldName;
    private Long longVal;
    private String keyVal;
    private String stringVal;

    public GenericRow() {
    }

    public GenericRow(Long created, String keyVal, String fieldName, Long intVal, Double doubleVal, String stringVal) {
        this.created = created;
        this.keyVal = keyVal;
        this.fieldName = fieldName;
        this.longVal = intVal;
        this.doubleVal = doubleVal;
        this.stringVal = stringVal;
    }

    public Long getCreated() {
        return created;
    }

    public Double getDoubleVal() {
        return doubleVal;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Long getLongVal() {
        return longVal;
    }

    public String getKeyVal() {
        return keyVal;
    }

    public String getStringVal() {
        return stringVal;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public void setDoubleVal(Double doubleVal) {
        this.doubleVal = doubleVal;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setLongVal(Long intVal) {
        this.longVal = intVal;
    }

    public void setKeyVal(String keyVal) {
        this.keyVal = keyVal;
    }

    public void setStringVal(String stringVal) {
        this.stringVal = stringVal;
    }

}
