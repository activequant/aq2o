package com.activequant.exceptions;

/**
 * Thrown to indicate a transport exception. 
 * 
 * @author ustaudinger
 *
 */
public class UnsupportedOrderType extends Exception {
    private static final long serialVersionUID = 1L;
    
    public UnsupportedOrderType(Throwable t){
    	super(t);
    }
    
    public UnsupportedOrderType(String text){
    	super(text);
    }
    
}
