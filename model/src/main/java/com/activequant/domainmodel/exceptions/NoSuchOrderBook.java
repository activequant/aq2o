package com.activequant.domainmodel.exceptions;

/**
 * Thrown to indicate a transport exception. 
 * 
 * @author ustaudinger
 *
 */
public class NoSuchOrderBook extends Exception {
    private static final long serialVersionUID = 1L;
    
    public NoSuchOrderBook(Throwable t){
    	super(t);
    }
    
    public NoSuchOrderBook(String text){
    	super(text);
    }
    
}
